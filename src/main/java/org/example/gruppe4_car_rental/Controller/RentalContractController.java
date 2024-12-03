package org.example.gruppe4_car_rental.Controller;

import org.apache.tomcat.util.buf.StringUtils;
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

@Controller
public class RentalContractController {
    @Autowired
    private RentalContractService rentalContractService;

    //actually creates the rental contract and redirects you to view all rental contracts
    @PostMapping("/dataregistrering/viewRentalContracts")
    public String viewRentalContracts(
            @RequestParam("cpr_number") String cpr_number,
            @RequestParam("frame_number") String frame_number,
            @RequestParam("start_date") String start_date_string,
            @RequestParam("end_date") String end_date_string,
            @RequestParam("insurance") boolean insurance,
            @RequestParam("max_km") String max_km_string,
            @RequestParam("voucher") String voucher_string) {
        boolean voucher = voucher_string != null && !voucher_string.trim().isEmpty();
        LocalDate local_start_date = LocalDate.parse(start_date_string);
        LocalDate local_end_date = LocalDate.parse(end_date_string);
        double total_price = this.rentalContractService.getTotalPrice(frame_number, local_start_date, local_end_date);
        Date start_date = Date.valueOf(local_start_date);
        Date end_date = Date.valueOf(local_end_date);
        int max_km = Integer.parseInt(max_km_string);
        //this.rentalContractService.createRentalContract(new RentalContract(cpr_number, frame_number, start_date, end_date, insurance, total_price, max_km,voucher));
        return "redirect:/dataregistrering/showRentalContracts";
    }

    //sikrer at createRentalContract siden loader.
    @GetMapping("dataregistrering/createRentalContract")
    public String showCreateRentalContractPage(Model model) {
        return "dataregistrering/createRentalContract";
    }

    //redirects you to the form that lets you enter cpr_number, etc
    @GetMapping("/createRentalContract/{frame_number}")
    public String createContract(@PathVariable("frame_number") String frame_number, Model model) {
        model.addAttribute("frame_number", frame_number);
        return "dataregistrering/createRentalContract";
    }

}
