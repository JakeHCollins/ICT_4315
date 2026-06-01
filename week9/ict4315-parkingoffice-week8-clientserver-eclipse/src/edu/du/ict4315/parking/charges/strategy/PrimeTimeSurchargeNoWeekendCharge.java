/*
 * Course ICT4315
 * Author: Instructor
 */
package edu.du.ict4315.parking.charges.strategy;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.ParkingPermit;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 *
 * @author michael
 */
public class PrimeTimeSurchargeNoWeekendCharge implements ParkingChargeStrategy {

    private ParkingChargeStrategy algorithm = new NoWeekendCharge();

    @Override
    public Money getParkingCharge(Money baseRate, LocalDateTime time, Duration duration,
            ParkingPermit permit) {
        Money result = Money.of(0.0);
        LocalDateTime beginningOfDay = time.toLocalDate().atStartOfDay();
        result = algorithm.getParkingCharge(baseRate, beginningOfDay, duration, permit);

        // Prime time surcharge (25%) between 10 AM and 4 PM on the first day    
        if (time.getDayOfWeek() != DayOfWeek.SATURDAY
                && time.getDayOfWeek() != DayOfWeek.SUNDAY
                && time.getHour() >= 10
                && time.getHour() < 16) {
            Money surcharge = algorithm.getParkingCharge(baseRate, time, Duration.ofDays(1L), permit);
            result = Money.add(result, Money.div(surcharge, 4));
        }

        return result;
    }

    @Override
    public String toString() {
        return this.getClass().getName();
    }
}
