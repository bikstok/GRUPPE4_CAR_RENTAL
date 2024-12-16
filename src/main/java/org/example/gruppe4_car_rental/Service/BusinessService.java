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


    // Beregner procentdelen af ledige biler
    public double getPercentOfAvailableCars() {
        return this.dataRepo.fetchAllAvailableCars().size() / (double) this.carRepo.fetchAllCars(null).size();
    }

    // Finder returnerede biler baseret på slutdato
    public List<Car> getReturnedCarsByEndDate(LocalDate end_date) {
        return this.businessRepo.findReturnedCarsByEndDate(end_date);
    }

    // Henter aktive lejekontrakter
    public List<RentalContract> getActiveRentalOverview() {
        return this.rentalContractRepo.getActiveRentalContracts();
    }

    // Henter et overblik over købte biler
    public List<Map<String, Object>> overviewOfCarPurchases() {
        return this.businessRepo.overviewOfCarPurchases();
    }

    // Beregner månedlig indtjening
    public double calculateMonthlyEarningsJava() {
        return this.businessRepo.calculateMonthlyEarningsJava();
    }

    // Henter top-5 mest udlejede bilmærker
    public List<Map<String, Object>> getTopRentedCarBrands() {
        return this.businessRepo.getTopRentedCarBrands();
    }

    // Tæller det samlede antal udlejede biler
    public int countRentedCarsOverall() {
        return this.businessRepo.countRentedCarsOverall();
    }

    // Tæller det aktuelle antal biler, der er udlejet
    public int countRentedCars() {
        return this.businessRepo.countRentedCars();
    }

    // Beregner total indtjening
    public double getTotalEarnings() {
        List<CarPurchase> carPurchases = this.rentalContractRepo.fetchAllCarPurchases();
        List<RentalContract> rentalContracts = this.rentalContractRepo.fetchAllRentalContracts();
        double earnings = 0.0;

        // Beregner indtjening fra lejekontrakter
        for (RentalContract rentalContract : rentalContracts) {
            earnings += rentalContract.getTotal_price();
        }

        // Beregner indtjening fra bilkøb
        for (CarPurchase carPurchase : carPurchases) {
            earnings += carPurchase.getPurchase_price();
        }

        return earnings;
    }
}
