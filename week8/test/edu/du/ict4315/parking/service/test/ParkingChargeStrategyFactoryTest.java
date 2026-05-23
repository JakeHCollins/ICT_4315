package edu.du.ict4315.parking.service.test;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.*;
import edu.du.ict4315.parking.charges.decorator.ParkingChargeCalculator;
import edu.du.ict4315.parking.charges.factory.ParkingChargeCalculatorFactory;
import edu.du.ict4315.parking.charges.factory.PeakHourCalculatorFactory;
import edu.du.ict4315.parking.charges.factory.StandardCalculatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class ParkingChargeStrategyFactoryTest {

    private ParkingPermit compactPermit;
    private ParkingPermit suvPermit;
    private Money baseRate;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        Car compact = new Car(CarType.COMPACT, "AAA111", customer);
        Car suv = new Car(CarType.SUV, "BBB222", customer);
        compactPermit = new ParkingPermit("P0001", compact, LocalDateTime.now().plusYears(1));
        suvPermit = new ParkingPermit("P0002", suv, LocalDateTime.now().plusYears(1));
        baseRate = Money.of(10.00);
    }

    @Test
    void standardCalculatorFactory_returnsNewInstanceEachCall() {
        StandardCalculatorFactory factory = new StandardCalculatorFactory();
        ParkingChargeCalculator first = factory.getCalculator();
        ParkingChargeCalculator second = factory.getCalculator();
        assertNotNull(first);
        assertNotNull(second);
        assertNotSame(first, second);
    }

    @Test
    void peakHourCalculatorFactory_correctChargeViaParkingLot() {
        ParkingChargeCalculatorFactory factory = new PeakHourCalculatorFactory();
        ParkingLot lot = new ParkingLot("TEST", "Test Lot",
                new Address.Builder().build(), baseRate, factory);
        LocalDateTime peakTime = LocalDateTime.of(2025, 4, 15, 8, 0);
        Money charge = lot.getParkingCharges(suvPermit, peakTime);
        // 10.00 * 1.50 (peak) * no compact discount = 15.00
        assertEquals(15.00, charge.doubleValue(), 0.01);
    }

    @Test
    void standardCalculatorFactory_correctChargeViaParkingLot() {
        ParkingChargeCalculatorFactory factory = new StandardCalculatorFactory();
        ParkingLot lot = new ParkingLot("TEST", "Test Lot",
                new Address.Builder().build(), baseRate, factory);
        LocalDateTime saturday = LocalDateTime.of(2025, 4, 19, 12, 0);
        Money charge = lot.getParkingCharges(compactPermit, saturday);
        // 10.00 * 0.75 (weekend) * 0.80 (compact) = 6.00
        assertEquals(6.00, charge.doubleValue(), 0.01);
    }
}