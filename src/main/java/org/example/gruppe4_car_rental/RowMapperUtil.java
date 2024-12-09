package org.example.gruppe4_car_rental;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.CarPurchase;
import org.example.gruppe4_car_rental.Model.Customer;
import org.example.gruppe4_car_rental.Model.RentalContract;

public class RowMapperUtil {
    public static org.springframework.jdbc.core.RowMapper<Car> CAR_ROW_MAPPER = (rs, rowNum) -> new Car(
            rs.getString("frame_number"),
            rs.getString("brand"),
            rs.getString("model"),
            rs.getString("car_status"),
            rs.getString("fuel_type"),
            rs.getString("gear_type"),
            rs.getInt("year_produced"),
            rs.getInt("monthly_sub_price"),
            rs.getInt("odometer"),
            rs.getDouble("original_price"),
            rs.getDate ("end_date")
    );
    public static org.springframework.jdbc.core.RowMapper<Customer> CUSTOMER_ROW_MAPPER = (rs, rowNum) -> new Customer(
            rs.getString("cpr_number"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("phone_number"),
            rs.getString("address"),
            rs.getString("city"),
            rs.getString("zip_code")

    );
    public static org.springframework.jdbc.core.RowMapper<RentalContract> RENTAL_CONTRACT_ROW_MAPPER = (resultSet, rowNum) -> new RentalContract(
            resultSet.getInt("contract_id"),
            resultSet.getString("cpr_number"),
            resultSet.getString("frame_number"),
            resultSet.getDate("start_date"),
            resultSet.getDate("end_date"),
            resultSet.getBoolean("insurance"),
            resultSet.getDouble("total_price"),
            resultSet.getInt("max_km"),
            resultSet.getBoolean("voucher"),
            resultSet.getInt("start_odometer")
    );

   public static org.springframework.jdbc.core.RowMapper<CarPurchase> CAR_PURCHASE_ROW_MAPPER = (resultSet, rowNum) -> new CarPurchase(
           resultSet.getInt("car_purchase_id"),
           resultSet.getInt("contract_id"),
           resultSet.getDouble("purchase_price")
   );
    
}
