package net.errs.internal.util.bookshelf;

import net.errs.internal.util.bookshelf.dto.BookDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;
import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BookshelfService {

    private static final Logger log = LoggerFactory.getLogger(BookshelfService.class);
    private static final int PREVIEW_MAX_WIDTH = 320;
    private static final int PREVIEW_MAX_HEIGHT = 420;
    private static final float PREVIEW_DPI = 144f;
    private static final String CACHE_SUFFIX = ".svg";
    private static final HexFormat HEX = HexFormat.of();

    private final Path rootPath;
    private final Path cachePath;
    private final Map<String, PreviewCacheEntry> previewCache = new ConcurrentHashMap<>();
    private final ExecutorService indexExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "bookshelf-indexer");
        thread.setDaemon(true);
        return thread;
    });
    private final AtomicBoolean indexing = new AtomicBoolean(false);

    private volatile List<BookItem> allItems = List.of();
    private volatile Map<String, List<BookItem>> childrenByParent = Map.of();

    public BookshelfService(@Value("${bookshelf.path:./books}") String rootPath,
                            @Value("${bookshelf.preview-cache.path:}") String cachePath) throws IOException {
        this.rootPath = Path.of(rootPath).toAbsolutePath().normalize();
        Files.createDirectories(this.rootPath);

        if (cachePath == null || cachePath.isBlank()) {
            this.cachePath = this.rootPath.resolve(".bookshelf-cache").normalize();
        } else {
            this.cachePath = Path.of(cachePath).toAbsolutePath().normalize();
        }
        Files.createDirectories(this.cachePath);

        triggerReindex();
    }

    public boolean triggerReindex() {
        if (indexing.compareAndSet(false, true)) {
            indexExecutor.submit(() -> {
                try {
                    performReindex();
                } catch (IOException e) {
                    log.error("Bookshelf reindex failed", e);
                } finally {
                    indexing.set(false);
                }
            });
            return true;
        }
        return false;
    }

    public boolean isIndexing() {
        return indexing.get();
    }

    private void performReindex() throws IOException {
        List<Path> paths;
        try (Stream<Path> stream = Files.walk(rootPath)) {
            paths = stream.filter(path -> !path.equals(rootPath))
                    .collect(Collectors.toList());
        }

        List<BookItem> items = paths.parallelStream()
                .map(this::toBookSafe)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        items.sort(Comparator.comparing(item -> item.relativePath.toLowerCase(Locale.ROOT)));

        Map<String, List<BookItem>> children = new HashMap<>();
        items.forEach(item -> children.computeIfAbsent(item.parentPath, k -> new ArrayList<>()).add(item));

        children.values().forEach(list ->
                list.sort(Comparator
                        .comparing((BookItem i) -> !i.directory)
                        .thenComparing(i -> i.name.toLowerCase()))
        );

        this.allItems = List.copyOf(items);
        this.childrenByParent = children.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        e -> List.copyOf(e.getValue())
                ));

        trimPreviewCache(items);
    }

    public List<BookDTO> listFolder(String path) {
        return childrenByParent.getOrDefault(normalizeRelative(path), List.of())
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<BookDTO> search(String q) {
        if (q == null || q.isBlank()) {
            return List.of();
        }
        String normalized = q.toLowerCase();
        boolean glob = normalized.indexOf('*') >= 0 || normalized.indexOf('?') >= 0;
        Pattern pattern = glob ? Pattern.compile(globToRegex(normalized), Pattern.CASE_INSENSITIVE) : null;

        return allItems.stream()
                .filter(item -> matchesQuery(item, normalized, pattern))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public String parentPath(String path) {
        String normalized = normalizeRelative(path);
        if (normalized.isEmpty()) {
            return null;
        }
        Path parent = Path.of(normalized).getParent();
        if (parent == null) {
            return "";
        }
        return toRelativeString(parent);
    }

    public Path resolveFile(String path) throws IOException {
        String normalized = normalizeRelative(path);
        if (normalized.isEmpty()) {
            throw new NoSuchFileException("Path refers to root");
        }
        Path resolved = rootPath.resolve(normalized).normalize();
        if (!Files.exists(resolved) || Files.isDirectory(resolved)) {
            throw new NoSuchFileException("File not found: " + normalized);
        }
        return resolved;
    }

    private String normalizeRelative(String path) {
        if (path == null || path.isBlank()) {
            return "";
        }
        Path resolved = rootPath.resolve(path).normalize();
        if (!resolved.startsWith(rootPath)) {
            throw new IllegalArgumentException("Path escapes bookshelf root");
        }
        Path relative = rootPath.relativize(resolved);
        return toRelativeString(relative);
    }

    private String toRelativeString(Path path) {
        if (path == null) {
            return "";
        }
        return path.toString().replace('\\', '/');
    }

    private BookItem toBook(Path path) {
        try {
            Path relative = rootPath.relativize(path);
            String relativePath = toRelativeString(relative);
            String parent = relative.getParent() == null ? "" : toRelativeString(relative.getParent());
            boolean directory = Files.isDirectory(path);
            long size = directory ? 0L : Files.size(path);
            String type = directory ? "folder" : Files.probeContentType(path);
            if (type == null) {
                type = "application/octet-stream";
            }
            long lastModified = directory ? 0L : Files.getLastModifiedTime(path).toMillis();
            String preview = directory ? null : resolvePreview(relativePath, path, type, size, lastModified);
            return new BookItem(path.getFileName().toString(), relativePath, parent, directory, size, type, preview, lastModified);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private BookItem toBookSafe(Path path) {
        try {
            return toBook(path);
        } catch (UncheckedIOException ex) {
            log.warn("Skipping {} during indexing: {}", path, ex.getMessage());
            return null;
        }
    }

    private String resolvePreview(String relativePath, Path path, String mimeType, long size, long lastModified) {
        if (!isPdf(mimeType, path)) {
            previewCache.remove(relativePath);
            deleteCacheVariants(relativePath);
            return null;
        }

        PreviewCacheEntry cached = previewCache.get(relativePath);
        if (cached != null && cached.size == size && cached.lastModified == lastModified) {
            return cached.svg;
        }

        String cachedSvg = loadCachedPreview(relativePath, size, lastModified);
        if (cachedSvg != null) {
            previewCache.put(relativePath, new PreviewCacheEntry(cachedSvg, size, lastModified));
            return cachedSvg;
        }

        try {
            String svg = renderPdfPreview(path);
            if (svg != null) {
                writeCachedPreview(relativePath, size, lastModified, svg);
                previewCache.put(relativePath, new PreviewCacheEntry(svg, size, lastModified));
            } else {
                previewCache.remove(relativePath);
                deleteCacheVariants(relativePath);
            }
            return svg;
        } catch (IOException ex) {
            log.warn("Failed to render PDF preview for {}: {}", path, ex.getMessage());
            previewCache.remove(relativePath);
            deleteCacheVariants(relativePath);
            return null;
        }
    }

    private boolean isPdf(String mimeType, Path path) {
        if (mimeType != null && mimeType.toLowerCase(Locale.ROOT).contains("pdf")) {
            return true;
        }
        String filename = path.getFileName().toString().toLowerCase(Locale.ROOT);
        return filename.endsWith(".pdf");
    }

    private String loadCachedPreview(String relativePath, long size, long lastModified) {
        CacheLocation location = cacheLocation(relativePath, size, lastModified);
        if (Files.exists(location.file())) {
            try {
                return Files.readString(location.file());
            } catch (IOException e) {
                log.warn("Failed to read cached preview {}: {}", location.file(), e.getMessage());
            }
        }
        return null;
    }

    private void writeCachedPreview(String relativePath, long size, long lastModified, String svg) throws IOException {
        CacheLocation location = cacheLocation(relativePath, size, lastModified);
        Files.createDirectories(location.dir());
        Path temp = Files.createTempFile(location.dir(), location.hash(), ".tmp");
        Files.writeString(temp, svg, StandardCharsets.UTF_8);
        Files.move(temp, location.file(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        deleteOtherVariants(location.hash(), location.filename());
    }

    private void deleteCacheVariants(String relativePath) {
        String hash = hashPath(relativePath);
        deleteOtherVariants(hash, null);
    }

    private void deleteOtherVariants(String hash, String keepFilename) {
        Path dir = cachePath.resolve(hash.substring(0, 2));
        if (!Files.exists(dir)) {
            return;
        }
        try (Stream<Path> files = Files.list(dir)) {
            files.filter(Files::isRegularFile)
                    .filter(file -> file.getFileName().toString().startsWith(hash + "_"))
                    .filter(file -> keepFilename == null || !file.getFileName().toString().equals(keepFilename))
                    .forEach(file -> {
                        try {
                            Files.deleteIfExists(file);
                        } catch (IOException e) {
                            log.warn("Failed removing cache entry {}: {}", file, e.getMessage());
                        }
                    });
        } catch (IOException e) {
            log.warn("Unable to enumerate cache variants in {}: {}", dir, e.getMessage());
        }

        try (Stream<Path> leftovers = Files.list(dir)) {
            if (!leftovers.findAny().isPresent()) {
                Files.deleteIfExists(dir);
            }
        } catch (IOException ignored) {
        }
    }

    private CacheLocation cacheLocation(String relativePath, long size, long lastModified) {
        String hash = hashPath(relativePath);
        String filename = hash + "_" + size + "_" + lastModified + CACHE_SUFFIX;
        Path dir = cachePath.resolve(hash.substring(0, 2));
        Path file = dir.resolve(filename);
        return new CacheLocation(hash, dir, file, filename);
    }

    private String hashPath(String relativePath) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(relativePath.getBytes(StandardCharsets.UTF_8));
            return HEX.formatHex(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    private String renderPdfPreview(Path pdfPath) throws IOException {
        try (PDDocument document = PDDocument.load(pdfPath.toFile())) {
            if (document.getNumberOfPages() == 0) {
                return null;
            }
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImageWithDPI(0, PREVIEW_DPI, ImageType.RGB);
            BufferedImage scaled = scaleImage(image, PREVIEW_MAX_WIDTH, PREVIEW_MAX_HEIGHT);
            return wrapInSvg(scaled);
        }
    }

    private BufferedImage scaleImage(BufferedImage source, int maxWidth, int maxHeight) {
        double widthScale = maxWidth / (double) source.getWidth();
        double heightScale = maxHeight / (double) source.getHeight();
        double scale = Math.min(1.0, Math.min(widthScale, heightScale));
        int targetWidth = Math.max(1, (int) Math.round(source.getWidth() * scale));
        int targetHeight = Math.max(1, (int) Math.round(source.getHeight() * scale));

        BufferedImage target = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = target.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.drawImage(source, 0, 0, targetWidth, targetHeight, null);
        } finally {
            graphics.dispose();
        }
        return target;
    }

    private String wrapInSvg(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", out);
        String base64 = Base64.getEncoder().encodeToString(out.toByteArray());
        int width = image.getWidth();
        int height = image.getHeight();
        return "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 " + width + " " + height +
                "\" preserveAspectRatio=\"xMidYMid meet\">" +
                "<rect width=\"100%\" height=\"100%\" rx=\"12\" fill=\"#18181b\"/>" +
                "<image width=\"" + width + "\" height=\"" + height + "\" href=\"data:image/png;base64," + base64 + "\"/>" +
                "</svg>";
    }

    private void trimPreviewCache(List<BookItem> items) {
        Set<String> validCompoundKeys = items.stream()
                .filter(item -> !item.directory && item.previewSvg != null)
                .map(item -> compoundKey(item.relativePath, item.size, item.lastModified))
                .collect(Collectors.toSet());

        previewCache.entrySet().removeIf(entry ->
                !validCompoundKeys.contains(compoundKey(entry.getKey(), entry.getValue().size, entry.getValue().lastModified)));

        Set<String> validFilenames = items.stream()
                .filter(item -> !item.directory && item.previewSvg != null)
                .map(item -> cacheLocation(item.relativePath, item.size, item.lastModified).filename())
                .collect(Collectors.toSet());
        cleanCacheDirectory(validFilenames);
    }

    private boolean matchesQuery(BookItem item, String normalized, Pattern pattern) {
        if (pattern != null) {
            return pattern.matcher(item.name).matches() || pattern.matcher(item.relativePath).matches();
        }
        String name = item.name.toLowerCase(Locale.ROOT);
        String path = item.relativePath.toLowerCase(Locale.ROOT);
        return name.contains(normalized) || path.contains(normalized);
    }

    private String globToRegex(String glob) {
        StringBuilder sb = new StringBuilder(glob.length() * 2);
        sb.append('^');
        for (char ch : glob.toCharArray()) {
            switch (ch) {
                case '*':
                    sb.append(".*");
                    break;
                case '?':
                    sb.append('.');
                    break;
                default:
                    if ("\\.[]{}()+-^$|".indexOf(ch) >= 0) {
                        sb.append('\\');
                    }
                    sb.append(ch);
            }
        }
        sb.append('$');
        return sb.toString();
    }

    private void cleanCacheDirectory(Set<String> validFilenames) {
        try (Stream<Path> dirs = Files.list(cachePath)) {
            dirs.forEach(path -> {
                if (Files.isDirectory(path)) {
                    try (Stream<Path> files = Files.list(path)) {
                        files.filter(Files::isRegularFile)
                                .filter(file -> file.getFileName().toString().endsWith(CACHE_SUFFIX))
                                .filter(file -> !validFilenames.contains(file.getFileName().toString()))
                                .forEach(file -> {
                                    try {
                                        Files.deleteIfExists(file);
                                    } catch (IOException e) {
                                        log.warn("Failed deleting stale preview {}: {}", file, e.getMessage());
                                    }
                                });
                    } catch (IOException e) {
                        log.warn("Failed scanning cache directory {}: {}", path, e.getMessage());
                    }
                    try (Stream<Path> remaining = Files.list(path)) {
                        if (!remaining.findAny().isPresent()) {
                            Files.deleteIfExists(path);
                        }
                    } catch (IOException ignored) {
                    }
                } else if (!validFilenames.contains(path.getFileName().toString())) {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        log.warn("Failed deleting stray cache file {}: {}", path, e.getMessage());
                    }
                }
            });
        } catch (IOException e) {
            log.warn("Unable to sweep preview cache root {}: {}", cachePath, e.getMessage());
        }
    }

    private String compoundKey(String relativePath, long size, long lastModified) {
        return relativePath + "#" + size + "#" + lastModified;
    }

    private BookDTO toDto(BookItem item) {
        return new BookDTO(item.name, item.relativePath, item.directory, item.size, item.type, item.previewSvg);
    }

    private record BookItem(
            String name,
            String relativePath,
            String parentPath,
            boolean directory,
            long size,
            String type,
            String previewSvg,
            long lastModified
    ) {}

    private record PreviewCacheEntry(String svg, long size, long lastModified) {}

    private record CacheLocation(String hash, Path dir, Path file, String filename) {}

    @PreDestroy
    void shutdownExecutor() {
        indexExecutor.shutdownNow();
    }
}
