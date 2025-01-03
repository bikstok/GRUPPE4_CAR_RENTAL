package org.example.gruppe4_car_rental.Controller;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.RentalContract;
import org.example.gruppe4_car_rental.Service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/*
BusinessController er ansvarlig for at håndtere anmodninger fra "forretningsudvikler"-relaterede sider.
Controlleren henter data fra BusinessService og sender det videre til HTML-visninger.
 */
@Controller
public class BusinessController {
    @Autowired
    private BusinessService businessService;

    //Simone
    @GetMapping("/forretningsudvikler/KPIDashboard")
    public String showKPIDashboard(Model model) {


        //Henter hvor mange antal biler der står som ''lejet''
        int rentedCarsCount = this.businessService.countRentedCars();
        model.addAttribute("rentedCars", rentedCarsCount);  // Attributnavnet 'rentedCars' bruges til at vise data i HTML-filen


        //Henter antal lejekontrakter i alt
        int rentedCarsCountOverall = this.businessService.countRentedCarsOverall();
        model.addAttribute("rentedCarsOverall", rentedCarsCountOverall); //Attributnavnet 'rentedCarsOverall' bruges til at vise data i HTML-filen

        //Henter de top 5 mest udlejede biler
        List<Map<String, Object>> topRentedCarBrands = this.businessService.getTopRentedCarBrands(); //List<Map<String, Object>> repræsenterer en liste af rækker fra en database, hvor hver række er gemt som et "nøgle-værdi"-par (Map).
        model.addAttribute("topRentedCarBrands", topRentedCarBrands);

        //Henter den månedlige indtjening udfra lejekontrakter
        double monthlyEarnings = Math.round(this.businessService.calculateMonthlyEarningsJava() * 100.0) / 100.0;
        model.addAttribute("monthlyEarnings", monthlyEarnings + " DKK");

        // Henter total indtjening og sender det til modellen
        model.addAttribute("totalEarnings", this.businessService.getTotalEarnings() + " DKK");

        //Simone og Albert
        // Beregner procentdelen af ledige biler og afrunder til nærmeste heltal
        int percentOfAvailableCars = (int) Math.round(this.businessService.getPercentOfAvailableCars() * 100);
        model.addAttribute("notification", "Der er " + percentOfAvailableCars + "% ledige biler");
        if (percentOfAvailableCars >= 75) {
            model.addAttribute("color", "green");
        } else if (percentOfAvailableCars <= 25) {
            model.addAttribute("color", "red");
        } else {
            model.addAttribute("color", "black");
        }

        return "forretningsudvikler/KPIDashboard";
    }

    //Simone
    // Henter oversigt over bilkøb og sender det til modellen
    @GetMapping("/forretningsudvikler/showCarPurchaseOverview")
    public String carPurchaseOverview(Model model) {
        List<Map<String, Object>> carPurchaseSummary = this.businessService.overviewOfCarPurchases();
        model.addAttribute("carPurchaseSummary", carPurchaseSummary);
        return "forretningsudvikler/showCarPurchaseOverview";
    }

    //Simone
    // Henter aktive lejekontrakter og sender det til modellen
    @GetMapping("/forretningsudvikler/showActiveRentalContracts")
    public String getActiveRentalContracts(Model model) {
        List<RentalContract> getActiveRentalContracts = this.businessService.getActiveRentalOverview();
        model.addAttribute("rentalContracts", getActiveRentalContracts);
        return "forretningsudvikler/showActiveRentalContracts";
    }

    //Nunu
    // Konverterer end_date (String) til LocalDate og henter returnerede biler baseret på dato
    @GetMapping("/forretningsudvikler/showReturnedCars")
    public String getReturnedCarsByEndDate(@RequestParam("end_date") String end_date, Model model) {
        List<Car> returnedCars = this.businessService.getReturnedCarsByEndDate(LocalDate.parse(end_date));
        model.addAttribute("returnedCars", returnedCars);
        return "forretningsudvikler/showReturnedCars";
    }
}