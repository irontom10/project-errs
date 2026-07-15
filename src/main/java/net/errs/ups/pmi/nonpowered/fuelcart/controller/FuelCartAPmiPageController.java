package net.errs.ups.pmi.nonpowered.fuelcart.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FuelCartAPmiPageController {

    @GetMapping("/internal/pmi/fuel-cart/page")
    public String fuelCartPage(Model model) {
        model.addAttribute("header", "internal/fragments/header");
        model.addAttribute("footer", "internal/fragments/footer");
        model.addAttribute("content", "ups/pmi/nonpowered/fuelcart/fuel-cart-a :: content");
        return "internal/layout/base";
    }
}