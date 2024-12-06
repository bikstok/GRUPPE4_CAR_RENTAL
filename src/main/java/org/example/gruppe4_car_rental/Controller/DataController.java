package org.example.gruppe4_car_rental.Controller;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.Customer;
import org.example.gruppe4_car_rental.Model.RentalContract;
import org.example.gruppe4_car_rental.Service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
public class DataController {
    @Autowired
    private DataService dataService;

    //actually creates the rental contract and redirects you to view all rental contracts
    @PostMapping("/dataregistrering/createRentalContract")
    public String createRentalContract(
            @RequestParam("cpr_number") String cpr_number,
            @RequestParam("frame_number") String frame_number,
            @RequestParam("start_date") String start_date_string,
            @RequestParam("end_date") int months,
            @RequestParam(name = "insurance", defaultValue = "false") boolean insurance,
            @RequestParam("max_km") String max_km_string,
            @RequestParam("voucher") String voucher_string,
            Model model) {

        try {
            boolean voucher = voucher_string != null && !voucher_string.trim().isEmpty();
            LocalDate local_start_date = LocalDate.parse(start_date_string);
            LocalDate local_end_date = local_start_date.plusMonths(months);
            /*if (local_end_date.isBefore(local_start_date)) {//rental contract starts after it ends, ???
                String message = "Du kan ikke vælge en slut dato i fortiden.";
                model.addAttribute("message", message);
                return "dataregistrering/error";
            } else if (local_start_date.plusMonths(3).isAfter(local_end_date)) {
                String message = "Kontrakten skal være på mindst 3 måneder.";
                model.addAttribute("message", message);
                return "dataregistrering/error";
            } else */if (ChronoUnit.WEEKS.between(LocalDate.now(), local_start_date) > 3) {//rental contract is over 100 years in the future
                String message = "Du kan ikke vælge en dato så langt frem i tiden";
                model.addAttribute("message", message);
                return "dataregistrering/error";
            }
            double total_price = this.dataService.getTotalPrice(frame_number, local_start_date, local_end_date);
            Date start_date = Date.valueOf(local_start_date);
            Date end_date = Date.valueOf(local_end_date);
            int max_km = Integer.parseInt(max_km_string);
            this.dataService.createRentalContract(new RentalContract(-1, cpr_number, frame_number, start_date, end_date, insurance, total_price, max_km, voucher, this.dataService.getCarFromFrameNumber(frame_number).getOdometer()));
            return "redirect:/dataregistrering/showRentalContracts";
        } catch (Throwable exception) {//basically the cpr number doesn't exist for any customer in the database
            String message = "Du har indtastet et ugyldigt CPR nummer";
            model.addAttribute("message", message);
            //exception.printStackTrace();
            return "dataregistrering/error";
        }
    }

    //redirects you to the form that lets you enter cpr_number, etc
    @GetMapping("/createRentalContract/{frame_number}")
    public String redirectToRentalContractForm(@PathVariable("frame_number") String frame_number, Model model) {
        model.addAttribute("frame_number", frame_number);
        return "dataregistrering/createRentalContract";
    }

    @GetMapping("dataregistrering/showRentalContracts")
    public String fetchAllRentalContracts(Model model) {
        List<RentalContract> rentalContracts = this.dataService.fetchAllRentalContracts();
        model.addAttribute("rentalContracts", rentalContracts);
        return "dataregistrering/showRentalContracts";
    }

    @GetMapping("dataregistrering/dataFrontPage")
    public String fetchAllAvailableCars(Model model) {
        List<Car> cars = this.dataService.fetchAllAvailableCars();
        model.addAttribute("cars", cars);
        return "dataregistrering/dataFrontPage";
    }

    @GetMapping("/deleteRentalContract/{contract_id}")
    public String deleteRentalContract(@PathVariable("contract_id") int contract_id) {
        this.dataService.deleteRentalContract(contract_id);
        return "redirect:/dataregistrering/showRentalContracts";
    }

    @GetMapping("/createCarPurchase/{contract_id}")
    public String createCarPurchase(@PathVariable("contract_id") int contract_id, Model model) {
        if (!this.dataService.createCarPurchase(contract_id)) {
            String message = "Denne bil er allerede solgt";
            model.addAttribute("message", message);
            return "dataregistrering/error";
        }
        return "redirect:/dataregistrering/showRentalContracts";
    }

   //Henviser til redigeringsformular for en specifik lejekontrakt baseret på contract_id, hvor man indtaster nye værdier.
   //den tager kundens parametre med over i formularen så man ikke skal indtaste alle informationer.
   @GetMapping("/editRentalContract/{contract_id}")
   public String showEditForm(@PathVariable("contract_id") int contract_id, Model model) {
       RentalContract rentalContract = this.dataService.getRentalContractFromContractId(contract_id);
       model.addAttribute("rentalContract", rentalContract);
       return "dataregistrering/editRentalContract";  // Returner Thymeleaf-template for redigering
   }

    //Håndterer redigeringsformularen og opdaterer i databasen.
    @PostMapping("/updateRentalContract")
    public String updateRentalContract(
            @RequestParam("cpr_number") String cpr_number,
            @RequestParam("frame_number") String frame_number,
            @RequestParam("start_date") Date start_date,
            @RequestParam("end_date") Date end_date,
            @RequestParam("insurance") String insurance,
            @RequestParam("total_price") double total_price,
            @RequestParam("max_km") int max_km,
            @RequestParam("voucher") boolean voucher) {
        //System.out.println("update rentalContract method called");

        //Opretter et ny rentalContract-objekt som er en opdateret version af det eksisterende.
        //RentalContract rentalContract = new RentalContract(cpr_number, frame_number, start_date, end_date, insurance, total_price, max_km, voucher);
        //this.dataService.updateRentalContract(rentalContract); //Opdaterer lejekontrakt i databasen via dataService
        return "redirect:/dataregistrering/showRentalContracts";
    }
}