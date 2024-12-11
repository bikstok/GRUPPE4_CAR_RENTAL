package org.example.gruppe4_car_rental.Controller;

import org.example.gruppe4_car_rental.Model.RentalContract;
import org.example.gruppe4_car_rental.Service.DamageService;
import org.example.gruppe4_car_rental.Service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private HomeService homeService;

    // Index forside/login siden
    @GetMapping("/")
    public String index() {
        this.homeService.changeStatusOfCarsToRented();//when they have an active rental contract
        this.homeService.changeStatusOfCarsToPendingInspection();//when the rental contract expires
        return "index";
    }

    // Håndtering af login-formularen
    @PostMapping("/login")
    public String handleLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              Model model) {

        // Variabel til at bestemme, hvilken side brugeren skal omdirigeres til
        String redirectUrl;
        String correctPasword;
        switch (username) {
            case "dataNerds":
                redirectUrl = "dataregistrering/dataFrontPage";
                correctPasword = "d14b79333f72d51553d643d5c47dd5dede15d85d6f64e417ce612f12c79b812b";
                break;
            case "damageNerds":
                redirectUrl = "skade_og_udbedring/skade";
                correctPasword = "16cc78d498a0a5d3d8b45813d0f0a24be06243c89c18b3c2d8353d5bedc6cf2e";
                break;
            case "businessNerds":
                redirectUrl = "forretningsudvikler/KPIDashboard";
                correctPasword = "93aabed5ffd1d9985138539f72969668570d32d7312a6c91a90489dfac15115c";
                break;
            default:
                correctPasword = null;
                redirectUrl = null;
                break;
        }

        if (correctPasword != null) {
            // Brug switch til at validere loginoplysninger
            // Sætter login i et hash som gemmer login
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                final StringBuilder hexString = new StringBuilder();
                for (byte b : hash) {
                    final String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) {
                        hexString.append('0');
                    }
                    hexString.append(hex);
                }
                String hashedPassword = hexString.toString();
                if (hashedPassword.equals(correctPasword)) {
                    // Omdiriger brugeren til den relevante profilside baseret på login
                    return "redirect:/" + redirectUrl;
                }
            } catch (Throwable ignored) {
            }
        }
        // Hvis login er korrekt, send brugeren til den relevante side, hvis forkert vis fejl
        model.addAttribute("error", "Forkert brugernavn eller adgangskode.");
        return "index"; // Send tilbage til login-siden med fejl
    }
}

/*Alternativt kunne vi lave
if (password.equals("WeLoveKPI")) {
    redirectUrl = "forretningsudvikler/testLogin";
} else {
    redirectUrl = "error";
}
*/
