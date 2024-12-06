package org.example.gruppe4_car_rental.Controller;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CarController {
    @Autowired
    private CarService carService;

    //@GetMapping("dataregistrering/cars")
    //public String fetchAllCars(Model model) {
    //       List<Car> cars = this.carService.fetchAllCars(); // Få fat i alle biler
    //       model.addAttribute("cars", cars); // Tilføj biler til Model
    //       return "dataregistrering/cars"; // Returnerer view (Thymeleaf template)
    //}

    @GetMapping("/dataregistrering/cars")
    public String showAllCars(
            @RequestParam(value = "previousSortBy", required = false) String previousSortBy,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            Model model) {

        //System.out.println("show all cars called with sortby " + sortBy);
        //System.out.println("existing attribute is " + previousSortBy);

        if (sortBy != null && sortBy.equals(previousSortBy)) {
            sortBy += " DESC";
        } else {
            model.addAttribute("sortBy", sortBy);
        }
        //henter liste af sorterede biler
        List<Car> cars = this.carService.fetchAllCars(sortBy);
        model.addAttribute("cars", cars);
        return "dataregistrering/cars";
    }

    @GetMapping("/deleteCar/{frame_number}")
    public String deleteCar(@PathVariable("frame_number") String frame_number) {
        this.carService.deleteCar(frame_number);
        return "redirect:/dataregistrering/cars";
    }

    @GetMapping("/editCar/{frame_number}")
    public String showEditForm(@PathVariable("frame_number") String frame_number, Model model) {
        Car car = this.carService.findByFrameNumber(frame_number);  // Henter bil fra CarService
        model.addAttribute("car", car);
        return "dataregistrering/editCar";  // Returner Thymeleaf-template for redigering
    }

    // håndterer redigeringsformularen og opdaterer i databasen.
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
        //System.out.println("update car method called");

        Car car = new Car(frame_number, model, brand, car_status, fuel_type, gear_type, year_produced, monthly_sub_price, odometer, original_price);

        this.carService.updateCar(car);  // Opdater bil i databasen via CarService
        return "redirect:/dataregistrering/cars";  // Omidiger til biloversigt
    }
}

