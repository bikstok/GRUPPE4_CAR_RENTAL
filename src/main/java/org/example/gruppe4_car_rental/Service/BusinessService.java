package org.example.gruppe4_car_rental.Service;
import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.CarPurchase;
import org.example.gruppe4_car_rental.Model.RentalContract;
import org.example.gruppe4_car_rental.Repository.BusinessRepo;
import org.example.gruppe4_car_rental.Repository.CarRepo;
import org.example.gruppe4_car_rental.Repository.DataRepo;
import org.example.gruppe4_car_rental.Repository.RentalContractRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class BusinessService {
    @Autowired
    private CarRepo carRepo;
    @Autowired
    private DataRepo dataRepo;
    @Autowired
    private BusinessRepo businessRepo;
    @Autowired
    private RentalContractRepo rentalContractRepo;

    public double getPercentOfAvailableCars() {
        return this.dataRepo.fetchAllAvailableCars().size() / (double) this.carRepo.fetchAllCars(null).size();
    }

    public List<Car> getReturnedCarsByEndDate(LocalDate endDate) {
        return businessRepo.findReturnedCarsByEndDate(endDate);
    }
    public List <RentalContract> getActiveRentalOverview() {
        return this.rentalContractRepo.getActiveRentalContracts();
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

    public double getTotalEarnings() {
        List<CarPurchase> carPurchases = this.rentalContractRepo.fetchAllCarPurchases();
        List<RentalContract> rentalContracts = this.rentalContractRepo.fetchAllRentalContracts();
        double earnings = 0.0;
        for (RentalContract rentalContract : rentalContracts
        ) {
            earnings += rentalContract.getTotal_price();
        }
        for (CarPurchase carPurchase : carPurchases) {
            earnings += carPurchase.getPurchase_price();
        }

        return earnings;
    }

}
    //public List<RentalContract> getActiveRentalContracts
