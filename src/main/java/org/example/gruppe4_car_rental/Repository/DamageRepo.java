package org.example.gruppe4_car_rental.Repository;

import org.example.gruppe4_car_rental.Model.DamageReport;
import org.example.gruppe4_car_rental.Model.DamageType;
import org.example.gruppe4_car_rental.Model.RentalContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DamageRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<RentalContract> fetchContractsWithPendingInspections() {
        String sql = "SELECT " +
                "    rc.contract_id, " +
                "    rc.cpr_number, " +
                "    rc.frame_number, " +
                "    rc.start_date, " +
                "    rc.end_date, " +
                "    rc.insurance, " +
                "    rc.total_price, " +
                "    rc.max_km, " +
                "    rc.voucher " +
                "FROM " +
                "    RentalContract rc " +
                "JOIN " +
                "    Cars c " +
                "ON " +
                "    rc.frame_number = c.frame_number " +
                "WHERE " +
                "    c.car_status = 'Mangler tilsyn';";

        RowMapper<RentalContract> rowMapper = (rs, rowNum) -> new RentalContract(
                rs.getInt("contract_id"),
                rs.getString("cpr_number"),
                rs.getString("frame_number"),
                rs.getDate("start_date"),
                rs.getDate("end_date"),
                rs.getBoolean("insurance"),
                rs.getDouble("total_price"),
                rs.getInt("max_km"),
                rs.getBoolean("voucher")
        );
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<DamageType> fetchAllDamageTypes() {
        String sql = "SELECT damage_name, damage_price FROM DamageType";
        RowMapper<DamageType> rowMapper = (rs, rowNum) -> new DamageType(
                rs.getString("damage_name"),
                rs.getDouble("damage_price")
        );
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void createDamageReport(DamageReport damageReport) {
        String sql = "INSERT INTO DamageReport (contract_id, total_repair_price) VALUES (?, ?)";
        this.jdbcTemplate.update(sql,
                damageReport.getContract_id(),
                damageReport.getTotal_repair_price()
        );
    }
}
