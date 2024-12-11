package org.example.gruppe4_car_rental;

import org.example.gruppe4_car_rental.Model.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class RowMapperUtil {
    public static org.springframework.jdbc.core.RowMapper<Car> CAR_ROW_MAPPER =
            new BeanPropertyRowMapper(Car.class);
    public static org.springframework.jdbc.core.RowMapper<Customer> CUSTOMER_ROW_MAPPER=
            new BeanPropertyRowMapper(Customer.class);
    public static org.springframework.jdbc.core.RowMapper<RentalContract> RENTAL_CONTRACT_ROW_MAPPER =
            new BeanPropertyRowMapper(RentalContract.class);
    public static org.springframework.jdbc.core.RowMapper<CarPurchase> CAR_PURCHASE_ROW_MAPPER =
           new BeanPropertyRowMapper(CarPurchase.class);
    public static org.springframework.jdbc.core.RowMapper<DamageReport> DAMAGE_REPORT_ROW_MAPPER =
            new BeanPropertyRowMapper(DamageReport.class);
    
}
