package org.example.gruppe4_car_rental.Controller;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.CarPurchase;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
DataController er ansvarlig for at håndtere anmodninger fra dataregistrering i forlængelse med
kontrakt håndtering.
Controlleren henter data fra DataService (som kalder metoder fra DataRepo og sender det videre til HTML-visninger.)
 */

@Controller
public class DataController {
    @Autowired
    private DataService dataService;

    // Albert & Jakob
    //Opret lejekontrakt
    @PostMapping("/dataregistrering/createRentalContract")
    public String createRentalContract(@RequestParam("cpr_number") String cpr_number, @RequestParam("frame_number") String frame_number, @RequestParam("start_date") String start_date_string, @RequestParam("end_date") int months, @RequestParam(name = "insurance", defaultValue = "false") boolean insurance, @RequestParam("max_km") String max_km_string, @RequestParam("voucher") String voucher_string, Model model) {
        //en try-catch for at fejlfinde, uanset hvilken fejl siger den at man har tastet forkert CPR nummer
        try {
            //Sætter vouches til true hvis brugeren har udfyldt voucher feltet
            boolean voucher = voucher_string != null && !voucher_string.trim().isEmpty();
            LocalDate local_start_date = LocalDate.parse(start_date_string);

            // Validerer at startdatoen ikke er for langt ude i fremtiden
            if (ChronoUnit.WEEKS.between(LocalDate.now(), local_start_date) > 3) {
                String message = "Du kan ikke vælge en dato så langt frem i tiden";
                model.addAttribute("message", message);
                return "showError";
            }

            //Lægger months til slut dato
            LocalDate local_end_date = local_start_date.plusMonths(months);
            // Beregner totalpris og opretter kontrakt
            double total_price = this.dataService.getTotalPrice(frame_number, local_start_date, local_end_date);
            Date start_date = Date.valueOf(local_start_date);
            Date end_date = Date.valueOf(local_end_date);
            int max_km = Integer.parseInt(max_km_string);

            RentalContract rentalContract = new RentalContract(-1, cpr_number, frame_number, start_date, end_date, insurance, total_price, max_km, voucher, this.dataService.getCarFromFrameNumber(frame_number).getOdometer());
            this.dataService.createRentalContract(rentalContract);

            // Henter kundens data og viser faktura
            Customer customer = this.dataService.getCustomerFromCprNumber(cpr_number);
            return this.redirectToInvoice(model, customer.getFirst_name() + " " + customer.getLast_name(), rentalContract.getTotal_price(), this.dataService.getCarFromFrameNumber(frame_number));
        } catch (Throwable exception) {
            String message = "Du har indtastet et ugyldigt CPR nummer";
            model.addAttribute("message", message);
            return "showError";
        }
    }

    //Fører videre til formularen hvor man indtaster CPR
    @GetMapping("/createRentalContract/{frame_number}")
    public String redirectToRentalContractForm(@PathVariable("frame_number") String frame_number, Model model) {
        model.addAttribute("frame_number", frame_number);
        return "dataregistrering/createRentalContract";
    }

    @GetMapping("dataregistrering/showCarPurchases")
    public String fetchAllCarPurchases(Model model) {
        List<CarPurchase> carPurchases = this.dataService.fetchAllCarPurchases();
        model.addAttribute("carPurchases", carPurchases);
        return "dataregistrering/showCarPurchases";
    }

    @GetMapping("dataregistrering/showRentalContracts")
    public String fetchAllRentalContracts(Model model) {
        List<RentalContract> rentalContracts = this.dataService.fetchAllRentalContracts();
        model.addAttribute("rentalContracts", rentalContracts);
        return "dataregistrering/showRentalContracts";
    }

    //Kodet i fællesskab
    @GetMapping("dataregistrering/dataFrontPage")
    public String fetchAllAvailableCars(Model model) {
        List<Car> cars = this.dataService.fetchAllAvailableCars();
        model.addAttribute("cars", cars);
        return "dataregistrering/dataFrontPage";
    }

    public String redirectToInvoice(Model model, String customerName, double price, Car car) {
        Map<String, Object> invoiceInformation = new HashMap<>();
        invoiceInformation.put("date", LocalDate.now().toString());
        invoiceInformation.put("customerName", customerName);
        invoiceInformation.put("brand", car.getBrand());
        invoiceInformation.put("model", car.getModel());
        invoiceInformation.put("withoutMoms", price + " DKK");
        invoiceInformation.put("moms", (price * 0.25) + " DKK");
        invoiceInformation.put("totalPrice", (price * 1.25) + " DKK");
        final double totalPriceEuro = Math.floor((price * 1.25) / 7.46);
        invoiceInformation.put("totalPriceEuro", totalPriceEuro + " €");
        invoiceInformation.put("paid", false);
        model.addAttribute("invoice", invoiceInformation);
        return "showInvoice";
    }

