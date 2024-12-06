package org.example.gruppe4_car_rental.Service;

import org.example.gruppe4_car_rental.Repository.DamageRepo;
import org.example.gruppe4_car_rental.Repository.HomeRepo;
import org.example.gruppe4_car_rental.Repository.RentalContractRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HomeService {
    @Autowired
    private RentalContractRepo rentalContractRepo;
    @Autowired
    private HomeRepo homeRepo;

    public void changeStatusOfCarsToRented() {
        this.homeRepo.changeStatusOfCarsToRented(this.rentalContractRepo.getActiveRentalContracts());
    }

    public void changeStatusOfCarsToPendingInspection() {
        this.homeRepo.changeStatusOfCarsToPendingInspection(this.rentalContractRepo.fetchAllRentalContracts());
    }
}
