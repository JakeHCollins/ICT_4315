package edu.du.ict4315.parking.service.test;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.Car;
import edu.du.ict4315.parking.CarType;
import edu.du.ict4315.parking.Customer;
import edu.du.ict4315.parking.ParkingPermit;
import edu.du.ict4315.parking.charges.strategy.PeakHourSurchargeSUVStrategy;
import edu.du.ict4315.parking.charges.strategy.WeekdayVehicleTypeStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParkingChargeStrategyTest {

    private ParkingPermit compactPermit;
    private ParkingPermit suvPermit;
    private Money baseRate;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer();
        Car compact = new Car(CarType.COMPACT, "AAA111", customer);
        Car suv = new Car(CarType.SUV, "BBB222", customer);
        compactPermit = new ParkingPermit("P0001", compact, LocalDateTime.now().plusYears(1));
        suvPermit = new ParkingPermit("P0002", suv, LocalDateTime.now().plusYears(1));
        baseRate = Money.of(10.00);
    }

    @Test
    void weekdayVehicleTypeStrategy_weekendCompact_appliesBothDiscounts() {
        WeekdayVehicleTypeStrategy strategy = new WeekdayVehicleTypeStrategy();
        LocalDateTime saturday = LocalDateTime.of(2025, 4, 19, 12, 0);
        Money charge = strategy.calculateCharge(baseRate, compactPermit, saturday);
        // 10.00 * 0.75 (weekend) * 0.90 (compact) = 6.75
        assertEquals(6.75, charge.doubleValue(), 0.01);
    }

    @Test
    void peakHourSurchargeSUVStrategy_peakHourSUV_appliesBothSurcharges() {
        PeakHourSurchargeSUVStrategy strategy = new PeakHourSurchargeSUVStrategy();
        LocalDateTime peakTime = LocalDateTime.of(2025, 4, 15, 8, 0);
        Money charge = strategy.calculateCharge(baseRate, suvPermit, peakTime);
        // 10.00 * 1.50 (peak) * 1.25 (SUV) = 18.75
        assertEquals(18.75, charge.doubleValue(), 0.01);
    }

    @Test
    void peakHourSurchargeSUVStrategy_offPeakCompact_noSurcharge() {
        PeakHourSurchargeSUVStrategy strategy = new PeakHourSurchargeSUVStrategy();
        LocalDateTime offPeak = LocalDateTime.of(2025, 4, 15, 14, 0);
        Money charge = strategy.calculateCharge(baseRate, compactPermit, offPeak);
        // 10.00 — no peak, no SUV surcharge
        assertEquals(10.00, charge.doubleValue(), 0.01);
    }
}