package net.errs.internal.system.status;

import net.errs.internal.system.status.dto.SystemStatusDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SystemStatusController {

    private final SystemStatusService systemStatusService;

    public SystemStatusController(SystemStatusService systemStatusService) {
        this.systemStatusService = systemStatusService;
    }

    @GetMapping("/internal/system/status")
    public String statusPage(Model model) {
        model.addAttribute("header", "internal/fragments/header :: header");
        model.addAttribute("content", "internal/pages/system/status :: content");
        model.addAttribute("footer", "internal/fragments/footer :: footer");
        model.addAttribute("status", systemStatusService.getStatus());
        return "internal/layout/base";
    }

    @GetMapping("/internal/api/system/status")
    @ResponseBody
    public SystemStatusDto statusJson() {
        return systemStatusService.getStatus();
    }
}