    @GetMapping("dataregistrering/exampleError")
    public String exampleError(Model model) {
        String message = "Denne bil er allerede solgt, test123";
        model.addAttribute("message", message);
        return "showError";
    }

    @GetMapping("dataregistrering/exampleInvoice")
    public String exampleInvoice(Model model) {
        return this.redirectToInvoice(model, "name name2", 123, new Car("123", "Brand Name", "Model Name", "test3", "test4", "test5", 5, 6, 7, 8));
    }

    @GetMapping("/deleteCarPurchase/{car_purchase_id}")
    public String deleteCarPurchase(@PathVariable("car_purchase_id") int car_purchase_id) {
        this.dataService.deleteCarPurchase(car_purchase_id);
        return "redirect:/dataregistrering/showCarPurchases";
    }

    // Albert
    @GetMapping("/deleteRentalContract/{contract_id}")
    public String deleteRentalContract(@PathVariable("contract_id") int contract_id) {
        this.dataService.deleteRentalContract(contract_id);
        return "redirect:/dataregistrering/showRentalContracts";
    }

    @GetMapping("/createCarPurchase/{contract_id}")
    public String createCarPurchase(@PathVariable("contract_id") int contract_id, Model model) {
        // Henter lejekontrakt og biloplysninger baseret på kontrakt-ID
        RentalContract rentalContract = this.dataService.getRentalContractFromContractId(contract_id);
        Car car = this.dataService.getCarFromFrameNumber(rentalContract.getFrame_number());
        // Validerer om bilen allerede er solgt
        if ("Solgt".equals(car.getCar_status())) {
            String message = "Denne bil er allerede solgt";
            model.addAttribute("message", message);
            return "showError";
        }
        // Beregner købspris baseret på bilens alder og kørte kilometer
        double originalPrice = car.getOriginal_price();
        int kilometersDriven = car.getOdometer() - rentalContract.getStart_odometer();
        int excessKilometers = Math.max(kilometersDriven - rentalContract.getMax_km(), 0);
        int years = LocalDate.now().getYear() - car.getYear_produced();
        double purchase_price = (originalPrice * Math.pow(0.9, years)) + (0.75 * excessKilometers);
        // Opretter en ny CarPurchase-objekt med afrundet købspris
        CarPurchase carPurchase = new CarPurchase(-1, contract_id, Math.round(purchase_price * 100.0) / 100.0);
        // Opdaterer databasen med det nye bilkøb
        this.dataService.createCarPurchase(carPurchase, rentalContract, car);
        // Henter kundens oplysninger og redirecter til faktura
        Customer customer = this.dataService.getCustomerFromCprNumber(rentalContract.getCpr_number());
        return this.redirectToInvoice(model, customer.getFirst_name() + " " + customer.getLast_name(), carPurchase.getPurchase_price(), car);
    }

    //Henviser til redigeringsformular for en specifik lejekontrakt baseret på contract_id, hvor man indtaster nye værdier.
    //den tager kundens parametre med over i formularen så man ikke skal indtaste alle informationer.
    @GetMapping("/editRentalContract/{contract_id}")
    public String showEditForm(@PathVariable("contract_id") int contract_id, Model model) {
        RentalContract rentalContract = this.dataService.getRentalContractFromContractId(contract_id);
        model.addAttribute("rentalContract", rentalContract);
        return "dataregistrering/editRentalContract";
    }

    //Nunu
    //Håndterer redigeringsformularen og opdaterer i databasen.
    @PostMapping("/updateRentalContract")
    public String updateRentalContract(@RequestParam("contract_id") int contract_id, @RequestParam("cpr_number") String cpr_number, @RequestParam("frame_number") String frame_number, @RequestParam("start_date") String start_date_string, @RequestParam("end_date") String end_date_string, @RequestParam(name = "insurance", defaultValue = "false") boolean insurance, @RequestParam("total_price") double total_price, @RequestParam("max_km") int max_km, @RequestParam(name = "voucher", defaultValue = "false") boolean voucher, @RequestParam("start_odometer") double start_odometer) {

        //Opretter et ny rentalContract-objekt som er en opdateret version af det eksisterende.
        RentalContract rentalContract = new RentalContract(contract_id, cpr_number, frame_number, Date.valueOf(LocalDate.parse(start_date_string)), Date.valueOf(LocalDate.parse(end_date_string)), insurance, total_price, max_km, voucher, (int) Math.floor(start_odometer));
        this.dataService.updateRentalContract(rentalContract);
        return "redirect:/dataregistrering/showRentalContracts";
    }
}