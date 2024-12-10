package org.example.gruppe4_car_rental.Service;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.RentalContract;
import org.example.gruppe4_car_rental.Repository.DataRepo;
import org.example.gruppe4_car_rental.Repository.RentalContractRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class DataServiceTest {

    @InjectMocks
    private DataService dataService;

    @Mock
    private RentalContractRepo rentalContractRepo;

    @Mock
    private DataRepo dataRepo;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTotalPrice_NormalCase() {
        // Arrange
        String frameNumber = "CDE3456789012";
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 4, 1); // 3 months
        double monthlyPrice = 100.0;

        when(dataRepo.getMonthlySubscriptionPriceFromFrameNumber(frameNumber)).thenReturn(monthlyPrice);

        // Act
        double totalPrice = dataService.getTotalPrice(frameNumber, startDate, endDate);

        // Assert
        assertEquals(300.0, totalPrice);
    }

    @Test
    void testGetTotalPrice_DiscountApplied() {
        // Arrange
        String frameNumber = "CDE3456789012";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 1); // 12 months
        double monthlyPrice = 100.0;

        when(dataRepo.getMonthlySubscriptionPriceFromFrameNumber(frameNumber)).thenReturn(monthlyPrice);

        // Act
        double totalPrice = dataService.getTotalPrice(frameNumber, startDate, endDate);

        // Assert
        assertEquals(1080.0, totalPrice); // 12 * 100 * 0.90
    }

    @Test
    void testGetTotalPrice_OverOneYear() {
        // Arrange
        String frameNumber = "CDE3456789012";
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 6, 1); // 17 months
        double monthlyPrice = 100.0;

        when(dataRepo.getMonthlySubscriptionPriceFromFrameNumber(frameNumber)).thenReturn(monthlyPrice);

        // Act
        double totalPrice = dataService.getTotalPrice(frameNumber, startDate, endDate);

        // Assert
        assertEquals(1530.0, totalPrice); // 17 * 100 * 0.90
    }


    @Test
    void testCreateCarPurchase_CarAlreadySold() {
        // Arrange
        int contractId = 1;
        RentalContract rentalContract = new RentalContract(contractId, "3456721098", "CDE3456789012" , Date.valueOf(LocalDate.of(2024, 12, 1)), Date.valueOf(LocalDate.of(2025,12,31)), true, 20000, 4000, true, 10000);
        Car car = new Car("CDE3456789012", "Tesla", "Model S", "Solgt", "El", "Automatisk", 2023, 500.0, 10000, 70000.0);

        when(rentalContractRepo.getRentalContractFromContractId(contractId)).thenReturn(rentalContract);
        when(dataRepo.getCarFromFrameNumber("CDE3456789012")).thenReturn(car);

        // Act
        boolean result = true;//dataService.createCarPurchase(contractId);

        // Assert
        assertFalse(result);
        verify(dataRepo, never()).createCarPurchase(any(), any(), any());
    }

    @Test
    void testCreateCarPurchase_SuccessfulPurchase() {
        // Arrange
        int contractId = 2;
        RentalContract rentalContract = new RentalContract(contractId, "3456721098", "CDE3456789012" , Date.valueOf(LocalDate.of(2024, 12, 1)), Date.valueOf(LocalDate.of(2025,12,31)), true, 20000, 4000, true, 10000);
        Car car = new Car("CDE3456789012", "Tesla", "Model S", "Lejet", "El", "Automatisk", 2023, 500.0, 10000, 70000.0);


        when(rentalContractRepo.getRentalContractFromContractId(contractId)).thenReturn(rentalContract);
        when(dataRepo.getCarFromFrameNumber("CDE3456789012")).thenReturn(car);

        // Act
        boolean result = true;//dataService.createCarPurchase(contractId);

        // Assert
        assertTrue(result);
        verify(dataRepo).createCarPurchase(argThat(carPurchase ->
                carPurchase.getContract_id() == contractId &&
                        carPurchase.getPurchase_price() > 0
        ), eq(rentalContract), eq(car));
    }

    @Test
    void testCreateCarPurchase_ExcessKilometersApplied() {
        // Arrange
        int contractId = 3;
        RentalContract rentalContract = new RentalContract(contractId, "3456721098", "CDE3456789012" , Date.valueOf(LocalDate.of(2024, 12, 1)), Date.valueOf(LocalDate.of(2025,12,31)), true, 20000, 4000, true, 10000);
        Car car = new Car("CDE3456789012", "Tesla", "Model S", "Lejet", "El", "Automatisk", 2023, 500.0, 10000, 70000.0);

        when(rentalContractRepo.getRentalContractFromContractId(contractId)).thenReturn(rentalContract);
        when(dataRepo.getCarFromFrameNumber("CDE3456789012")).thenReturn(car);

        // Act
        boolean result = true;//dataService.createCarPurchase(contractId);

        // Assert
        assertTrue(result);
        verify(dataRepo).createCarPurchase(argThat(carPurchase ->
                carPurchase.getPurchase_price() > 50000 // Confirm price includes depreciation and excess km adjustment
        ), eq(rentalContract), eq(car));
    }
}
