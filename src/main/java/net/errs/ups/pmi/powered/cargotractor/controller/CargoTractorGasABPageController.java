package net.errs.ups.pmi.powered.cargotractor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CargoTractorGasABPageController {

    @GetMapping("/internal/pmi/cargo-tractor/page")
    public String cargoTractorPage(Model model) {
        model.addAttribute("header", "internal/fragments/header");
        model.addAttribute("footer", "internal/fragments/footer");
        model.addAttribute("content", "ups/pmi/cargo-tractor/cargo-tractor-gas-ab :: content");
        return "internal/layout/base";
    }
}