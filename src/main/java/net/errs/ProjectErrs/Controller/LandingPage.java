package net.errs.ProjectErrs.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/public")
public class LandingPage {
    @GetMapping("/landing_page")
    public String showLandingPage(HttpSession session, Model model) {
        // Check if user is logged in
        if (session.getAttribute("loggedin") != null) {
            return "redirect:/home"; // Redirect to home page if logged in
        }
        // Provide dynamic variables for Thymeleaf
        model.addAttribute("linkFavicon", "/favicon.ico");
        model.addAttribute("linkLogin", "/login");
        return "/public/landing_page";
    }
}
