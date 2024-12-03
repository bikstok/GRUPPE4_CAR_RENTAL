package org.example.gruppe4_car_rental.Controller;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.Customer;
import org.example.gruppe4_car_rental.Service.CarService;
import org.example.gruppe4_car_rental.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BusinessController {

    /*@Autowired
    private RentalContractService rentalContractService;

    @GetMapping("/kpiDashboard")
    public String showKPIDashboard(Model model) {
        int rentedCarsCount = rentalContractService;.countRentedCars();
        model.addAttribute("rentedCars", rentedCarsCount);
        return "kpiDashboard";
    }*/


    //ændres til KPIDashboard når den er færdiglavet.
    @GetMapping("/forretningsudvikler/testLogin")
    public String showBusinessPage() {
        return "forretningsudvikler/testLogin";
    }
}
