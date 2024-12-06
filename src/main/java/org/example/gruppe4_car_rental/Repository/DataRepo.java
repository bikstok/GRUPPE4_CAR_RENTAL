package org.example.gruppe4_car_rental.Repository;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.RowMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DataRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public double getMonthlySubscriptionPriceFromFrameNumber(final String frame_number) {
        String sql = "SELECT Cars.monthly_sub_price FROM Cars WHERE frame_number = ?";
        return this.jdbcTemplate.queryForObject(sql, Double.class, frame_number);
    }

    public List<Car> fetchAllAvailableCars() {
        String sql = "SELECT Cars.frame_number, Cars.model, Cars.car_status, Cars.fuel_type, Cars.gear_type, " +
                "Cars.year_produced, Cars.monthly_sub_price, Cars.odometer, Cars.orignal_price, CarModels.brand " +
                "FROM Cars " +
                "JOIN CarModels ON Cars.model = CarModels.model " +
                "WHERE Cars.car_status = 'Ledig'";
        return jdbcTemplate.query(sql, RowMapperUtil.CAR_ROW_MAPPER);

    }

}
