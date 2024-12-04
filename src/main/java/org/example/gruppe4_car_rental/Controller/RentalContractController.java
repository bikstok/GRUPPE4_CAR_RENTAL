package org.example.gruppe4_car_rental.Controller;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.RentalContract;
import org.example.gruppe4_car_rental.Service.RentalContractService;
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
public class RentalContractController {
    @Autowired
    private RentalContractService rentalContractService;

    //actually creates the rental contract and redirects you to view all rental contracts
    @PostMapping("/dataregistrering/createRentalContract")
    public String createRentalContract(
            @RequestParam("cpr_number") String cpr_number,
            @RequestParam("frame_number") String frame_number,
            @RequestParam("start_date") String start_date_string,
            @RequestParam("end_date") String end_date_string,
            @RequestParam(name = "insurance", defaultValue = "false") boolean insurance,
            @RequestParam("max_km") String max_km_string,
            @RequestParam("voucher") String voucher_string, Model model) {
        try {
            boolean voucher = voucher_string != null && !voucher_string.trim().isEmpty();
            LocalDate local_start_date = LocalDate.parse(start_date_string);
            LocalDate local_end_date = LocalDate.parse(end_date_string);
            if (local_end_date.isBefore(local_start_date)) {//rental contract starts after it ends, ???
                String message = "you fucked up";
                model.addAttribute("message", message);
                return "dataregistrering/error";
            } else if (ChronoUnit.YEARS.between(LocalDate.now(), local_start_date) > 100) {//rental contract is over 100 years in the future
                String message = "back to the future";
                model.addAttribute("message", message);
                return "dataregistrering/error";
            }
            double total_price = this.rentalContractService.getTotalPrice(frame_number, local_start_date, local_end_date);
            Date start_date = Date.valueOf(local_start_date);
            Date end_date = Date.valueOf(local_end_date);
            int max_km = Integer.parseInt(max_km_string);
            this.rentalContractService.createRentalContract(new RentalContract(-1, cpr_number, frame_number, start_date, end_date, insurance, total_price, max_km, voucher));
            return "redirect:/dataregistrering/showRentalContracts";
        }catch (Throwable exception){//basically the cpr number doesn't exist for any customer in the database
            String message = "Du har indtastet et ugyldigt CPR nummer";
            model.addAttribute("message", message);
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
        List<RentalContract> rentalContracts = this.rentalContractService.fetchAllRentalContracts();
        model.addAttribute("rentalContracts", rentalContracts);
        return "dataregistrering/showRentalContracts";
    }

    @GetMapping("dataregistrering/dataFrontPage")
    public String fetchAllAvailableCars(Model model) {
        List<Car> cars = this.rentalContractService.fetchAllAvailableCars();
        model.addAttribute("cars", cars);
        return "dataregistrering/dataFrontPage";
    }

    @GetMapping("/deleteRentalContract/{contract_id}")
    public String deleteRentalContract(@PathVariable("contract_id") int contract_id) {
        this.rentalContractService.deleteRentalContract(contract_id);
        return "redirect:/dataregistrering/showRentalContracts";
    }

}
