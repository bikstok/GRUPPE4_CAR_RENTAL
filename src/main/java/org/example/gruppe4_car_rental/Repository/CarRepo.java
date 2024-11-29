package org.example.gruppe4_car_rental.Repository;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.CarModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class CarRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /*for at slette bil via frame_number skal vi først fjerne det fra RentalContract, da frame_number er en foreign key i RentalContract*/
    public boolean deleteCar(String frame_number) {
        this.jdbcTemplate.update("DELETE FROM RentalContract WHERE frame_number = ?", frame_number);
        return this.jdbcTemplate.update("DELETE FROM Cars WHERE frame_number = ?", frame_number) > 0;
    }

    public List<Car> fetchAllCars() {
        /*Da vores tabeller er normaliseret, så brand/model står i egen tabel, så er brand en foreign key i Cars tabellen.
        Derfor skal vi join CarModels tablenne til vores Cars tabel så vi kan få brand værdien med. */
        String sql = "SELECT Cars.frame_number, Cars.model, Cars.car_status, Cars.fuel_type, Cars.gear_type, " +
                "Cars.year_produced, Cars.monthly_sub_price, Cars.odometer, Cars.orignal_price, CarModels.brand " +
                "FROM Cars " +
                "JOIN CarModels ON Cars.model = CarModels.model";
        //String sql = "SELECT * FROM Cars"; // SQL query to fetch all cars
        RowMapper<Car> rowMapper = (rs, rowNum) -> new Car(
                rs.getString("frame_number"),
                rs.getString("brand"),
                rs.getString("model"),
                rs.getString("car_status"),
                rs.getString("fuel_type"),
                rs.getString("gear_type"),
                rs.getInt("year_produced"),
                rs.getDouble("monthly_sub_price"),
                rs.getInt("odometer"),
                rs.getDouble("orignal_price")
        );
        return jdbcTemplate.query(sql, rowMapper); // udfører query og mapper resultatet som objekter i en liste/table
    }
}
