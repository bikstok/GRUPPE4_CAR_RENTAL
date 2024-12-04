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
import org.springframework.web.bind.annotation.RequestParam;

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
   // @GetMapping("dataregistrering/customers")
   // public String getAllCustomers(Model model) {
   //     List<Customer> customers = this.customerService.fetchAllCustomers(); // Get all cars
   //     model.addAttribute("customers", customers); // Add the cars to the model
   //     return "dataregistrering/customers"; // Return the view (Thymeleaf template)
   // }

    // Endpoint for at vise alle kunder, med mulighed for sortering via sortBy-parameter
    @GetMapping("/dataregistrering/customers")
    public String showAllCustomers(
            @RequestParam(value = "previousSortBy", required = false) String previousSortBy,
            @RequestParam(value = "sortBy", required = false) String sortBy,
                                   Model model) {

        System.out.println("show all customers called with sortby " + sortBy);
        System.out.println("existing attribute is " + previousSortBy);

       /*vi tilføjer previousSortBy for at kunne holde fast i når en overskrift er blevet klikket på.
        sortBy = ASC order
        previousSortBy = DESV */
         if (sortBy != null && sortBy.equals(previousSortBy)) {
             sortBy += " DESC";
        } else {
             //Denne linje gør at man kan få fat i previousSortBy næste gang.
             model.addAttribute("sortBy", sortBy);
         }
        // Henter kunder fra service-laget, sorteret efter sortBy-parameteren
        List<Customer> customers = customerService.fetchAllCustomers(sortBy);
        model.addAttribute("customers", customers);
        return "dataregistrering/customers";// Returnerer til customers.html
    }
}

