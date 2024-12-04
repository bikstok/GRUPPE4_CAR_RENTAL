package org.example.gruppe4_car_rental.Controller;

import org.example.gruppe4_car_rental.Repository.BusinessRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BusinessController {

    @Autowired
    private BusinessRepo businessRepo;

    @GetMapping("/forretningsudvikler/KPIDashboard")
    public String showKPIDashboard(Model model) {
        int rentedCarsCount = this.businessRepo.countRentedCars();  // Henter antallet af udlejede biler fra BusinessRepo
        model.addAttribute("rentedCars", rentedCarsCount);  // Attributnavnet 'rentedCars' bruges til at vise data i HTML-filen
        return "forretningsudvikler/KPIDashboard";
    }
}
