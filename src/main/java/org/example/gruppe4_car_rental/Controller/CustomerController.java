package org.example.gruppe4_car_rental.Controller;

import org.example.gruppe4_car_rental.Model.Customer;
import org.example.gruppe4_car_rental.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/*
CustomerController håndterer HTTP-anmodninger vedrørende kunder i systemet.
Controlleren giver funktionalitet som visning, sletning, redigering og opdatering af kunder.
 */
@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    //Sletter en kunde ud fra cpr_number
    @GetMapping("/deleteCustomer/{cpr_number}")
    public String deleteCustomer(@PathVariable("cpr_number") String cpr_number) {
        this.customerService.deleteCustomer(cpr_number);
        return "redirect:/dataregistrering/showCustomers";
    }

    //Håndterer forespørgsler til visning af kunder med sorteringsmuligheder.
    @GetMapping("/dataregistrering/showCustomers")
    public String showAllCustomers(
            @RequestParam(value = "previousSortBy", required = false) String previousSortBy,
            @RequestParam(value = "sortBy", required = false) String sortBy,
                                   Model model) {

        //System.out.println("show all customers called with sortby " + sortBy);
        //System.out.println("existing attribute is " + previousSortBy);

        /*Vi tilføjer previousSortBy for at kunne holde fast i når en overskrift er blevet klikket på.
        Hvis brugeren klikker på samme kolonne igen, ændres sorteringsrækkefølgen til det omvendte.*/
        if (sortBy != null && sortBy.equals(previousSortBy)) {
            sortBy += " DESC";
        } else {
            //Denne linje gør at man kan få fat i previousSortBy næste gang.
            model.addAttribute("sortBy", sortBy);
        }
        //Henter liste af kunder fra service-laget, sorteret efter sortBy-parameteren
        List<Customer> customers = customerService.fetchAllCustomers(sortBy);
        model.addAttribute("customers", customers);
        return "dataregistrering/showCustomers";// Returnerer til showCustomers.html
    }

    //Henviser til redigeringsformular for en specifik kunde baseret på cpr_number, hvor man indtaster nye værdier.
    //den tager kundens parametre med over i formularen så man ikke skal indtaste alle informationer.
    @GetMapping("/editCustomer/{cpr_number}")
    public String showEditForm(@PathVariable("cpr_number") String cpr_number, Model model) {
        Customer customer = this.customerService.findByCprNumber(cpr_number);
        model.addAttribute("customer", customer);
        return "dataregistrering/editCustomer";
    }

    //Håndterer redigeringsformularen og opdaterer i databasen.
    @PostMapping("/updateCustomer")
    public String updateCustomer(
            @RequestParam("cpr_number") String cpr_number,
            @RequestParam("first_name") String first_name,
            @RequestParam("last_name") String last_name,
            @RequestParam("email") String email,
            @RequestParam("phone_number") String phone_number,
            @RequestParam("address") String address,
            @RequestParam("city") String city,
            @RequestParam("zip_code") String zip_code) {
        //System.out.println("update customer method called");

        //Opretter et ny Customer-objekt som er en opdateret version af det eksisterende.
        Customer customer = new Customer(cpr_number, first_name, last_name, email, phone_number, address, city, zip_code);

        this.customerService.updateCustomer(customer); //Opdaterer kunde i databasen via CustomerService
        return "redirect:/dataregistrering/showCustomers";
    }
}


