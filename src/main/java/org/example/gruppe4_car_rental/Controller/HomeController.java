package org.example.gruppe4_car_rental.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    // Index forside/login siden
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Håndtering af login-formularen
    @PostMapping("/index")
    public String handleLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              Model model) {

        // Variabel til at bestemme, hvilken side brugeren skal omdirigeres til
        String redirectUrl;

        // Brug switch til at validere loginoplysninger
        switch (username) {
            case "dataNerds":
                //ternary operator fremfor en masse if/else statements
                //condition ? value_if_true : value_if_false;
                redirectUrl = password.equals("WeLoveData") ? "dataregistrering/dataFrontPage" : "error";
                break;
            case "damageNerds":
                redirectUrl = password.equals("WeLoveDamage") ? "skade_og_udbedring/skade" : "error";
                break;
            case "businessNerds":
                redirectUrl = password.equals("WeLoveKPI") ? "forretningsudvikler/testLogin" : "error";
                break;
            default:
                redirectUrl = "error"; // Hvis brugernavnet ikke findes
        }

        // Hvis login er korrekt, send brugeren til den relevante side, hvis forkert vis fejl
        if ("error".equals(redirectUrl)) {
            model.addAttribute("error", "Forkert brugernavn eller adgangskode.");
            return "index"; // Send tilbage til login-siden med fejl
        }

        // Omdiriger brugeren til den relevante profilside baseret på login
        return "redirect:/" + redirectUrl;
    }
}

/*Alternativt kunne vi lave
if (password.equals("WeLoveKPI")) {
    redirectUrl = "forretningsudvikler/testLogin";
} else {
    redirectUrl = "error";
}
*/
