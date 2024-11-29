package org.example.gruppe4_car_rental.Service;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Repository.CarRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CarService {

@Autowired
private CarRepo carRepo;
    public List<Car> fetchAllCars() { //Til visning af alle biler / hente biler
        List<Car> cars = this.carRepo.fetchAllCars();
        for (Car element : cars) {
            System.out.println(element);
        }
        return cars;
    }

    public boolean deleteCar(String frame_number) {
        return this.carRepo.deleteCar(frame_number);
    }
}
