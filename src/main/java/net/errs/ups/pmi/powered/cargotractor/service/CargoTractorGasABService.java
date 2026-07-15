package net.errs.ups.pmi.powered.cargotractor.service;

import net.errs.ups.pmi.powered.cargotractor.dto.CargoTractorGasABRequest;
import net.errs.ups.pmi.common.BasePmiService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

@Service
public class CargoTractorGasABService extends BasePmiService<CargoTractorGasABRequest> {

    private static final List<String> FORM_ITEM_IDS = List.of(
            "101", "102", "103", "104", "105", "106",
            "107", "108", "109", "110", "110.1",
            "201", "201.1", "202", "203", "204", "205", "206", "207", "208",
            "209", "210", "211", "212", "213",
            "301", "302", "302.1", "302.2", "302.3", "302.4",
            "303", "304", "305", "306", "306.1", "306.2", "306.3", "306.4",
            "501", "502", "502.1", "503", "503.1", "504", "505", "506", "507", "508",
            "509", "509.1", "510", "511", "512", "513", "514",
            "601", "601.1", "602", "603", "604", "605", "606", "607", "608",
            "609", "610", "611", "612", "613", "613.1",
            "614", "614.1", "614.2", "614.3", "614.4", "614.5", "614.6", "614.7",
            "616", "616.1", "617", "617.1", "617.2", "618", "618.1",
            "701", "702"
    );

    @Override
    protected void validateDocument(PDDocument document) throws IOException {
        if (document.getNumberOfPages() < 2) {
            throw new IOException("Expected a 2-page PMI template.");
        }
    }

    @Override
    protected void renderDocument(PDDocument document, PDFont font, CargoTractorGasABRequest request) throws IOException {

        Map<String, String> itemValues = new HashMap<>(request.getItemValues());

        if ("A".equalsIgnoreCase(request.getPmi())) {
            List<String> baseIds = List.of("303", "304", "612", "614", "618");

            for (String formId : FORM_ITEM_IDS) {
                for (String base : baseIds) {
                    if (formId.equals(base) || formId.startsWith(base + ".")) {
                        itemValues.put(formId, "1");
                    }
                }
            }
        }

        Queue<String> glyphQueue = buildGlyphQueue(itemValues, FORM_ITEM_IDS);

        PDPage page1 = document.getPage(0);
        PDPage page2 = document.getPage(1);

        drawPage1(document, page1, font, request, glyphQueue);
        drawPage2(document, page2, font, request, glyphQueue);
    }

    private void drawPage1(PDDocument document,
                           PDPage page,
                           PDFont font,
                           CargoTractorGasABRequest request,
                           Queue<String> glyphQueue) throws IOException {

        try (PDPageContentStream cs = new PDPageContentStream(
                document, page, AppendMode.APPEND, true, true)) {

            String date = formatDate(request.getDateCompleted());

            drawTextMm(cs, page, font, 8, 38, 29, safe(request.getLocation()));
            drawTextMm(cs, page, font, 8, 18, 32.5, date);
            drawTextMm(cs, page, font, 8, 62, 32.5, safe(request.getUnitNo()));

            double column1 = 15;
            double column2 = 118;
            double row1 = 64.5;
            double row2 = 104;
            double row3 = 151;
            double row4 = 190;
            double offset = 3.8;

            drawGlyphsMm(cs, page, font, 12, 6, column1, row1, offset, popMany(glyphQueue, 6));
            drawGlyphsMm(cs, page, font, 12, 5, column2, row1, offset, popMany(glyphQueue, 5));
            drawGlyphsMm(cs, page, font, 12, 9, column1, row2, offset, popMany(glyphQueue, 9));
            drawGlyphsMm(cs, page, font, 12, 5, column2, row2, offset, popMany(glyphQueue, 5));
            drawGlyphsMm(cs, page, font, 12, 6, column1, row3, offset, popMany(glyphQueue, 6));
            drawGlyphsMm(cs, page, font, 12, 8, column2, row3, offset, popMany(glyphQueue, 8));
            drawGlyphsMm(cs, page, font, 12, 10, column1, row4, offset, popMany(glyphQueue, 10));
            drawGlyphsMm(cs, page, font, 12, 7, column2, row4, offset, popMany(glyphQueue, 7));

            if ("A".equalsIgnoreCase(request.getPmi())) {
                drawTextMm(cs, page, font, 36, 155.3, 18, "");
            } else if ("B".equalsIgnoreCase(request.getPmi())) {
                drawTextMm(cs, page, font, 36, 165.9, 18, "");
            }

            double c1 = 30;
            double c2 = 50;
            double c3 = 70;
            double c4 = 135;
            double c5 = 155;
            double c6 = 175;
            double r1 = 162;
            double r2 = 166;
            double r3 = 169;
            double r4 = 173;

            drawTextMm(cs, page, font, 10, 145, 76, safe(request.getSteeringPlay()));

            drawTextMm(cs, page, font, 10, c1, r1, safe(request.getRfTireDepth()));
            drawTextMm(cs, page, font, 10, c1, r2, safe(request.getLfTireDepth()));
            drawTextMm(cs, page, font, 10, c4, r3, safe(request.getRrTireDepth()));
            drawTextMm(cs, page, font, 10, c4, r4, safe(request.getLrTireDepth()));

            drawTextMm(cs, page, font, 10, c2, r1, safe(request.getRfTirePressureChecked()));
            drawTextMm(cs, page, font, 10, c2, r2, safe(request.getLfTirePressureChecked()));
            drawTextMm(cs, page, font, 10, c5, r3, safe(request.getRrTirePressureChecked()));
            drawTextMm(cs, page, font, 10, c5, r4, safe(request.getLrTirePressureChecked()));

            drawTextMm(cs, page, font, 10, c3, r1, safe(request.getRfTirePressureCorrected()));
            drawTextMm(cs, page, font, 10, c3, r2, safe(request.getLfTirePressureCorrected()));
            drawTextMm(cs, page, font, 10, c6, r3, safe(request.getRrTirePressureCorrected()));
            drawTextMm(cs, page, font, 10, c6, r4, safe(request.getLrTirePressureCorrected()));

            drawTextMm(cs, page, font, 10, 145, 208.5, safe(request.getQtsOil()));
        }
    }

