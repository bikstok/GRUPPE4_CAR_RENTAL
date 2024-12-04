package org.example.gruppe4_car_rental.Controller;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CarController {
    @Autowired
    private CarService carService;

    @GetMapping("/deleteCar/{frame_number}")
    public String deleteCar(@PathVariable("frame_number") String frame_number) {
        this.carService.deleteCar(frame_number);
        return "redirect:/dataregistrering/cars";
    }

    @GetMapping("dataregistrering/cars")
    public String fetchAllCars(Model model) {
        List<Car> cars = this.carService.fetchAllCars(); // Få fat i alle biler
        model.addAttribute("cars", cars); // Tilføj biler til Model
        return "dataregistrering/cars"; // Returnerer view (Thymeleaf template)
    }
}



