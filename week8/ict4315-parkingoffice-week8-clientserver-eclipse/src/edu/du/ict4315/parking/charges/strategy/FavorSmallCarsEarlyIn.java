package edu.du.ict4315.parking.charges.strategy;

/*
 * This is one parking charge strategy implementation
 * Author: M I Schwartz
 */
import java.time.LocalDateTime;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.CarType;
import edu.du.ict4315.parking.ParkingPermit;
import java.time.Duration;

// Lots with this strategy are for smaller cars commuting in the morning
// 20% discount for COMPACT cars for the whole stay
// 10% off for early in (before 8 am) for the whole stay
public class FavorSmallCarsEarlyIn implements ParkingChargeStrategy {

    @Override
    public Money getParkingCharge(Money baseRate, LocalDateTime in,
            Duration duration, ParkingPermit p) {
        CarType carType = p.getCar().getType();
        Money dailyCharge = baseRate;
        Money charges = Money.times(dailyCharge, duration.toDays() + 1);
        // Discount small cars 20%
        if (carType == CarType.COMPACT) {
            charges = Money.div(Money.times(charges, 4), 5); // 4/5
        }
        // Discount early in (by 8 AM) by 10%
        if (in.getHour() < 8) {
            charges = Money.div(Money.times(charges, 9), 10); // 9/10
        }

        return charges;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Favor small cars and early entry charging strategy (Compact cars and before 8 AM");
        return sb.toString();
    }

}
