/*
 * Course: ICT
 * File: 
 * Author: Instructor
 */
package edu.du.ict4315.parking.charges.strategy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * Creates a common way to count parking days and parking hours (not currently
 * used)
 *
 * @author michael
 */
public class ParkingDays {

    private static final Logger logger = Logger.getLogger(ParkingDays.class.getName());

    // Number of days to charge for parking
    public static int count(LocalDateTime inTime, Duration duration) {
        int result;
        LocalDateTime outTime = inTime.plus(duration);
        Duration diff = Duration.between(inTime.toLocalDate().atStartOfDay(),
                outTime.toLocalDate().atStartOfDay()) // .minusMinutes(1) // Avoid time boundary
                ;
        result = (int) diff.toDays() + 1;

        return result;
    }

    // Number of hours to charge for parking, rounds up.
    // Less than five minutes returns a zero
    public static int hours(LocalDateTime inTime, Duration duration) {
        return (int) (duration.plusMinutes(55L)).toHours();
    }
}
