package org.example.gruppe4_car_rental.Controller;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CarController {
    @Autowired
    private CarService carService;

    //Håndterer forespørgsler til visning af biler med sorteringsmuligheder.
    @GetMapping("/dataregistrering/showCars")
    public String showAllCars(
            @RequestParam(value = "previousSortBy", required = false) String previousSortBy,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            Model model) {
        /*Hvis brugeren klikker på samme kolonne igen, ændres sorteringsrækkefølgen til DESC (omvendt).
        Første klik på en kolonne sorterer stigende (ASC).
        Andet klik på samme kolonne sorterer faldende (DESC).*/
        if (sortBy != null && sortBy.equals(previousSortBy)) {
            sortBy += " DESC";
        } else {
            model.addAttribute("sortBy", sortBy);
        }
        //Henter listen af biler baseret på den valgte sortering.
        List<Car> cars = this.carService.fetchAllCars(sortBy);
        model.addAttribute("cars", cars);
        return "dataregistrering/showCars";
    }

    //Sletter en bil fra databasen baseret på frame_number.
    @GetMapping("/deleteCar/{frame_number}")
    public String deleteCar(@PathVariable("frame_number") String frame_number) {
        this.carService.deleteCar(frame_number);
        return "redirect:/dataregistrering/showCars";
    }

    //Henviser til redigeringsformular for en specifik bil baseret på frame_number, hvor man indtaster nye værdier.
    //den tager bilens parametre med over i formularen så man ikke skal indtaste alle informationer.
    @GetMapping("/editCar/{frame_number}")
    public String showEditForm(@PathVariable("frame_number") String frame_number, Model model) {
        Car car = this.carService.findByFrameNumber(frame_number);  //Henter bil fra CarService
        model.addAttribute("car", car); //Sender bilens data til Thymeleaf-formularen.
        return "dataregistrering/editCar";
    }

    //Håndterer redigeringsformularen og opdaterer i databasen.
    @PostMapping("/updateCar")
    public String updateCar(
            @RequestParam("frame_number") String frame_number,
            @RequestParam("model") String model,
            @RequestParam("brand") String brand,
            @RequestParam("car_status") String car_status,
            @RequestParam("fuel_type") String fuel_type,
            @RequestParam("gear_type") String gear_type,
            @RequestParam("year_produced") int year_produced,
            @RequestParam("monthly_sub_price") double monthly_sub_price,
            @RequestParam("odometer") int odometer,
            @RequestParam("original_price") double original_price) {

        //Opretter et ny Car-objekt som er en opdateret version af det eksisterende.
        Car car = new Car(frame_number, model, brand, car_status, fuel_type, gear_type, year_produced, monthly_sub_price, odometer, original_price);

        this.carService.updateCar(car);  // Opdaterer bil i databasen via CarService
        return "redirect:/dataregistrering/showCars";  // Omidigerer til biloversigt
    }
}

