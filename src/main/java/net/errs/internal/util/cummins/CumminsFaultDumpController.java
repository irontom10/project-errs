package net.errs.internal.util.cummins;

import jakarta.servlet.http.HttpServletRequest;
import net.errs.internal.util.cummins.dto.CumminsFaultDump;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/internal/util/cummins-faults")
public class CumminsFaultDumpController {
    private static final String PAGE = "internal/pages/system/util/cummins-faults";
    private static final String CONTENT = PAGE + " :: content";

    private final CumminsFaultDumpParser parser;

    public CumminsFaultDumpController(CumminsFaultDumpParser parser) {
        this.parser = parser;
    }

    @GetMapping
    public String page(Model model, HttpServletRequest request) {
        model.addAttribute("dump", null);
        model.addAttribute("error", null);
        return render(model, request);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String importDump(@RequestParam("file") MultipartFile file,
                             Model model,
                             HttpServletRequest request) {
        try {
            CumminsFaultDump dump = parser.parse(file);
            model.addAttribute("dump", dump);
            model.addAttribute("error", null);
        } catch (Exception e) {
            model.addAttribute("dump", null);
            model.addAttribute("error", e.getMessage() == null
                    ? e.getClass().getSimpleName()
                    : e.getMessage());
        }

        return render(model, request);
    }

    private String render(Model model, HttpServletRequest request) {
        if ("true".equalsIgnoreCase(request.getHeader("HX-Request"))) {
            return CONTENT;
        }

        model.addAttribute("title", "Cummins Fault Dump");
        model.addAttribute("header", "internal/fragments/header :: header");
        model.addAttribute("content", CONTENT);
        model.addAttribute("footer", "internal/fragments/footer :: footer");
        return "internal/layout/base";
    }
}
