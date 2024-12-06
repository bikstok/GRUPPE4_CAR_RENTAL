package org.example.gruppe4_car_rental.Service;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.DamageReport;
import org.example.gruppe4_car_rental.Model.DamageType;
import org.example.gruppe4_car_rental.Model.RentalContract;
import org.example.gruppe4_car_rental.Repository.CustomerRepo;
import org.example.gruppe4_car_rental.Repository.DamageRepo;
import org.example.gruppe4_car_rental.Repository.RentalContractRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DamageService {
    @Autowired
    private DamageRepo damageRepo;

    public List<RentalContract> fetchContractsWithPendingInspections() {
        return this.damageRepo.fetchContractsWithPendingInspections();
    }

    public List<DamageType> fetchAllDamageTypes() {
        return this.damageRepo.fetchAllDamageTypes();
    }

    public void createDamageReport(DamageReport rentalContract) {
        this.damageRepo.createDamageReport(rentalContract);
    }

    public List<Car> getCarsWithPendingInspectionsForXDays(int days) {
        return this.damageRepo.getCarsWithPendingInspectionsForXDays(days);
    }


}
