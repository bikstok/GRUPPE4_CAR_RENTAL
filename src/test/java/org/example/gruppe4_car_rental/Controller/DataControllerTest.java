package org.example.gruppe4_car_rental.Controller;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

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
        // Opretter et eksempel på en udlejningskontrakt og en bil for at simulere testscenariet.
        int contractId = 1; // Kontrakt-ID for den udlejningskontrakt, der skal testes.
        RentalContract rentalContract = new RentalContract(
                contractId, "3456721098", "CDE3456789012", // Kunde-ID og bilens stelnummer.
                Date.valueOf(LocalDate.of(2024, 12, 1)),  // Kontraktens startdato.
                Date.valueOf(LocalDate.of(2025, 12, 31)), // Kontraktens slutdato.
                true, 20000, 4000, true, 10000            // Forskellige kontraktbetingelser (fx aktiv status, beløb mv.).
        );

        Car car = new Car(
                "CDE3456789012", "Tesla", "Model S",     // Stelnummer, mærke og model.
                "Solgt", "El", "Automatisk", 2023,       // Status, brændstoftype, gearkasse og årgang.
                500.0, 10000, 70000.0                    // Dagspris, kilometer og salgspris.
        );

        // Mock behavior
        // Definerer opførsel for mock-objekter, der simulerer dataService.
        when(dataService.getRentalContractFromContractId(contractId))
                .thenReturn(rentalContract); // Returnerer den oprettede udlejningskontrakt baseret på ID.
        when(dataService.getCarFromFrameNumber("CDE3456789012"))
                .thenReturn(car); // Returnerer bilen med stelnummeret "CDE3456789012".


        // Kalder metoden `createCarPurchase`, som vi tester, og sender kontrakt-ID og model som parametre.
        String viewName = dataController.createCarPurchase(contractId, model);


        // Verificerer, at metoden tilføjer den rigtige fejlmeddelelse til modellen.
        verify(model).addAttribute("message", "Denne bil er allerede solgt");

        // Sikrer, at metoden returnerer den forventede visningsside i tilfælde af fejl.
        assertEquals("showError", viewName);
    }

    @Test
    void testCreateCarPurchase_Calculation() {
        int contractId = 2; // Kontrakt-ID, som identificerer den udlejningskontrakt, der bruges i testen.


        // Opretter en udlejningskontrakt med nødvendige data som stelnummer, CPR-nummer og andre egenskaber.
        RentalContract rentalContract = new RentalContract(
                contractId, "3456721098", "CDE3456789012", // CPR-nummer og bilens stelnummer.
                Date.valueOf(LocalDate.of(2024, 12, 1)),  // Kontraktens startdato.
                Date.valueOf(LocalDate.of(2025, 12, 31)), // Kontraktens slutdato.
                true, 20000, 4000, true, 10000            // Detaljer som maksimale kilometer og startkilometer.
        );


        // Opretter en bil med specifikationer som årgang, pris og status.
        Car car = new Car(
                "CDE3456789012", "Tesla", "Model S",     // Stelnummer, mærke og model.
                "Lejet", "El", "Automatisk", 2020,       // Status, brændstoftype, gearkasse og årgang.
                500.0, 10000, 100000.0                   // Dagspris, kilometer og original pris.
        );


        // Opretter en kunde med CPR-nummer og øvrige personoplysninger.
        Customer customer = new Customer();
        customer.setCpr_number("3456721098");
        customer.setFirst_name("Lars");
        customer.setLast_name("Jensen");
        customer.setEmail("lars.jensen@example.com");
        customer.setPhone_number("12345678");
        customer.setAddress("Hovedgaden 1");
        customer.setZip_code("1000");

        // M
        // Simulerer dataService-opslag for kontrakt, bil og kunde.
        when(dataService.getRentalContractFromContractId(contractId)).thenReturn(rentalContract);
        when(dataService.getCarFromFrameNumber("CDE3456789012")).thenReturn(car);
        when(dataService.getCustomerFromCprNumber("3456721098")).thenReturn(customer);

        // C
        // Kalder metoden `createCarPurchase` med kontrakt-ID og model som parametre.
        dataController.createCarPurchase(contractId, model);

        //
        // Udfører de beregninger, som metoden forventes at lave, for at sammenligne resultaterne.
        int kilometersDriven = car.getOdometer() - rentalContract.getStart_odometer(); // 10000 - 4000 = 6000
        int excessKilometers = Math.max(kilometersDriven - rentalContract.getMax_km(), 0); // max(6000 - 20000, 0) = 0
        int years = LocalDate.now().getYear() - car.getYear_produced(); // 2024 - 2020 = 4
        double expectedPurchasePrice = (car.getOriginal_price() * Math.pow(0.9, years)) + (0.75 * excessKilometers);

        // Verify interactions
        // Fanger det oprettede `CarPurchase`-objekt for at kunne validere dets værdier.
        ArgumentCaptor<CarPurchase> carPurchaseCaptor = ArgumentCaptor.forClass(CarPurchase.class);
        verify(dataService).createCarPurchase(carPurchaseCaptor.capture(), eq(rentalContract), eq(car));


        // Sikrer, at den beregnede købspris matcher den forventede værdi.
        CarPurchase capturedCarPurchase = carPurchaseCaptor.getValue();
        assertNotNull(capturedCarPurchase); // Kontrollerer, at objektet ikke er null.
        assertEquals(Math.round(expectedPurchasePrice * 100.0) / 100.0, capturedCarPurchase.getPurchase_price());

        // Verify customer interaction
        // Bekræfter, at kunden blev hentet korrekt fra dataService.
        verify(dataService).getCustomerFromCprNumber("3456721098");
    }
}
