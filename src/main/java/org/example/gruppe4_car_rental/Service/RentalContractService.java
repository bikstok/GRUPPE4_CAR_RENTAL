package org.example.gruppe4_car_rental.Service;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.RentalContract;
import org.example.gruppe4_car_rental.Repository.RentalContractRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RentalContractService {
    @Autowired
    private RentalContractRepo rentalContractRepo;

    public double getTotalPrice(String frame_number, LocalDate start_date, LocalDate end_date) {
        double monthly_sub_price = this.rentalContractRepo.getMonthlySubscriptionPriceFromFrameNumber(frame_number);
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

    public List<RentalContract> fetchAllRentalContracts(){
        return this.rentalContractRepo.fetchAllRentalContracts();
    }

    public List<Car> fetchAllAvailableCars() {
        return this.rentalContractRepo.fetchAllAvailableCars();
    }

    public boolean deleteRentalContract(int contract_id) {
        return this.rentalContractRepo.deleteRentalContract(contract_id);
    }
}
