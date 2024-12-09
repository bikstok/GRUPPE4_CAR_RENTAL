package org.example.gruppe4_car_rental.Controller;
import java.sql.Date;
import java.util.Map;
import java.util.List;
import org.example.gruppe4_car_rental.Repository.BusinessRepo;
import org.example.gruppe4_car_rental.Service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.example.gruppe4_car_rental.Model.Car;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;

@Controller
public class BusinessController {
    @Autowired
    private BusinessService businessService;

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
        double monthlyEarnings = this.businessService.calculateMonthlyEarningsJava();
        model.addAttribute("monthlyEarnings", monthlyEarnings + " DKK");

        return "forretningsudvikler/KPIDashboard";
    }

    @GetMapping("/forretningsudvikler/BilopkobOversigt")
    public String carPurchaseOverview(Model model) {
        List<Map<String, Object>> carPurchaseSummary = this.businessService.overviewOfCarPurchases();
        model.addAttribute("carPurchaseSummary", carPurchaseSummary);
        return "forretningsudvikler/BilopkobOversigt";
    }

    @GetMapping("/forretningsudvikler/returnedCars")
    public String getReturnedCarsByEndDate(@RequestParam LocalDate endDate, Model model) {
        System.out.println("Controller: Received end_date = " + endDate);
        List<Car> returnedCars = this.businessService.getReturnedCarsByEndDate(endDate);
        System.out.println("Controller: Returned cars size = " + returnedCars.size());
        model.addAttribute("returnedCars", returnedCars);
        return "forretningsudvikler/returnedCars";
    }
}

/*this.carService.deleteCar(frame_number);
        return "redirect:/dataregistrering/cars";*/


