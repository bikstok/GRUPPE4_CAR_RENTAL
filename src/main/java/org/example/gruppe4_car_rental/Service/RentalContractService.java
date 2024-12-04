package org.example.gruppe4_car_rental.Service;

import org.example.gruppe4_car_rental.Model.RentalContract;
import org.example.gruppe4_car_rental.Repository.RentalContractRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RentalContractService {
    @Autowired
    private RentalContractRepo rentalContractRepo;

    public double getTotalPrice(String frame_number, LocalDate start_date, LocalDate end_date) {

        double monthly_sub_price = this.rentalContractRepo.getMonthlySubscriptionPriceFromFrameNumber(frame_number);
        System.out.println("monthly sub price is " + monthly_sub_price + " and frame number was " + frame_number);
        //todo: calculate and return the total price


        return 0.0;
    }

    public void createRentalContract(RentalContract rentalContract) {
        this.rentalContractRepo.createRentalContract(rentalContract);

    }
}