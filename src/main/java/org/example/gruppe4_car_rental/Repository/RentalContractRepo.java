package org.example.gruppe4_car_rental.Repository;

import org.example.gruppe4_car_rental.Model.CarPurchase;
import org.example.gruppe4_car_rental.Model.RentalContract;
import org.example.gruppe4_car_rental.RowMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RentalContractRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createRentalContract(RentalContract rentalContract) {
        String sql = "INSERT INTO RentalContract (cpr_number, frame_number, start_date, end_date, insurance, total_price, max_km, voucher, start_odometer) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        this.jdbcTemplate.update(sql,
                rentalContract.getCpr_number(),
                rentalContract.getFrame_number(),
                rentalContract.getStart_date(),
                rentalContract.getEnd_date(),
                rentalContract.isInsurance(),
                rentalContract.getTotal_price(),
                rentalContract.getMax_km(),
                rentalContract.getVoucher(),
                rentalContract.getStart_odometer()
        );
    }

    public List<CarPurchase> fetchAllCarPurchases() {
        String sql = "SELECT car_purchase_id, contract_id, purchase_price FROM CarPurchase";
        return this.jdbcTemplate.query(sql, RowMapperUtil.CAR_PURCHASE_ROW_MAPPER);
    }

    public List<RentalContract> fetchAllRentalContracts() {
        String sql = "SELECT RentalContract.contract_id, RentalContract.cpr_number,RentalContract.frame_number, RentalContract.start_date, RentalContract.end_date, RentalContract.insurance," +
                "RentalContract.total_price, RentalContract.max_km, RentalContract.voucher, RentalContract.start_odometer FROM RentalContract";
        return this.jdbcTemplate.query(sql, RowMapperUtil.RENTAL_CONTRACT_ROW_MAPPER);
    }

    public RentalContract getRentalContractFromContractId(int contract_id) {
        String sql = "SELECT * FROM RentalContract WHERE contract_id = ?";
        return jdbcTemplate.queryForObject(sql, RowMapperUtil.RENTAL_CONTRACT_ROW_MAPPER, contract_id);
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
        this.jdbcTemplate.update("DELETE FROM CarPurchase WHERE contract_id = ?", contract_id);
        this.jdbcTemplate.update("DELETE FROM DamageReport WHERE contract_id = ?", contract_id);
        return this.jdbcTemplate.update("DELETE FROM RentalContract WHERE contract_id = ?", contract_id) > 0;
    }

    public List<RentalContract> getActiveRentalContracts() {
        String sql = "SELECT * FROM RentalContract WHERE CURRENT_DATE BETWEEN start_date AND end_date";
        return this.jdbcTemplate.query(sql, RowMapperUtil.RENTAL_CONTRACT_ROW_MAPPER);
    }


    public void updateRentalContract(RentalContract rentalContract) {
        //System.out.println("we are updating rentalcontract " + rentalContract);
        //System.out.println("updating in repo");

        /*For at opdatere i RentalContracts tabellen.
        Vi redigerer IKKE contract_id men skal bruge det til at tage fat i den rentalContract man vil redigere.*/

        String sql = "UPDATE RentalContract SET cpr_number = ?, frame_number = ?, start_date = ?, end_start = ?, insurance = ?, total_price = ?, max_km = ?, voucher = ? WHERE contract_id = ?";
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
    }
}



