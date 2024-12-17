package org.example.gruppe4_car_rental.Repository;

import org.example.gruppe4_car_rental.Model.RentalContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/*Primært bilstatus og ændringer heraf.*/
@Repository
public class HomeRepo {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void changeStatusOfCarsToRented(List<RentalContract> activeRentalContracts) {
        for (RentalContract rentalContract : activeRentalContracts) {
            this.jdbcTemplate.update("UPDATE Cars SET car_status = 'Lejet' WHERE frame_number = ? AND car_status = 'Ledig';",
                    rentalContract.getFrame_number()
            );
        }
    }

    public void changeStatusOfCarsToPendingInspection(List<RentalContract> allRentalContracts) {
        LocalDate now = LocalDate.now();
        for (RentalContract rentalContract : allRentalContracts) {
            if (now.isAfter(rentalContract.getEnd_date().toLocalDate())) {
                String sql = "SELECT COUNT(*) FROM DamageReport WHERE contract_id = ?";
                Integer count = this.jdbcTemplate.queryForObject(sql, new Object[]{rentalContract.getContract_id()}, Integer.class);
                if (count == null || count <= 0) { //Hvis der ikke en skadesrapport
                    this.jdbcTemplate.update("UPDATE Cars SET car_status = 'Mangler tilsyn' WHERE frame_number = ? AND car_status = 'Lejet';",
                            rentalContract.getFrame_number()
                    );
                }
            }
        }
    }
}
