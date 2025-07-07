package net.errs.ProjectErrs.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("/public/hello")
    public String hello(Model model) {
        model.addAttribute("myVar","Hello from Java Controller!");
        return "hello";
    }
}
