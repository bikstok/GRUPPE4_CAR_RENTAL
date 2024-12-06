package org.example.gruppe4_car_rental.Repository;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.RowMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class CarRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Car> fetchAllCars(String sortBy) {
        // Standard sortering
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "c.frame_number";
        }

        // SQL-query med sorteringsparameter
        String sql = "SELECT c.frame_number, m.brand, c.model, c.car_status, c.fuel_type, c.gear_type, c.year_produced, c.monthly_sub_price, " +
                "c.odometer, c.original_price " +
                "FROM Cars c " +
                "JOIN CarModels m ON c.model = m.model " +
                "ORDER BY " + sortBy;

        return this.jdbcTemplate.query(sql, RowMapperUtil.CAR_ROW_MAPPER);
    }

    /*for at slette bil via frame_number skal vi først fjerne det fra RentalContract, da frame_number er en foreign key i RentalContract*/
    public boolean deleteCar(String frame_number) {
        this.jdbcTemplate.update("DELETE FROM RentalContract WHERE frame_number = ?", frame_number);
        return this.jdbcTemplate.update("DELETE FROM Cars WHERE frame_number = ?", frame_number) > 0;
    }

    public int countByCarStatus(String status) {
        String sql = "SELECT COUNT(*) FROM Cars WHERE car_status = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, status);
    }

    //// Hent bil ud fra frame_number
    public Car findByFrameNumber(String frameNumber) {
        String sql = "SELECT c.*, cm.brand " +
                "FROM Cars c " +
                "JOIN CarModels cm ON c.model = cm.model " +
                "WHERE c.frame_number = ?";
        return this.jdbcTemplate.queryForObject(sql, new Object[]{frameNumber}, RowMapperUtil.CAR_ROW_MAPPER);
    }

    // Opdater bilens information i databasen
    public void updateCar(Car car) {
        //System.out.println("we are updating car " + car);

        //System.out.println("updating carModels in repo");
        // Insert new model and brand into CarModels
        String insertCarModelSql = "INSERT INTO CarModels (model, brand) VALUES (?, ?)";
        this.jdbcTemplate.update(insertCarModelSql, car.getModel(), car.getBrand());


        //System.out.println("updating car in repo");
        String sql = "UPDATE Cars SET model = ?, car_status = ?, fuel_type = ?, gear_type = ?, year_produced = ?, monthly_sub_price = ?, odometer = ?, original_price = ? WHERE frame_number = ?";
        this.jdbcTemplate.update(sql,
                car.getModel(),
                car.getCar_status(),
                car.getFuel_type(),
                car.getGear_type(),
                car.getYear_produced(),
                car.getMonthly_sub_price(),
                car.getOdometer(),
                car.getOriginal_price(),
                car.getFrame_number()
        );
    }
}
