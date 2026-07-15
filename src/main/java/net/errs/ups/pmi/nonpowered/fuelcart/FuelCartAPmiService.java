package net.errs.ups.pmi.nonpowered.fuelcart;

import net.errs.ups.pmi.common.BasePmiService;
import net.errs.ups.pmi.nonpowered.fuelcart.dto.FuelCartAPmiRequest;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Queue;

@Service
public class FuelCartAPmiService extends BasePmiService<FuelCartAPmiRequest> {

    /**
     * Order matters.
     * This must match the visual order of the inspection rows on the PDF.
     */
    private static final List<String> FORM_ITEM_IDS = List.of(
            // left side
            "1", "38", "40", "41", "9", "37", "29", "31", "32", "33",
            "16", "35", "46", "65", "36", "51",

            // right side
            "58", "20", "53", "22", "24", "59", "27", "39", "30", "61",
            "62", "63", "66", "67", "60", "50", "52", "54", "56",

            // wrapup
            "71"
    );

    @Override
    protected void validateDocument(PDDocument document) throws IOException {
        if (document.getNumberOfPages() < 1) {
            throw new IOException("Expected a 1-page Fuel Cart PMI template.");
        }
    }

    @Override
    protected void renderDocument(PDDocument document, PDFont font, FuelCartAPmiRequest request) throws IOException {
        Queue<String> glyphQueue = buildGlyphQueue(request.getItemValues(), FORM_ITEM_IDS);
        PDPage page = document.getPage(0);
        drawPage(document, page, font, request, glyphQueue);
    }

    private void drawPage(PDDocument document,
                          PDPage page,
                          PDFont font,
                          FuelCartAPmiRequest request,
                          Queue<String> glyphQueue) throws IOException {

        try (PDPageContentStream cs = new PDPageContentStream(
                document, page, AppendMode.APPEND, true, true)) {

            String date = formatDate(request.getDateCompleted());

            /*
             * Top header fields
             * These are estimated from the uploaded PDF and will probably need a tiny nudge pass.
             */
            drawTextMm(cs, page, font, 8, 34, 29.0, safe(request.getLocation()));
            drawTextMm(cs, page, font, 8, 28, 36.5, date);
            drawTextMm(cs, page, font, 8, 86, 36.5, safe(request.getUnitNo()));

            /*
             * Inspection glyph columns.
             * The uploaded PDF is a single-page table with left and right inspection blocks. :contentReference[oaicite:3]{index=3}
             *
             * These X/Y values are starting points and should be very close.
             */
            double leftX = 17.0;
            double rightX = 111.5;
            double startY = 78.3;
            double rowGap = 5.55;

            drawGlyphsMm(cs, page, font, 12, 16, leftX, startY, rowGap, popMany(glyphQueue, 16));
            drawGlyphsMm(cs, page, font, 12, 19, rightX, startY, rowGap, popMany(glyphQueue, 19));

            // Wrapup row "71 Touch Up"
            drawGlyphsMm(cs, page, font, 12, 1, leftX, 159.0, rowGap, popMany(glyphQueue, 1));

            /*
             * Tire rows
             * RF / LF on left block, LR / RR on lower left block
             */
            double depthX = 48.5;
            double checkedX = 69.5;
            double correctedX = 90.5;

            double rfY = 124.3;
            double lfY = 129.9;
            double lrY = 135.5;
            double rrY = 141.1;

            drawTextMm(cs, page, font, 9, depthX, rfY, safe(request.getRfTireDepth()));
            drawTextMm(cs, page, font, 9, checkedX, rfY, safe(request.getRfTirePressureChecked()));
            drawTextMm(cs, page, font, 9, correctedX, rfY, safe(request.getRfTirePressureCorrected()));

            drawTextMm(cs, page, font, 9, depthX, lfY, safe(request.getLfTireDepth()));
            drawTextMm(cs, page, font, 9, checkedX, lfY, safe(request.getLfTirePressureChecked()));
            drawTextMm(cs, page, font, 9, correctedX, lfY, safe(request.getLfTirePressureCorrected()));

            drawTextMm(cs, page, font, 9, depthX, lrY, safe(request.getLrTireDepth()));
            drawTextMm(cs, page, font, 9, checkedX, lrY, safe(request.getLrTirePressureChecked()));
            drawTextMm(cs, page, font, 9, correctedX, lrY, safe(request.getLrTirePressureCorrected()));

            drawTextMm(cs, page, font, 9, depthX, rrY, safe(request.getRrTireDepth()));
            drawTextMm(cs, page, font, 9, checkedX, rrY, safe(request.getRrTirePressureChecked()));
            drawTextMm(cs, page, font, 9, correctedX, rrY, safe(request.getRrTirePressureCorrected()));

            /*
             * Wrapup date and optional signatures
             */
            drawTextMm(cs, page, font, 12, 66, 183.8, date);

            if (request.getTechnicianSignature() != null && !request.getTechnicianSignature().isBlank()) {
                byte[] signatureBytes = decodeBase64Png(request.getTechnicianSignature());
                PDImageXObject sig = PDImageXObject.createFromByteArray(document, signatureBytes, "tech-signature");
                drawImageMm(page, cs, sig, 132, 179.5, 45, 10);
            }

            if (request.getSupervisorSignature() != null && !request.getSupervisorSignature().isBlank()) {
                byte[] signatureBytes = decodeBase64Png(request.getSupervisorSignature());
                PDImageXObject sig = PDImageXObject.createFromByteArray(document, signatureBytes, "supervisor-signature");
                drawImageMm(page, cs, sig, 132, 189.5, 45, 10);
            }
        }
    }
}