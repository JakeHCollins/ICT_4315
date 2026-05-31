/*
 * File: OriginalAlgorithm.java
 * Author: Instructor
 */
package edu.du.ict4315.parking.charges.strategy;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.CarType;
import edu.du.ict4315.parking.ParkingPermit;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Charge baseRate if SUV, 20% off if COMPACT
 *
 * @author michael
 */
public class OriginalAlgorithm implements ParkingChargeStrategy {

    public OriginalAlgorithm() {
    }

    @Override
    public Money getParkingCharge(Money baseRate, LocalDateTime time,
            Duration duration, ParkingPermit permit) {
        Money amount = baseRate;
        CarType carType = permit.getCar().getType();
        switch (carType) {
            case COMPACT:
                amount = Money.div(Money.times(baseRate, 4), 5); // 4/5 == 0.8, 20% off
                break;
            default:
                break;
        }
        if (duration != null && !duration.isZero() && !duration.isNegative()) {
            long days = ParkingDays.count(time, duration);
            if (days > 1L) {
                amount = Money.times(amount, days);
            }
        }
        return amount;
    }

    @Override
    public String toString() {
        return "Original charging strategy: 20% off for COMPACT cars";
    }

}
