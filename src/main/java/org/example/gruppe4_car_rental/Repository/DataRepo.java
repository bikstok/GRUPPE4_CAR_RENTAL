package org.example.gruppe4_car_rental.Repository;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.CarPurchase;
import org.example.gruppe4_car_rental.Model.RentalContract;
import org.example.gruppe4_car_rental.RowMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    // Jakob og Albert
    public double getMonthlySubscriptionPriceFromFrameNumber(final String frame_number) {
        String sql = "SELECT Cars.monthly_sub_price FROM Cars WHERE frame_number = ?";
        return this.jdbcTemplate.queryForObject(sql, Double.class, frame_number);
    }
    // Jakob og Albert
    public Car getCarFromFrameNumber(String frame_number) {
        String sql = """
        SELECT c.frame_number, cm.brand, c.model, c.car_status, c.fuel_type, 
               c.gear_type, c.year_produced, c.monthly_sub_price, 
               c.odometer, c.original_price
        FROM Cars c
        JOIN CarModels cm ON c.model = cm.model
        WHERE c.frame_number = ?
       """;
        return jdbcTemplate.queryForObject(sql, RowMapperUtil.CAR_ROW_MAPPER, frame_number);
    }
    // Jakob og Albert
    public List<Car> fetchAllAvailableCars() {
        String sql = "SELECT Cars.frame_number, Cars.model, Cars.car_status, Cars.fuel_type, Cars.gear_type, " +
                "Cars.year_produced, Cars.monthly_sub_price, Cars.odometer, Cars.original_price, CarModels.brand " +
                "FROM Cars " +
                "JOIN CarModels ON Cars.model = CarModels.model " +
                "WHERE Cars.car_status = 'Ledig'";
        return jdbcTemplate.query(sql, RowMapperUtil.CAR_ROW_MAPPER);

    }
    // Jakob og Albert
    public void createCarPurchase(CarPurchase carPurchase, RentalContract rentalContract, Car car) {
        String sql = "INSERT INTO CarPurchase (contract_id, purchase_price) VALUES (?, ?)";
        this.jdbcTemplate.update(sql, carPurchase.getContract_id(), carPurchase.getPurchase_price());

        String updateCarStatus = "UPDATE Cars SET car_status = 'Solgt' WHERE frame_number = ?";
        this.jdbcTemplate.update(updateCarStatus, car.getFrame_number());
    }
    // Jakob og Albert
    public void deleteCarPurchase(int car_purchase_id) {
        int contract_id = this.jdbcTemplate.queryForObject(
                "SELECT contract_id FROM CarPurchase WHERE car_purchase_id = ?",
                Integer.class,
                car_purchase_id
        );

        String frameNumber = this.jdbcTemplate.queryForObject(
                "SELECT frame_number FROM RentalContract WHERE contract_id = ?",
                String.class,
                contract_id
        );

        this.jdbcTemplate.update(
                "UPDATE Cars SET car_status = 'Ledig' WHERE frame_number = ?",
                frameNumber
        );

        this.jdbcTemplate.update("DELETE FROM CarPurchase WHERE car_purchase_id = ?", car_purchase_id);
    }
}
