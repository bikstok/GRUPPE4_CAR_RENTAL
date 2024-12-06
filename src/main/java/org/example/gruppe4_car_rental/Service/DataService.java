package org.example.gruppe4_car_rental.Service;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.CarPurchase;
import org.example.gruppe4_car_rental.Model.RentalContract;
import org.example.gruppe4_car_rental.Repository.DataRepo;
import org.example.gruppe4_car_rental.Repository.RentalContractRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DataService {
    @Autowired
    private RentalContractRepo rentalContractRepo;
    @Autowired
    private DataRepo dataRepo;

    public double getTotalPrice(String frame_number, LocalDate start_date, LocalDate end_date) {
        double monthly_sub_price = this.dataRepo.getMonthlySubscriptionPriceFromFrameNumber(frame_number);
        long monthsBetween = ChronoUnit.MONTHS.between(start_date, end_date);
        //rounds up incase its less than a month or whatever
        if (start_date.plusMonths(monthsBetween).isBefore(end_date)) {
            monthsBetween++;
        }
        double total_price = monthly_sub_price * monthsBetween;
        if (monthsBetween >= 12) {
            total_price *= 0.90;
        }
        return total_price;
    }

    public void createRentalContract(RentalContract rentalContract) {
        this.rentalContractRepo.createRentalContract(rentalContract);
    }

    public List<RentalContract> fetchAllRentalContracts() {
        return this.rentalContractRepo.fetchAllRentalContracts();
    }

    public List<Car> fetchAllAvailableCars() {
        return this.dataRepo.fetchAllAvailableCars();
    }

    public boolean deleteRentalContract(int contract_id) {
        return this.rentalContractRepo.deleteRentalContract(contract_id);
    }

    public boolean createCarPurchase(int contract_id) {
        RentalContract rentalContract = this.rentalContractRepo.getRentalContractFromContractId(contract_id);
        Car car = this.dataRepo.getCarFromFrameNumber(rentalContract.getFrame_number());
        if ("Solgt".equals(car.getCar_status())){
            return false;
        }
        double originalPrice = car.getOriginal_price();
        int kilometersDriven = car.getOdometer() - rentalContract.getStart_odometer();
        int excessKilometers = Math.max(kilometersDriven - rentalContract.getMax_km(), 0);
        int years = LocalDate.now().getYear() - car.getYear_produced();
        double purchase_price = (originalPrice * Math.pow(0.9, years)) + (0.75 * excessKilometers);
        this.dataRepo.createCarPurchase(new CarPurchase(-1, contract_id, Math.round(purchase_price * 100.0) / 100.0), rentalContract, car);
        return true;
    }

    public Car getCarFromFrameNumber(String frameNumber) {
        return this.dataRepo.getCarFromFrameNumber(frameNumber);
    }

    public RentalContract getRentalContractFromContractId(int contract_id) {
        return this.rentalContractRepo.getRentalContractFromContractId(contract_id);
    }
}
