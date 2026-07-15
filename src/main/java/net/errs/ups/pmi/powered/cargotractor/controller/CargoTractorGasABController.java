package net.errs.ups.pmi.powered.cargotractor.controller;

import net.errs.ups.pmi.powered.cargotractor.dto.CargoTractorGasABRequest;
import net.errs.ups.pmi.powered.cargotractor.service.CargoTractorGasABService;
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
public class CargoTractorGasABController {

    private final CargoTractorGasABService pdfService;

    public CargoTractorGasABController(CargoTractorGasABService pdfService) {
        this.pdfService = pdfService;
    }

    private Path templatePdf = Path.of("/home/irontom10/Downloads/AB Cargo Tractor.pdf");
    private Path nerdFont = Path.of("/home/irontom10/git/project-errs/src/main/resources/static/fonts/jetbrains/JetBrainsMonoNerdFontMono-Regular.ttf");

    // ✅ GET → default test render
    @GetMapping(value = "/cargo-tractor", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> renderDefault() throws Exception {

        CargoTractorGasABRequest request = buildDefault();

        byte[] rendered = pdfService.render(request, templatePdf, nerdFont);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline()
                                .filename("pmiform-default.pdf")
                                .build()
                                .toString())
                .contentType(MediaType.APPLICATION_PDF)
                .body(rendered);
    }


    // ✅ POST → real data
    @PostMapping(value = "/cargo-tractor", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> renderCargoTractor(@RequestBody CargoTractorGasABRequest request) throws Exception {

        byte[] rendered = pdfService.render(request, templatePdf, nerdFont);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline()
                                .filename("pmiform.pdf")
                                .build()
                                .toString())
                .contentType(MediaType.APPLICATION_PDF)
                .body(rendered);
    }

    // 🔧 default test data
    private CargoTractorGasABRequest buildDefault() {
        CargoTractorGasABRequest req = new CargoTractorGasABRequest();

        req.setLocation("ABY");
        req.setDateCompleted(LocalDate.now().toString());
        req.setUnitNo("TEST-UNIT-001");
        req.setPmi("A");

        // example measurements
        req.setSteeringPlay("0.1");
        req.setRfTireDepth("12");
        req.setLfTireDepth("11");
        req.setRrTireDepth("10");
        req.setLrTireDepth("10");

        req.setRfTirePressureChecked("100");
        req.setLfTirePressureChecked("100");
        req.setRrTirePressureChecked("100");
        req.setLrTirePressureChecked("100");

        req.setRfTirePressureCorrected("100");
        req.setLfTirePressureCorrected("100");
        req.setRrTirePressureCorrected("100");
        req.setLrTirePressureCorrected("100");

        req.setQtsOil("5");

        // 🔑 important: fill inspection items
        Map<String, String> items = new HashMap<>();

        // default everything to OK ("2")
        for (int i = 100; i < 800; i++) {
            items.put(String.valueOf(i), "2");
        }

        // override a few for testing visibility
        items.put("101", "5"); // repair needed
        items.put("201", "3"); // adjusted
        items.put("501", "4"); // repaired

        req.setItemValues(items);

        return req;
    }
}