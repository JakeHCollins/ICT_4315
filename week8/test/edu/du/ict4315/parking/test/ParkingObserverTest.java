package edu.du.ict4315.parking;

import edu.du.ict4315.currency.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParkingObserverTest {

    private RealParkingOffice office;
    private TransactionManager transactionManager;
    private ParkingLot lot;
    private ParkingPermit compactPermit;
    private ParkingPermit suvPermit;
    private Customer customer;

    @BeforeEach
    void setUp() {
        office = new RealParkingOffice();
        transactionManager = new TransactionManager(office);

        Address address = new Address.Builder()
                .withStreetAddress1("123 Test St")
                .withCity("Denver")
                .withState("CO")
                .withZip("80210")
                .build();

        lot = new ParkingLot("T1", "Test Lot", address, Money.of(10.00));

        customer = new Customer();
        customer.setFirstName("Jane");
        customer.setLastName("Doe");

        Car compact = new Car(CarType.COMPACT, "AAA111", customer);
        Car suv = new Car(CarType.SUV, "BBB222", customer);

        compactPermit = new ParkingPermit("P0001", compact,
                java.time.LocalDateTime.now().plusYears(1));
        suvPermit = new ParkingPermit("P0002", suv,
                java.time.LocalDateTime.now().plusYears(1));

        new ParkingObserver(transactionManager, List.of(lot));
    }

    @Test
    void enterCreatesTransaction() {
        lot.enter(compactPermit);
        Money charges = transactionManager.getParkingCharges(compactPermit);
        assertNotNull(charges);
        assertEquals(true, charges.more(Money.of(0.0)));
    }

    @Test
    void exitDoesNotCreateTransaction() {
        lot.exit(compactPermit);
        Money charges = transactionManager.getParkingCharges(compactPermit);
        assertEquals(Money.of(0.0), charges);
    }

    @Test
    void multipleObserversEachReceivesEvent() {
        TransactionManager secondManager = new TransactionManager(office);
        new ParkingObserver(secondManager, List.of(lot));

        lot.enter(suvPermit);

        Money firstCharges = transactionManager.getParkingCharges(suvPermit);
        Money secondCharges = secondManager.getParkingCharges(suvPermit);

        assertNotNull(firstCharges);
        assertNotNull(secondCharges);
        assertEquals(firstCharges, secondCharges);
    }
}