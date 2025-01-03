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

    // Jakob og Albert
    public List<RentalContract> fetchContractsWithPendingInspections() {
        return this.damageRepo.fetchContractsWithPendingInspections();
    }

    public List<DamageType> fetchAllDamageTypes() {
        return this.damageRepo.fetchAllDamageTypes();
    }

    // Jakob og Albert
    public void createDamageReport(DamageReport rentalContract) {
        this.damageRepo.createDamageReport(rentalContract);
    }

    // Jakob og Albert
    public List<Car> getCarsWithPendingInspectionsForXDays(int days) {
        return this.damageRepo.getCarsWithPendingInspectionsForXDays(days);
    }

    // Jakob og Albert
    public List<DamageReport> fetchAllDamageReports() {
        return this.damageRepo.fetchAllDamageReports();
    }
    // Albert
    public void deleteDamageReport(int contract_id) {
        this.damageRepo.deleteDamageReport(contract_id);
    }
}
