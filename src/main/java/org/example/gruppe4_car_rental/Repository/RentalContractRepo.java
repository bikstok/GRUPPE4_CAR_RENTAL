package org.example.gruppe4_car_rental.Repository;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.RentalContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RentalContractRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createRentalContract(RentalContract rentalContract) {
        String sql = "INSERT INTO RentalContract (cpr_number, frame_number, start_date, end_date, insurance, total_price, max_km, voucher) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        this.jdbcTemplate.update(sql,
                rentalContract.getCpr_number(),
                rentalContract.getFrame_number(),
                rentalContract.getStart_date(),
                rentalContract.getEnd_date(),
                rentalContract.isInsurance(),
                rentalContract.getTotal_price(),
                rentalContract.getMax_km(),
                rentalContract.getVoucher()
        );
        String updateCarStatus = "UPDATE Cars SET car_status = 'Lejet' WHERE frame_number = ?";
        this.jdbcTemplate.update(updateCarStatus, rentalContract.getFrame_number());
    }

    public double getMonthlySubscriptionPriceFromFrameNumber(final String frame_number) {
        String sql = "SELECT Cars.monthly_sub_price FROM Cars WHERE frame_number = ?";
        return this.jdbcTemplate.queryForObject(sql, Double.class, frame_number);
    }

    public List<RentalContract> fetchAllRentalContracts() {
        String sql = "SELECT RentalContract.contract_id, RentalContract.cpr_number,RentalContract.frame_number, RentalContract.start_date, RentalContract.end_date, RentalContract.insurance," +
                "RentalContract.total_price, RentalContract.max_km, RentalContract.voucher FROM RentalContract";

        RowMapper<RentalContract> rowMapper = (resultSet, rowNum) -> new RentalContract(
                resultSet.getInt("contract_id"),
                resultSet.getString("cpr_number"),
                resultSet.getString("frame_number"),
                resultSet.getDate("start_date"),
                resultSet.getDate("end_date"),
                resultSet.getBoolean("insurance"),
                resultSet.getDouble("total_price"),
                resultSet.getInt("max_km"),
                resultSet.getBoolean("voucher")
                );
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Car> fetchAllAvailableCars() {
        String sql = "SELECT Cars.frame_number, Cars.model, Cars.car_status, Cars.fuel_type, Cars.gear_type, " +
                "Cars.year_produced, Cars.monthly_sub_price, Cars.odometer, Cars.orignal_price, CarModels.brand " +
                "FROM Cars " +
                "JOIN CarModels ON Cars.model = CarModels.model " +
                "WHERE Cars.car_status = 'Ledig'";
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
        return jdbcTemplate.query(sql, rowMapper);

    }

    public boolean deleteRentalContract(int contract_id) {
        String frameNumber = this.jdbcTemplate.queryForObject(
                "SELECT frame_number FROM RentalContract WHERE contract_id = ?",
                String.class,
                contract_id
        );

        this.jdbcTemplate.update(
                "UPDATE Cars SET car_status = 'Ledig' WHERE frame_number = ?",
                frameNumber
        );



        return this.jdbcTemplate.update("DELETE FROM RentalContract WHERE contract_id = ?", contract_id) > 0;
    }
}
