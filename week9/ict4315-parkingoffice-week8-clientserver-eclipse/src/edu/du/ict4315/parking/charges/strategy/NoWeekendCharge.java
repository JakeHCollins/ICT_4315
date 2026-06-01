/*
 * Course ICT4315
 * Author: Instructor
 */
package edu.du.ict4315.parking.charges.strategy;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.ParkingPermit;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * If time is Saturday or Sunday, charge is zero Otherwise, it follows the
 * original algorithm
 *
 * @author michael
 */
public class NoWeekendCharge implements ParkingChargeStrategy {

    ParkingChargeStrategy original = new OriginalAlgorithm();

    private static final Logger logger = Logger.getLogger(NoWeekendCharge.class.getName());

    private int addDay(LocalDateTime time) {
        int result = 0;
        switch (time.getDayOfWeek()) {
            case SATURDAY:
                break;
            case SUNDAY:
                break;
            default:
                result = 1;
                break;
        }
        logger.info("Checking " + time + " (" + result + ")");
        return result;
    }

    @Override
    public Money getParkingCharge(Money baseRate, LocalDateTime time, Duration duration,
            ParkingPermit p) {
        Money result = Money.of(0.0);
        long days = ParkingDays.count(time, duration);
        long numDays = 0;
        for (int i = 0; i < days; i++) {
            numDays += addDay(time.plusDays(i));
        }

        if (numDays > 0) {
            result = Money.times(original.getParkingCharge(baseRate, time, Duration.ofHours(1), p),
                    numDays);
        }
        logger.info("Charging " + result + " for " + numDays + " days (" + p + ")");

        return result;
    }

    @Override
    public String toString() {
        return this.getClass().getName();
    }

}
