package org.example.gruppe4_car_rental.Service;
import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.CarContractJoin;
import org.example.gruppe4_car_rental.Repository.BusinessRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class BusinessService {
    @Autowired
    private BusinessRepo businessRepo;

    public List<CarContractJoin> getReturnedCarsByEndDate(LocalDate endDate) {
        return this.businessRepo.findReturnedCarsByEndDate(endDate);
    }

    public List<Map<String, Object>> overviewOfCarPurchases() {
        return this.businessRepo.overviewOfCarPurchases();
    }

    public double calculateMonthlyEarningsJava() {
        return this.businessRepo.calculateMonthlyEarningsJava();
    }

    public List<Map<String, Object>> getTopRentedCarBrands() {
        return this.businessRepo.getTopRentedCarBrands();
    }

    public int countRentedCarsOverall() {
        return this.businessRepo.countRentedCarsOverall();
    }

    public int countRentedCars() {
        return this.businessRepo.countRentedCars();
    }
}
