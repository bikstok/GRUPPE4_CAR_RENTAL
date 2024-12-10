package org.example.gruppe4_car_rental.Repository;

import org.example.gruppe4_car_rental.Model.Customer;
import org.example.gruppe4_car_rental.RowMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Customer> fetchAllCustomers() {
        /*Da vores tabeller er normaliseret, så zipcode og city står i egen tabel, så er brand en foreign key i Customers tabellen.
        Derfor skal vi join ZipCode tablenne til vores Customers tabel så vi kan få zipcode og city værdien med. */
        String sql = "SELECT Customers.cpr_number, Customers.first_name, Customers.last_name, Customers.email, Customers.phone_number, Customers.address, Customers.zip_code, ZipCodes.city " +
                "FROM Customers " +
                "JOIN ZipCodes ON Customers.zip_code = ZipCodes.zip_code";
        //String sql = "SELECT * FROM Customers"; // SQL query to fetch all customers

        //Kører SQL'en og map'per resultaterne til Customer-objekter vha. RowMapper.
        return jdbcTemplate.query(sql, RowMapperUtil.CUSTOMER_ROW_MAPPER); // udfører query og mapper resultatet som objekter i en liste/table
    }

    //Vise liste af kunder efter den valgte sorteringsmulighed
    public List<Customer> fetchAllCustomers(String sortBy) {
        // Hvis sortBy er null eller tom, brug standard sortering efter fornavn
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "c.first_name"; //standard sortering, hvis der ikke er klikket på nogen kategori.

        }
        //SQL-query, der henter data fra Customers og ZipCodes tabellerne med mulighed for sortering.
        //HUSK foreign key: zip_code som tilhører ZipCodes som indeholder (zip_code, city)*/
        String sql = "SELECT c.cpr_number, c.first_name, c.last_name, c.email, c.phone_number, c.address, c.zip_code, z.city " +
                     "FROM Customers c JOIN ZipCodes z ON c.zip_code = z.zip_code ORDER BY " + sortBy;

        RowMapper<Customer> rowMapper = (rs, rowNum) -> new Customer(
                rs.getString("cpr_number"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone_number"),
                rs.getString("address"),
                rs.getString("city"),
                rs.getString("zip_code")
        );

        return jdbcTemplate.query(sql, rowMapper);
    }

    //Sletter en kunde baseret på cpr_number
    public boolean deleteCustomer(String cpr_number) {
        this.jdbcTemplate.update("DELETE FROM CarPurchase WHERE contract_id IN (SELECT contract_id FROM RentalContract WHERE cpr_number = ?)", cpr_number);
        this.jdbcTemplate.update("DELETE FROM DamageReport WHERE contract_id IN (SELECT contract_id FROM RentalContract WHERE cpr_number = ?)", cpr_number);
        this.jdbcTemplate.update("DELETE FROM RentalContract WHERE cpr_number = ?", cpr_number);
        return this.jdbcTemplate.update("DELETE FROM Customers WHERE cpr_number = ?", cpr_number) > 0;
    }

    //Henter kunde ud fra det valgte cpr_number for at kunne redigere i kunden.
    public Customer findByCprNumber(String CprNumber) {
        String sql = "SELECT c.*, z.city " +
                "FROM Customers c " +
                "JOIN ZipCodes z ON c.zip_code = z.zip_code " +
                "WHERE c.cpr_number = ?";

        //Kører SQL'en og map'per resultaterne til Customer-objekter vha. RowMapper.
        return this.jdbcTemplate.queryForObject(sql, new Object[]{CprNumber}, RowMapperUtil.CUSTOMER_ROW_MAPPER);
    }

    //Opdaterer kundens information i databasen efter redigering (via editCustomer redigeringsformular)
    public void updateCustomer(Customer customer) {
        System.out.println("we are updating customer " + customer);
        System.out.println("updating customersModels in repo");

        /*For at vi kan redigere/og opdatere i ZipCodes tabellen og ikke kun Customer tabellen
        (eftersom vi skal kunne redigere vores foreign keys (zip_code og city fra ZipCodes)*/
        this.jdbcTemplate.update(
                "INSERT INTO ZipCodes (zip_code, city) " +
                        "VALUES (?, ?) " +
                        "ON DUPLICATE KEY UPDATE city = VALUES(city)",
                customer.getZip_code(), customer.getCity()
        );

        /*For at opdatere i Customers tabellen.
        Vi redigerer IKKE cpr_number men skal bruge det til at tage fat i den kunde man vil redigere.*/
        String sql = "UPDATE Customers SET first_name = ?, last_name = ?, email = ?, phone_number = ?, address = ?, zip_code = ? WHERE cpr_number = ?";
        this.jdbcTemplate.update(sql,
                customer.getFirst_name(),
                customer.getLast_name(),
                customer.getEmail(),
                customer.getPhone_number(),
                customer.getAddress(),
                customer.getZip_code(),
                customer.getCpr_number()

        );
    }
}



