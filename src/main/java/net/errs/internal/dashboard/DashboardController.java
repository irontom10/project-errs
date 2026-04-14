package net.errs.internal.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @GetMapping("/internal")
    public String dashboard(Model model) {
        model.addAttribute("title", "Dashboard");
        model.addAttribute("header", "internal/fragments/header :: header");
        model.addAttribute("content", "internal/pages/dashboard/menu :: content");
        model.addAttribute("footer", "internal/fragments/footer :: footer");
        return "internal/layout/base";
    }
}
