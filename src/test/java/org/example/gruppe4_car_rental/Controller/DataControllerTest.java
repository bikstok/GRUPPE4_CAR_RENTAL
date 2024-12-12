package org.example.gruppe4_car_rental.Controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.gruppe4_car_rental.Model.Car;
import org.example.gruppe4_car_rental.Model.CarPurchase;
import org.example.gruppe4_car_rental.Model.Customer;
import org.example.gruppe4_car_rental.Model.RentalContract;
import org.example.gruppe4_car_rental.Service.DataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.sql.Date;
import java.time.LocalDate;
class DataControllerTest {

    @Mock
    private DataService dataService;

    @Mock
    private Model model;

    @InjectMocks
    private DataController dataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCarPurchase_CarAlreadySold() {
        // Test data setup
        int contractId = 1;
        RentalContract rentalContract = new RentalContract(
                contractId, "3456721098", "CDE3456789012",
                Date.valueOf(LocalDate.of(2024, 12, 1)),
                Date.valueOf(LocalDate.of(2025, 12, 31)),
                true, 20000, 4000, true, 10000
        );

        Car car = new Car(
                "CDE3456789012", "Tesla", "Model S",
                "Solgt", "El", "Automatisk", 2023,
                500.0, 10000, 70000.0
        );

        // Mock behavior
        when(dataService.getRentalContractFromContractId(contractId)).thenReturn(rentalContract);
        when(dataService.getCarFromFrameNumber("CDE3456789012")).thenReturn(car);

        // Call the method under test
        String viewName = dataController.createCarPurchase(contractId, model);

        // Verify and assert
        verify(model).addAttribute("message", "Denne bil er allerede solgt");
        assertEquals("dataregistrering/error", viewName);
    }

    @Test
    void testCreateCarPurchase_Calculation() {
        int contractId = 2;

        // Setup RentalContract
        RentalContract rentalContract = new RentalContract(
                contractId, "3456721098", "CDE3456789012",
                Date.valueOf(LocalDate.of(2024, 12, 1)),
                Date.valueOf(LocalDate.of(2025, 12, 31)),
                true, 20000, 4000, true, 10000
        );

        // Setup Car
        Car car = new Car(
                "CDE3456789012", "Tesla", "Model S",
                "Lejet", "El", "Automatisk", 2020,
                500.0, 10000, 100000.0
        );

        // Setup Customer
        Customer customer = new Customer();
        customer.setCpr_number("3456721098");
        customer.setFirst_name("Lars");
        customer.setLast_name("Jensen");
        customer.setEmail("lars.jensen@example.com");
        customer.setPhone_number("12345678");
        customer.setAddress("Hovedgaden 1");
        customer.setZip_code("1000");

        // Mock behaviors
        when(dataService.getRentalContractFromContractId(contractId)).thenReturn(rentalContract);
        when(dataService.getCarFromFrameNumber("CDE3456789012")).thenReturn(car);
        when(dataService.getCustomerFromCprNumber("3456721098")).thenReturn(customer);

        // Call the method under test
        dataController.createCarPurchase(contractId, model);

        // Calculate expected values
        int kilometersDriven = car.getOdometer() - rentalContract.getStart_odometer(); // 10000 - 4000 = 6000
        int excessKilometers = Math.max(kilometersDriven - rentalContract.getMax_km(), 0); // max(6000 - 20000, 0) = 0
        int years = LocalDate.now().getYear() - car.getYear_produced(); // 2024 - 2020 = 4
        double expectedPurchasePrice = (car.getOriginal_price() * Math.pow(0.9, years)) + (0.75 * excessKilometers);

        // Verify interactions
        ArgumentCaptor<CarPurchase> carPurchaseCaptor = ArgumentCaptor.forClass(CarPurchase.class);
        verify(dataService).createCarPurchase(carPurchaseCaptor.capture(), eq(rentalContract), eq(car));

        // Assert calculation
        CarPurchase capturedCarPurchase = carPurchaseCaptor.getValue();
        assertNotNull(capturedCarPurchase);
        assertEquals(Math.round(expectedPurchasePrice * 100.0) / 100.0, capturedCarPurchase.getPurchase_price());

        // Verify customer interaction
        verify(dataService).getCustomerFromCprNumber("3456721098");
    }
}
