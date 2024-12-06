package org.example.gruppe4_car_rental.Repository;

import org.example.gruppe4_car_rental.Model.RentalContract;
import org.example.gruppe4_car_rental.RowMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

@Repository
public class BusinessRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //Viser antallet af udlejede biler lige nu (som i at den er startet)
    public int countRentedCars() {
        //Viser antallet af hvor mange gange der står 'Lejet' i vores sql.
        String sql = "SELECT COUNT(*) FROM Cars WHERE car_status = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, "Lejet");
    }

    //Viser antallet af lejekontrakter overall (på alle tidspunkter og frem)
    public int countRentedCarsOverall () {
         String sql = "SELECT COUNT(*) FROM RentalContract";
        return jdbcTemplate.queryForObject(sql, Integer.class);
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

    public double calculateMonthlyEarningsJava() {
        // SQL: Hent alle kontrakter, der er aktive i øjeblikket
        String sql = "SELECT * FROM RentalContract WHERE CURRENT_DATE BETWEEN start_date AND end_date";
        List<RentalContract> rentalContracts = this.jdbcTemplate.query(sql, RowMapperUtil.RENTAL_CONTRACT_ROW_MAPPER);

        // Beregning af den samlede månedlige indtjening
        double totalMonthlyEarnings = 0;

        for (RentalContract rentalContract : rentalContracts) {

            // Konverter start- og slutdato til LocalDate
            LocalDate local_start_date = rentalContract.getStart_date().toLocalDate();
            LocalDate local_end_date = rentalContract.getEnd_date().toLocalDate();

            // Beregn varigheden af kontrakten i måneder
            long months = ChronoUnit.MONTHS.between(local_start_date, local_end_date); //Den kan beregne forskellen mellem to datoer/tidspunkter i en specifik tidsenhed (f.eks. antal dage, måneder, år osv.).
            if (months < 3 || months > 36) {
                continue; // Ignorer kontrakter uden for intervallet 3-36 måneder
            }
            // Beregn månedlig pris og tilføj til totalen
            double monthlyEarnings = rentalContract.getTotal_price() / months;
            totalMonthlyEarnings += monthlyEarnings;
            
        }
        return totalMonthlyEarnings;
    }



}


