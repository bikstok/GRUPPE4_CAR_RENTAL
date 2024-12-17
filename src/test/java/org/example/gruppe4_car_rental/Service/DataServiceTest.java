package org.example.gruppe4_car_rental.Service;

import org.example.gruppe4_car_rental.Repository.DataRepo;
import org.example.gruppe4_car_rental.Repository.RentalContractRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class DataServiceTest {

    @InjectMocks
    private DataService dataService;

    @Mock
    private RentalContractRepo rentalContractRepo;

    @Mock
    private DataRepo dataRepo;

    @BeforeEach
    void setup() {
        // Initialiserer mocks og injecter dem i dataService før hver test.
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTotalPrice_NormalCase() {
        //  Sæt testens inputdata og forventede resultater
        String frameNumber = "CDE3456789012";
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 4, 1); // 3 måneder
        double monthlyPrice = 100.0;

        //  Simulér opførsel af dataRepo ved at returnere en fast månedlig pris
        when(dataRepo.getMonthlySubscriptionPriceFromFrameNumber(frameNumber)).thenReturn(monthlyPrice);

        //  Beregn den samlede pris
        double totalPrice = dataService.getTotalPrice(frameNumber, startDate, endDate);

        //  Verificér, at den samlede pris er korrekt beregnet
        assertEquals(300.0, totalPrice); // 3 * 100
    }

    @Test
    void testGetTotalPrice_DiscountApplied() {
        //  Opsæt testdata for et leje, der varer præcis 12 måneder (med rabat)
        String frameNumber = "CDE3456789012";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 1); // 12 måneder
        double monthlyPrice = 100.0;

        // Returnér månedlig pris fra dataRepo
        when(dataRepo.getMonthlySubscriptionPriceFromFrameNumber(frameNumber)).thenReturn(monthlyPrice);

        // Beregn totalprisen, som skal inkludere en rabat (10%)
        double totalPrice = dataService.getTotalPrice(frameNumber, startDate, endDate);

        // Verificér den korrekte rabatberegning
        assertEquals(1080.0, totalPrice); // 12 * 100 * 0.90
    }

    @Test
    void testGetTotalPrice_OverOneYear() {
        //  Test for et leje, der varer mere end ét år (17 måneder)
        String frameNumber = "CDE3456789012";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 6, 1); // 17 måneder
        double monthlyPrice = 100.0;

        //  Returnér månedlig pris fra dataRepo
        when(dataRepo.getMonthlySubscriptionPriceFromFrameNumber(frameNumber)).thenReturn(monthlyPrice);

        //  Beregn totalprisen med rabat
        double totalPrice = dataService.getTotalPrice(frameNumber, startDate, endDate);

        //  Kontroller, at rabatten anvendes korrekt
        assertEquals(1530.0, totalPrice); // 17 * 100 * 0.90
    }
}
