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
        //String sql = "SELECT * FROM Cars"; // SQL query to fetch all cars

        return jdbcTemplate.query(sql, RowMapperUtil.CUSTOMER_ROW_MAPPER); // udfører query og mapper resultatet som objekter i en liste/table
    }

    // Fetch all customers with sortBy functionality using if-else
    public List<Customer> fetchAllCustomers(String sortBy) {
        // Hvis sortBy er null eller tom, brug standard sortering efter fornavn
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "c.first_name"; // Standard sortering

        }

        // Definerer SQL forespørgsel med if-else statement for sorteringen
        String sql = "";

        sql = "SELECT c.cpr_number, c.first_name, c.last_name, c.email, c.phone_number, c.address, c.zip_code, z.city FROM Customers c JOIN ZipCodes z ON c.zip_code = z.zip_code ORDER BY " + sortBy + ";";


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

    public boolean deleteCustomer(String cpr_number) {
        this.jdbcTemplate.update("DELETE FROM RentalContract WHERE cpr_number = ?", cpr_number);
        return this.jdbcTemplate.update("DELETE FROM Customers WHERE cpr_number = ?", cpr_number) > 0;
    }
}



