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
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "c.frame_number"; //standard sortering, hvis der ikke er klikket på nogen kategori.
        }

        /*SQL-query, der henter data fra Cars og CarModels tabellerne med mulighed for sortering.
        HUSK foreign key: model som tilhører CarModel som indeholder (brand, model)*/
        String sql = "SELECT c.frame_number, m.brand, c.model, c.car_status, c.fuel_type, c.gear_type, c.year_produced, c.monthly_sub_price, " +
                "c.odometer, c.original_price " +
                "FROM Cars c " +
                "JOIN CarModels m ON c.model = m.model " +
                "ORDER BY " + sortBy;

        //Kører SQL'en og map'per resultaterne til Car-objekter vha. RowMapper.
        return this.jdbcTemplate.query(sql, RowMapperUtil.CAR_ROW_MAPPER);
    }

    /*for at slette bil via frame_number skal vi først fjerne det fra RentalContract,
    da frame_number er en foreign key i RentalContract*/
    public boolean deleteCar(String frame_number) {
        this.jdbcTemplate.update("DELETE FROM CarPurchase WHERE contract_id IN (SELECT contract_id FROM RentalContract WHERE frame_number = ?)", frame_number);
        this.jdbcTemplate.update("DELETE FROM DamageReport WHERE contract_id IN (SELECT contract_id FROM RentalContract WHERE frame_number = ?)", frame_number);
        this.jdbcTemplate.update("DELETE FROM RentalContract WHERE frame_number = ?", frame_number);
        return this.jdbcTemplate.update("DELETE FROM Cars WHERE frame_number = ?", frame_number) > 0;
    }

    //Metode til at tælle antallet af biler med en bestemt status.
    public int countByCarStatus(String status) {
        String sql = "SELECT COUNT(*) FROM Cars WHERE car_status = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, status);
    }

    //Henter bil ud fra det valgte frame_number for at kunne redigere i bilen.
    public Car findByFrameNumber(String frameNumber) {
        String sql = "SELECT c.*, cm.brand " +
                "FROM Cars c " +
                "JOIN CarModels cm ON c.model = cm.model " +
                "WHERE c.frame_number = ?";
        return this.jdbcTemplate.queryForObject(sql, new Object[]{frameNumber}, RowMapperUtil.CAR_ROW_MAPPER);
    }

    //Opdaterer bilens information i databasen efter redigering (via editCar redigeringsformular)
    public void updateCar(Car car) {

        /*For at vi kan redigere/opdatere i CarModels tabellen og ikke kun Cars tabellen laves følgende sql
        (eftersom vi skal kunne redigere vores foreign keys (model og brand fra CarModels)*/
        this.jdbcTemplate.update(
                "INSERT INTO CarModels (model, brand) " +
                        "VALUES (?, ?) " +
                        "ON DUPLICATE KEY UPDATE brand = VALUES(brand)",
            car.getModel(), car.getBrand()
        );

        /*For at opdatere i Cars tabellen.
        Vi redigerer IKKE frame_number men skal bruge det til at tage fat i den bil man vil redigere.*/
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
