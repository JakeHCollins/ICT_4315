package edu.du.ict4315.parking.service.test;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.*;
import edu.du.ict4315.parking.charges.factory.ParkingChargeStrategyFactory;
import edu.du.ict4315.parking.charges.factory.PeakHourSurchargeSUVStrategyFactory;
import edu.du.ict4315.parking.charges.factory.WeekdayVehicleTypeStrategyFactory;
import edu.du.ict4315.parking.charges.strategy.ParkingChargeStrategy;
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
    void weekdayVehicleTypeStrategyFactory_returnsNewInstanceEachCall() {
        WeekdayVehicleTypeStrategyFactory factory = new WeekdayVehicleTypeStrategyFactory();
        ParkingChargeStrategy first = factory.getStrategy();
        ParkingChargeStrategy second = factory.getStrategy();
        assertNotNull(first);
        assertNotNull(second);
        assertNotSame(first, second);
    }

    @Test
    void peakHourSurchargeSUVStrategyFactory_correctChargeViaParkingLot() {
        ParkingChargeStrategyFactory factory = new PeakHourSurchargeSUVStrategyFactory();
        ParkingLot lot = new ParkingLot("TEST", "Test Lot",
                new Address.Builder().build(), baseRate, factory);
        LocalDateTime peakTime = LocalDateTime.of(2025, 4, 15, 8, 0);
        Money charge = lot.getParkingCharges(suvPermit, peakTime);
        assertEquals(18.75, charge.doubleValue(), 0.01);
    }

    @Test
    void weekdayVehicleTypeStrategyFactory_correctChargeViaParkingLot() {
        ParkingChargeStrategyFactory factory = new WeekdayVehicleTypeStrategyFactory();
        ParkingLot lot = new ParkingLot("TEST", "Test Lot",
                new Address.Builder().build(), baseRate, factory);
        LocalDateTime saturday = LocalDateTime.of(2025, 4, 19, 12, 0);
        Money charge = lot.getParkingCharges(compactPermit, saturday);
        assertEquals(6.75, charge.doubleValue(), 0.01);
    }
}