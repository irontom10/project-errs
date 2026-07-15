package net.errs.ups.pmi.common;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class BasePmiService<TRequest> {

    protected static final double MM_TO_PT = 72.5 / 25.4;

    protected static final Map<String, String> GLYPH_MAP = Map.of(
            "4", "", // Defective Item Repaired
            "5", "", // Repair Needed
            "2", "", // Item OK
            "1", "", // Not Applicable
            "3", ""  // Adjustment Made
    );

    public byte[] render(TRequest request, Path templatePdf, Path nerdFontPath) throws IOException {
        validatePaths(templatePdf, nerdFontPath);

        byte[] pdfBytes = Files.readAllBytes(templatePdf);

        try (PDDocument document = Loader.loadPDF(pdfBytes);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PDFont nerdFont = PDType0Font.load(document, Files.newInputStream(nerdFontPath), true);

            validateDocument(document);
            renderDocument(document, nerdFont, request);

            document.save(out);
            return out.toByteArray();
        }
    }

    protected void validatePaths(Path templatePdf, Path nerdFontPath) throws IOException {
        if (!Files.exists(templatePdf)) {
            throw new IOException("Template PDF not found: " + templatePdf);
        }
        if (!Files.exists(nerdFontPath)) {
            throw new IOException("Font not found: " + nerdFontPath);
        }
    }

    protected abstract void validateDocument(PDDocument document) throws IOException;

    protected abstract void renderDocument(PDDocument document, PDFont font, TRequest request) throws IOException;

    protected Queue<String> buildGlyphQueue(Map<String, String> itemValues, List<String> formItemIds) {
        Queue<String> queue = new ArrayDeque<>();
        Map<String, String> values = itemValues != null ? itemValues : Collections.emptyMap();

        for (String id : formItemIds) {
            String value = values.get(id);

            if (value == null && id.contains(".")) {
                String baseId = id.substring(0, id.indexOf('.'));
                value = values.get(baseId);
            }

            if (value == null) {
                value = "2";
            }

            queue.add(GLYPH_MAP.getOrDefault(value, ""));
        }

        return queue;
    }

    protected List<String> popMany(Queue<String> queue, int count) {
        List<String> values = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            values.add(queue.isEmpty() ? "" : queue.poll());
        }
        return values;
    }

    protected void drawGlyphsMm(PDPageContentStream cs,
                                PDPage page,
                                PDFont font,
                                float fontSize,
                                int repetitions,
                                double startXmm,
                                double startYmm,
                                double incrementYmm,
                                List<String> glyphs) throws IOException {
        for (int i = 0; i < repetitions; i++) {
            String glyph = glyphs.get(i % glyphs.size());
            drawTextMm(cs, page, font, fontSize, startXmm, startYmm + (incrementYmm * i), glyph);
        }
    }

    protected void drawTextMm(PDPageContentStream cs,
                              PDPage page,
                              PDFont font,
                              float fontSize,
                              double xmm,
                              double ymmFromTop,
                              String text) throws IOException {
        if (text == null || text.isBlank()) {
            return;
        }

        float x = mmToPt(xmm);
        float y = topMmToPdfPt(page, ymmFromTop);

        cs.beginText();
        cs.setFont(font, fontSize);
        cs.newLineAtOffset(x, y);
        cs.showText(text);
        cs.endText();
    }

    protected void drawImageMm(PDPage page,
                               PDPageContentStream cs,
                               PDImageXObject image,
                               double xmm,
                               double ymmFromTop,
                               double wmm,
                               double hmm) throws IOException {
        float x = mmToPt(xmm);
        float h = mmToPt(hmm);
        float yTop = topMmToPdfPt(page, ymmFromTop);
        float y = yTop - h;
        float w = mmToPt(wmm);

        cs.drawImage(image, x, y, w, h);
    }

    protected float mmToPt(double mm) {
        return (float) (mm * MM_TO_PT);
    }

    protected float topMmToPdfPt(PDPage page, double mmFromTop) {
        float pageHeight = page.getMediaBox().getHeight();
        return pageHeight - mmToPt(mmFromTop);
    }

    protected String safe(String value) {
        return value == null ? "" : value;
    }

    protected String formatDate(String raw) {
        if (raw == null || raw.isBlank()) {
            return LocalDate.now().format(DateTimeFormatter.ofPattern("MM   dd   yyyy"));
        }

        try {
            LocalDate parsed = LocalDate.parse(raw);
            return parsed.format(DateTimeFormatter.ofPattern("MM   dd   yyyy"));
        } catch (Exception ignored) {
            return raw;
        }
    }

    protected byte[] decodeBase64Png(String signature) {
        String base64 = signature;
        String prefix = "data:image/png;base64,";
        if (base64.startsWith(prefix)) {
            base64 = base64.substring(prefix.length());
        }
        return Base64.getDecoder().decode(base64);
    }
}