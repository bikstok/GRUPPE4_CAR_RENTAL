package org.example.gruppe4_car_rental.Repository;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.RentalContract;
import org.example.gruppe4_car_rental.RowMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

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
    public int countRentedCarsOverall() {
        String sql = "SELECT COUNT(*) FROM RentalContract";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public List<Map<String, Object>> getTopRentedCarBrands() {
        //udfører en SQL-forespørgsel, der finder de 5 mest udlejede bilbrands baseret på antallet af lejeaftaler.
        //Antallet af gange, et brand er blevet lejet, beregnes ved at:
        // 1. Tælle rækker i RentalContract for hver bil
        // 2. Gruppere resultaterne efter brand
        // 3. Summere antallet af lejeaftaler for hver gruppe.
        String sql = "SELECT cm.brand AS brand, COUNT(rc.frame_number) AS rental_count " +
                "FROM RentalContract rc " +
                "JOIN Cars c ON rc.frame_number = c.frame_number " +
                "JOIN CarModels cm ON c.model = cm.model " +
                "GROUP BY cm.brand " +
                "ORDER BY rental_count DESC " +
                "LIMIT 5";
        return jdbcTemplate.queryForList(sql);
    }

    public double calculateMonthlyEarningsJava() {
        // SQL: Hent alle kontrakter, der er aktive i øjeblikket
        // 1. Beregning af den samlede månedlige indtjening
        // 2. Konverter start- og slutdato til LocalDate
        // 3. Beregn varigheden af kontrakten i måneder
        // 4. Beregn månedlig pris og tilføj til totalen

        String sql = "SELECT * FROM RentalContract WHERE CURRENT_DATE BETWEEN start_date AND end_date";
        List<RentalContract> rentalContracts = this.jdbcTemplate.query(sql, RowMapperUtil.RENTAL_CONTRACT_ROW_MAPPER);
        double totalMonthlyEarnings = 0;
        for (RentalContract rentalContract : rentalContracts) {
            LocalDate local_start_date = rentalContract.getStart_date().toLocalDate();
            LocalDate local_end_date = rentalContract.getEnd_date().toLocalDate();

            long months = ChronoUnit.MONTHS.between(local_start_date, local_end_date);
            if (months < 3 || months > 36) {
                continue;
            }

            double monthlyEarnings = rentalContract.getTotal_price() / months;
            totalMonthlyEarnings += monthlyEarnings;

        }
        return totalMonthlyEarnings;
    }

    public List<Map<String, Object>> overviewOfCarPurchases() {
        //1. Sammensætter køberens fulde navn (fornavn og efternavn)
        //2. Tæller antal biler købt af hver kunde
        //3. Summerer det samlede beløb brugt af kunden på bilkøb
        //4. Joiner RentalContract for at matche bilkøbet med en specifik lejekontrakt
        //5. Joiner Customers for at få kundens detaljer fra lejekontrakten
        //6. Grupperer resultaterne efter kunde, baseret på CPR-nummer
        String sql = """
                    SELECT
                        CONCAT(Customers.first_name, ' ', Customers.last_name) AS buyer_name, 
                        COUNT(CarPurchase.car_purchase_id) AS cars_purchased,
                        SUM(CarPurchase.purchase_price) AS total_spent
                    FROM
                        CarPurchase
                    JOIN
                        RentalContract ON CarPurchase.contract_id = RentalContract.contract_id
                    JOIN
                        Customers ON RentalContract.cpr_number = Customers.cpr_number
                    GROUP BY
                        Customers.cpr_number
                    ORDER BY
                        total_spent DESC;
                """;
        return jdbcTemplate.queryForList(sql);
    }

    /*Finder returnerede biler udfra slut-datoen af udlejningen. Vi skal have hele car objektet med
    Alternativt kunne man lave en seperat klasse med de værdier man ønsker at fremvise*/
    public List<Car> findReturnedCarsByEndDate(LocalDate end_date) {
        String sql = "SELECT c.frame_number, c.model, cm.brand, c.car_status, c.fuel_type, " +
                "c.gear_type, c.year_produced, c.monthly_sub_price, c.odometer, c.original_price " +
                "FROM Cars c " +
                "JOIN CarModels cm ON cm.model = c.model " +
                "JOIN RentalContract r ON c.frame_number = r.frame_number " +
                "WHERE r.end_date = ?";

        return this.jdbcTemplate.query(sql, RowMapperUtil.CAR_ROW_MAPPER, end_date);
    }
}