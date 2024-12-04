package org.example.gruppe4_car_rental.Controller;
import java.util.Map;
import java.util.List;
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

        //Henter hvor mange antal biler der står som ''lejet''
        int rentedCarsCount = this.businessRepo.countRentedCars();
        model.addAttribute("rentedCars", rentedCarsCount);  // Attributnavnet 'rentedCars' bruges til at vise data i HTML-filen

        //Henter de top 5 mest udlejede biler
        List<Map<String, Object>> topRentedCarBrands = businessRepo.getTopRentedCarBrands(); //List<Map<String, Object>> repræsenterer en liste af rækker fra en database, hvor hver række er gemt som et "nøgle-værdi"-par (Map).
        model.addAttribute("topRentedCarBrands", topRentedCarBrands);

        return "forretningsudvikler/KPIDashboard";
    }

}
