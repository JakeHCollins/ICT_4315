package edu.du.ict4315.parking.test;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.Address;
import edu.du.ict4315.parking.Car;
import edu.du.ict4315.parking.CarType;
import edu.du.ict4315.parking.Customer;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingPermit;
import edu.du.ict4315.parking.charges.decorator.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParkingChargeCalculatorTest {

    private ParkingLot lot;
    private ParkingPermit compactPermit;
    private ParkingPermit suvPermit;

    @BeforeEach
    void setUp() {
        Address address = new Address.Builder()
                .withStreetAddress1("123 Test St")
                .withCity("Denver")
                .withState("CO")
                .withZip("80210")
                .build();
        lot = new ParkingLot("T1", "Test Lot", address, Money.of(10.00));

        Customer customer = new Customer();
        Car compact = new Car(CarType.COMPACT, "AAA111", customer);
        Car suv = new Car(CarType.SUV, "BBB222", customer);
        compactPermit = new ParkingPermit("P0001", compact, LocalDateTime.now().plusYears(1));
        suvPermit = new ParkingPermit("P0002", suv, LocalDateTime.now().plusYears(1));
    }

    @Test
    void flatRate_suv_returnsBaseRate() {
        ParkingChargeCalculator calc = new FlatRateCalculator();
        Money charge = calc.getParkingCharge(LocalDateTime.now(), lot, suvPermit);
        assertEquals(10.00, charge.doubleValue(), 0.01);
    }

    @Test
    void compactCarDiscount_compact_appliesTwentyPercentDiscount() {
        ParkingChargeCalculator calc = new CompactCarDiscountDecorator(new FlatRateCalculator());
        Money charge = calc.getParkingCharge(LocalDateTime.now(), lot, compactPermit);
        assertEquals(8.00, charge.doubleValue(), 0.01);
    }

    @Test
    void compactCarDiscount_suv_noDiscount() {
        ParkingChargeCalculator calc = new CompactCarDiscountDecorator(new FlatRateCalculator());
        Money charge = calc.getParkingCharge(LocalDateTime.now(), lot, suvPermit);
        assertEquals(10.00, charge.doubleValue(), 0.01);
    }

    @Test
    void weekendDiscount_saturday_appliesDiscount() {
        ParkingChargeCalculator calc = new WeekendDiscountDecorator(new FlatRateCalculator());
        LocalDateTime saturday = LocalDateTime.of(2025, 4, 19, 12, 0);
        Money charge = calc.getParkingCharge(saturday, lot, suvPermit);
        assertEquals(7.50, charge.doubleValue(), 0.01);
    }

    @Test
    void peakHourSurcharge_peakTime_appliesSurcharge() {
        ParkingChargeCalculator calc = new PeakHourSurchargeDecorator(new FlatRateCalculator());
        LocalDateTime peak = LocalDateTime.of(2025, 4, 15, 8, 0);
        Money charge = calc.getParkingCharge(peak, lot, suvPermit);
        assertEquals(15.00, charge.doubleValue(), 0.01);
    }

    @Test
    void compactAndWeekend_compact_saturday_bothApplied() {
        ParkingChargeCalculator calc = new CompactCarDiscountDecorator(
                new WeekendDiscountDecorator(new FlatRateCalculator()));
        LocalDateTime saturday = LocalDateTime.of(2025, 4, 19, 12, 0);
        Money charge = calc.getParkingCharge(saturday, lot, compactPermit);
        assertEquals(6.00, charge.doubleValue(), 0.01);
    }

    @Test
    void peakAndCompact_compact_peakTime_bothApplied() {
        ParkingChargeCalculator calc = new PeakHourSurchargeDecorator(
                new CompactCarDiscountDecorator(new FlatRateCalculator()));
        LocalDateTime peak = LocalDateTime.of(2025, 4, 15, 8, 0);
        Money charge = calc.getParkingCharge(peak, lot, compactPermit);
        assertEquals(12.00, charge.doubleValue(), 0.01);
    }
}