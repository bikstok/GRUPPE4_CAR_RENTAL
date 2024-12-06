package org.example.gruppe4_car_rental.Controller;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.DamageReport;
import org.example.gruppe4_car_rental.Model.DamageType;
import org.example.gruppe4_car_rental.Model.RentalContract;
import org.example.gruppe4_car_rental.Repository.DamageRepo;
import org.example.gruppe4_car_rental.Service.DamageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DamageController {
    @Autowired
    private DamageService damageService;

    @GetMapping("skade_og_udbedring/damageFrontPage")
    public String fetchContractsWithPendingInspections(Model model) {

        this.damageService.changeStatusOfCarsToPendingInspection();
        List<Car> bruh = this.damageService.getCarsWithPendingInspectionsForXDays(2);
        System.out.println("cars that have pending for inspection for 2 days: " + bruh);



        List<RentalContract> rentalContracts = this.damageService.fetchContractsWithPendingInspections();
        model.addAttribute("rentalContracts", rentalContracts);
        return "skade_og_udbedring/damageFrontPage";
    }

    @GetMapping("/createDamageReport/{contract_id}")
    public String redirectToDamageReportForm(@PathVariable("contract_id") String contract_id, Model model) {
        model.addAttribute("contract_id", contract_id);
           List<DamageType> damageTypes = this.damageService.fetchAllDamageTypes();
           model.addAttribute("damageTypes", damageTypes);
           return "skade_og_udbedring/createDamageReport";
    }

    @PostMapping("/skade_og_udbedring/createDamageReport")
    public String createDamageReport(
            @RequestParam("contract_id") int contract_id,
            @RequestParam("damage_prices") List<Double> damage_prices,
            Model model) {

            double total_repair_price = 0.0;
            for (double damage_price : damage_prices) {
                total_repair_price += damage_price;
            }

        this.damageService.createDamageReport(new DamageReport(-1, contract_id, total_repair_price));

        return "redirect:/dataregistrering/cars";
    }

    @GetMapping("/skade_og_udbedring/skade")
    public String showDamagePage() {
        return "skade_og_udbedring/skade";
    }
}
