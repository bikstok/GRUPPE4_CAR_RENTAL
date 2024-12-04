package org.example.gruppe4_car_rental.Repository;
import java.util.Map;
import java.util.List;

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
        // udfører en SQL-forespørgsel, der finder de 5 mest udlejede bilbrands baseret på antallet af lejeaftaler.
        //Antallet af gange, et brand er blevet lejet, beregnes ved at:
        //1. Tælle rækker i RentalContract for hver bil, 2. Gruppere resultaterne efter brand, 3. Summere antallet af lejeaftaler for hver gruppe.
    public List<Map<String, Object>> getTopRentedCarBrands() {
        String sql = "SELECT cm.brand AS brand, COUNT(rc.frame_number) AS rental_count " +
                "FROM RentalContract rc " +
                "JOIN Cars c ON rc.frame_number = c.frame_number " +
                "JOIN CarModels cm ON c.model = cm.model " +
                "GROUP BY cm.brand " +
                "ORDER BY rental_count DESC " +
                "LIMIT 5";

        // Returner resultatet som en liste af Map (nøgle/værdi-par)
        return jdbcTemplate.queryForList(sql);
    }

    }



