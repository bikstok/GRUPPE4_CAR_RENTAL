package org.example.gruppe4_car_rental.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BusinessRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //Viser antallet af udlejede biler
    public int countRentedCars() {
        //Viser antallet af hvor mange gange der står 'Lejet' i vores sql.
        String sql = "SELECT COUNT(*) FROM Cars WHERE car_status = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, "Lejet");
    }
}
