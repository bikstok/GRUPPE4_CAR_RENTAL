package org.example.gruppe4_car_rental.Service;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.Customer;
import org.example.gruppe4_car_rental.Repository.CarRepo;
import org.example.gruppe4_car_rental.Repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    public List<Customer> fetchAllCustomers() {
        return customerRepo.fetchAllCustomers();
    }

    //Sletter kunden ud fra cpr_number
    public boolean deleteCustomer(String cpr_number) {
        return this.customerRepo.deleteCustomer(cpr_number);
    }

    //Returnerer liste af sorterede kunder efter den valgte sortBy
    public List<Customer> fetchAllCustomers(String sortBy) {
        return customerRepo.fetchAllCustomers(sortBy);
    }

    //Redigerer kunde ud fra cpr_number
    public Customer findByCprNumber(String cpr_number) {
        return customerRepo.findByCprNumber(cpr_number);  // Brug CustomerRepo til at hente bilen
    }

    //Opdaterer kundens information efter redigering
    public void updateCustomer(Customer customer) {
        this.customerRepo.updateCustomer(customer);  // Brug CustomerRepo til at opdatere bilen i databasen
    }
}