    private void drawPage2(PDDocument document,
                           PDPage page,
                           PDFont font,
                           CargoTractorGasABRequest request,
                           Queue<String> glyphQueue) throws IOException {

        try (PDPageContentStream cs = new PDPageContentStream(
                document, page, AppendMode.APPEND, true, true)) {

            if ("A".equalsIgnoreCase(request.getPmi())) {
                drawTextMm(cs, page, font, 36, 53.6, 20, "");
            } else if ("B".equalsIgnoreCase(request.getPmi())) {
                drawTextMm(cs, page, font, 36, 64.5, 20, "");
            }

            double column1 = 14.5;
            double column2 = 117.5;
            double row1 = 55.5;
            double row2 = 140.5;
            double offset = 3.82;

            drawGlyphsMm(cs, page, font, 12, 13, column1, row1, offset, popMany(glyphQueue, 13));
            drawGlyphsMm(cs, page, font, 12, 17, column2, row1, offset, popMany(glyphQueue, 17));
            drawGlyphsMm(cs, page, font, 12, 1, column1, row2, offset, popMany(glyphQueue, 1));
            drawGlyphsMm(cs, page, font, 12, 1, column2, row2, offset, popMany(glyphQueue, 1));

            drawTextMm(cs, page, font, 8, 70, 55, safe(request.getCoolantCondition()));
            drawTextMm(cs, page, font, 8, 65, 58.85, safe(request.getCoolantProtection()));
            drawTextMm(cs, page, font, 8, 150, 58.85, safe(request.getBatteryLoadTestVoltage()));

            if ("B".equalsIgnoreCase(request.getPmi())) {
                drawTextMm(cs, page, font, 8, 160, 66.55, safe(request.getBattCableDropPos()));
                drawTextMm(cs, page, font, 8, 160, 70.4, safe(request.getBattCableDropNeg()));
                drawTextMm(cs, page, font, 8, 175, 74.25, safe(request.getBattIgnSwDrop()));

                drawTextMm(cs, page, font, 8, 145, 81.95, safe(request.getInitTiming()));
                drawTextMm(cs, page, font, 8, 155, 85.8, safe(request.getMechanicalAdvTiming()));
                drawTextMm(cs, page, font, 8, 145, 89.65, safe(request.getTotalAdvTiming()));
            }

            drawTextMm(cs, page, font, 8, 135, 97.35, safe(request.getAltFullOutputVolts()));
            drawTextMm(cs, page, font, 8, 160, 97.35, safe(request.getAltFullOutputAmps()));

            if ("B".equalsIgnoreCase(request.getPmi())) {
                drawTextMm(cs, page, font, 8, 155, 116, safe(request.getThermostatOpening()));
            }

            String date = formatDate(request.getDateCompleted());
            drawTextMm(cs, page, font, 12, 39, 158, date);
            drawTextMm(cs, page, font, 12, 55, 170, safe(request.getGaugeHours()));

            if (request.getSignature() != null && !request.getSignature().isBlank()) {
                byte[] signatureBytes = decodeBase64Png(request.getSignature());
                PDImageXObject sig = PDImageXObject.createFromByteArray(document, signatureBytes, "signature");
                drawImageMm(page, cs, sig, 160, 152, 25, 8);
            }
        }
    }
}