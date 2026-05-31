/*
 * File: FlatRate.java
 * Author: Instructor
 */
package edu.du.ict4315.parking.charges.strategy;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.ParkingPermit;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 *
 * @author michael
 */
public class FlatRate implements ParkingChargeStrategy {

    public FlatRate() {
    }

    @Override
    public Money getParkingCharge(Money baseRate, LocalDateTime time,
            Duration duration, ParkingPermit p) {
        Money result = baseRate;
        if (!(duration == null) && !duration.isNegative() && !duration.isZero()) {
            long days = ParkingDays.count(time, duration);
            if (days > 1L) {
                result = Money.times(result, days);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Flat Rate charging strategy";
    }

}
