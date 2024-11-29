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

import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/deleteCustomer/{cpr_number}")
    public String deleteCustomer(@PathVariable("cpr_number") String cpr_number){
        this.customerService.deleteCustomer(cpr_number);
        return "redirect:/dataregistrering/customers";
    }
    @GetMapping("dataregistrering/customers")
    public String getAllCustomers(Model model) {
        List<Customer> customers = this.customerService.fetchAllCustomers(); // Get all cars
        model.addAttribute("customers", customers); // Add the cars to the model
        return "dataregistrering/customers"; // Return the view (Thymeleaf template)
    }
}




