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

// Lots with this strategy are for larger vehicles with overnight stays.
// These lots are a little further out.
// 20% discount for SUV cars for the whole stay
// 30% off for late in (after 5 pm) for the whole stay
public class FavorLargeCarsLateEntry implements ParkingChargeStrategy {

    @Override
    public Money getParkingCharge(Money baseRate, LocalDateTime time,
            Duration duration, ParkingPermit p) {
        CarType carType = p.getCar().getType();
        Money dailyCharge = baseRate;
        Money charges = Money.times(dailyCharge, duration.toDays() + 1);
        // Discount SUV cars 20% for the whole stay
        if (carType == CarType.SUV) {
            charges = Money.div(Money.times(charges, 4), 5); // 4/5
        }
        // Discount late in (after 5 PM) by 30% for the whole stay
        if (time.getHour() >= 17) {
            charges = Money.div(Money.times(charges, 7), 10); // 7/10
        }

        return charges;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Favor large cars and late entry charging strategy (SUVs and after 5 PM)");
        return sb.toString();
    }

}
