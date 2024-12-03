package org.example.gruppe4_car_rental.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DamageController {
    @GetMapping("/skade_og_udbedring/skade")
    public String showDamagePage() {
        return "skade_og_udbedring/skade";
    }
}
