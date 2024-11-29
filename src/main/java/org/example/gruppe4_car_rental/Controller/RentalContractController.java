package org.example.gruppe4_car_rental.Controller;

import org.example.gruppe4_car_rental.Service.RentalContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RentalContractController {
    @Autowired
    private RentalContractService rentalContractService;


    @GetMapping("/dataregistrering/createRentalContract")
    public String createContract(@RequestParam("frame_number") String frame_number, Model model) {
        model.addAttribute("frame_number", frame_number);
        return "redirect:/dataregistrering/customers";
    }
}
