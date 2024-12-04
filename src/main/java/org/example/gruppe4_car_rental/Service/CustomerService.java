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

    public boolean deleteCustomer(String cpr_number) {
        return this.customerRepo.deleteCustomer(cpr_number);
    }

    public List<Customer> fetchAllCustomers() {
        return customerRepo.fetchAllCustomers();
    }


    // Metode til at hente alle kunder og sortere efter den angivne 'sortBy'
    public List<Customer> fetchAllCustomers(String sortBy) {
        return customerRepo.fetchAllCustomers(sortBy);
    }
    // public List<Customer> fetchAllCustomers() {
    //     List<Customer> customers = this.customerRepo.fetchAllCustomers();
    //     //for (Customer element : customers) {
    //     //    System.out.println(element);
    //     //}
    //     return customers;
    // }
}