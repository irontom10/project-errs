package net.errs.ups.pmi.nonpowered.fuelcart.controller;

import net.errs.ups.pmi.nonpowered.fuelcart.dto.FuelCartAPmiRequest;
import net.errs.ups.pmi.nonpowered.fuelcart.FuelCartAPmiService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/internal/pmi")
public class FuelCartAPmiController {

    private final FuelCartAPmiService pdfService;

    public FuelCartAPmiController(FuelCartAPmiService pdfService) {
        this.pdfService = pdfService;
    }

    private final Path templatePdf = Path.of("/home/irontom10/Downloads/APMI Fuel Cart.pdf");
    private final Path nerdFont = Path.of("/home/irontom10/git/project-errs/src/main/resources/static/fonts/jetbrains/JetBrainsMonoNerdFontMono-Regular.ttf");

    @GetMapping(value = "/fuel-cart", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> renderDefault() throws Exception {
        FuelCartAPmiRequest request = buildDefault();
        byte[] rendered = pdfService.render(request, templatePdf, nerdFont);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline()
                                .filename("fuel-cart-default.pdf")
                                .build()
                                .toString())
                .contentType(MediaType.APPLICATION_PDF)
                .body(rendered);
    }

    @PostMapping(value = "/fuel-cart", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> renderFuelCart(@RequestBody FuelCartAPmiRequest request) throws Exception {
        byte[] rendered = pdfService.render(request, templatePdf, nerdFont);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline()
                                .filename("fuel-cart.pdf")
                                .build()
                                .toString())
                .contentType(MediaType.APPLICATION_PDF)
                .body(rendered);
    }

    private FuelCartAPmiRequest buildDefault() {
        FuelCartAPmiRequest req = new FuelCartAPmiRequest();

        req.setLocation("ABY");
        req.setDateCompleted(LocalDate.now().toString());
        req.setUnitNo("FUEL-CART-001");

        req.setRfTireDepth("12");
        req.setLfTireDepth("12");
        req.setLrTireDepth("10");
        req.setRrTireDepth("10");

        req.setRfTirePressureChecked("100");
        req.setLfTirePressureChecked("100");
        req.setLrTirePressureChecked("100");
        req.setRrTirePressureChecked("100");

        req.setRfTirePressureCorrected("100");
        req.setLfTirePressureCorrected("100");
        req.setLrTirePressureCorrected("100");
        req.setRrTirePressureCorrected("100");

        Map<String, String> items = new HashMap<>();

        String[] ids = {
                "1", "38", "40", "41", "9", "37", "29", "31", "32", "33",
                "16", "35", "46", "65", "36", "51",
                "58", "20", "53", "22", "24", "59", "27", "39", "30", "61",
                "62", "63", "66", "67", "60", "50", "52", "54", "56", "71"
        };

        for (String id : ids) {
            items.put(id, "2");
        }

        // visibility tests
        items.put("38", "5");
        items.put("53", "3");
        items.put("71", "4");

        req.setItemValues(items);

        return req;
    }
}