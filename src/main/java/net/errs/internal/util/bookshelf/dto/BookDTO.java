package net.errs.internal.util.bookshelf.dto;

public class BookDTO {
    private String name;
    private String relativePath;
    private boolean directory;
    private long size;
    private String type;
    private String previewSvg;

    public BookDTO(String name, String relativePath, boolean directory, long size, String type, String previewSvg) {
        this.name = name;
        this.relativePath = relativePath;
        this.directory = directory;
        this.size = size;
        this.type = type;
        this.previewSvg = previewSvg;
    }

    public String getName() { return name; }
    public String getRelativePath() { return relativePath; }
    public boolean isDirectory() { return directory; }
    public long getSize() { return size; }
    public String getType() { return type; }
    public String getPreviewSvg() { return previewSvg; }
}
