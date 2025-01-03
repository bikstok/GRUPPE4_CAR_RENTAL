package org.example.gruppe4_car_rental.Repository;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.DamageReport;
import org.example.gruppe4_car_rental.Model.DamageType;
import org.example.gruppe4_car_rental.Model.RentalContract;
import org.example.gruppe4_car_rental.RowMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DamageRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Jakob og Albert
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

        return jdbcTemplate.query(sql, RowMapperUtil.RENTAL_CONTRACT_ROW_MAPPER);
    }

    //Henter alle damage types så vi senere kan udregne totalprisen af skader.
    public List<DamageType> fetchAllDamageTypes() {
        String sql = "SELECT damage_name, damage_price FROM DamageType";
        RowMapper<DamageType> rowMapper = (rs, rowNum) -> new DamageType(
                rs.getString("damage_name"),
                rs.getDouble("damage_price")
        );
        return jdbcTemplate.query(sql, rowMapper);
    }

    // Jakob og Albert
    //For at oprette skadesrapport ud fra contract_id med udregning af totalpris af skaderne
    public void createDamageReport(DamageReport damageReport) {
        String frameNumber = this.jdbcTemplate.queryForObject(
                "SELECT frame_number FROM RentalContract WHERE contract_id = ?",
                String.class,
                damageReport.getContract_id()
        );

        // Albert
        /*I princippet et simpelt if/else statement,som tjekker om reperationsprisen er større end 0 :
        Hvis ja =skadet, hvis nej = ledig. */
        String status = damageReport.getTotal_repair_price() > 0 ? "Skadet" : "Ledig";
        this.jdbcTemplate.update(
                "UPDATE Cars SET car_status = '" + status + "' WHERE frame_number = ?",
                frameNumber
        );
        //skadesrapport indsættes og opdateres i DamageReport tabel
        String sql = "INSERT INTO DamageReport (contract_id, total_repair_price) VALUES (?, ?)";
        this.jdbcTemplate.update(sql,
                damageReport.getContract_id(),
                damageReport.getTotal_repair_price()
        );
    }

    // Jakob og Albert
    //Finde antal biler der mangler tilsyn
    public List<Car> getCarsWithPendingInspectionsForXDays(int days) {
        List<Car> results = new ArrayList<>();
        List<Car> carsWithPendingInspection = this.jdbcTemplate.query(
                "SELECT c.frame_number, c.model, cm.brand, c.car_status, c.fuel_type, c.gear_type, c.year_produced, c.monthly_sub_price, c.odometer, c.original_price " +
                        "FROM Cars c " +
                        "JOIN CarModels cm ON c.model = cm.model " +
                        "WHERE c.car_status = 'Mangler tilsyn'",
                RowMapperUtil.CAR_ROW_MAPPER
        );

        /*For each loop går igennem biler som mangler tilsyn
        SQL-forespørgsel for at hente alle lejekontrakter for en bil,
        sorteret efter slutdato, den nyeste først*/
        for (Car car : carsWithPendingInspection) {
            String fetchContractsSql = "SELECT * FROM RentalContract WHERE frame_number = ? ORDER BY end_date DESC";
            List<RentalContract> rentalContracts = this.jdbcTemplate.query(
                    fetchContractsSql,
                    new Object[]{car.getFrame_number()},
                    RowMapperUtil.RENTAL_CONTRACT_ROW_MAPPER
            );
            /*Må kun stå i mangler tilsyn i 2 dage. Tager fat i den nyeste lejekontrakt
             Finder dens slutdato og ser om det er før 2 dage siden
             ergo er den for længe i "mangler tilsyn" tilstand*/
            if (!rentalContracts.isEmpty()) {
                RentalContract latestContract = rentalContracts.get(0);
                LocalDate endDate = latestContract.getEnd_date().toLocalDate();
                if (endDate.isBefore(LocalDate.now().minusDays(days))) {
                    results.add(car);
                }
            }
        }
        return results;
    }
    public List<DamageReport> fetchAllDamageReports() {
        String sql = "SELECT damage_report_id, contract_id, total_repair_price FROM DamageReport";
        return this.jdbcTemplate.query(sql, RowMapperUtil.DAMAGE_REPORT_ROW_MAPPER);
    }

    public void deleteDamageReport(int contract_id) {
        this.jdbcTemplate.update("DELETE FROM DamageReport WHERE contract_id = ?", contract_id);
    }
}
