package org.example.gruppe4_car_rental.Service;

import org.example.gruppe4_car_rental.Repository.RentalContractRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RentalContractService {
    @Autowired
    private RentalContractRepo rentalContractRepo;
}
