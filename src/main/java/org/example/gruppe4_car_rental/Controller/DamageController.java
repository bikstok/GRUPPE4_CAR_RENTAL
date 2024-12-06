package org.example.gruppe4_car_rental.Controller;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.DamageReport;
import org.example.gruppe4_car_rental.Model.DamageType;
import org.example.gruppe4_car_rental.Model.RentalContract;
import org.example.gruppe4_car_rental.Service.DamageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DamageController {
    @Autowired
    private DamageService damageService;

    @GetMapping("skade_og_udbedring/damageFrontPage")
    public String fetchContractsWithPendingInspections(Model model) {
        int days = 2;
        List<Car> carsWithPendingInspections = this.damageService.getCarsWithPendingInspectionsForXDays(days);
        if (!carsWithPendingInspections.isEmpty()) {
            String dage = days > 1 ? "dage" : "dag";
            int antalBiler = carsWithPendingInspections.size();
            String biler = antalBiler > 1 ? "biler" : "bil";
            String message = "Der er " + antalBiler + " " + biler + " der har overskrevet tidsgrænsen på " + days + " " + dage + " og mangler en skadereport: ";
            boolean first = true;
            for (Car car : carsWithPendingInspections) {
                if (!first) {
                    message += ", " + car.getFrame_number();
                } else {
                    message += car.getFrame_number();
                    first = false;
                }
            }
            model.addAttribute("notification_title", "Notifikation om statusændringer");
            model.addAttribute("notification", message);
        }
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
