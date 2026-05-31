/*
 * File: ParkingChargeStrategy.java
 * Author: Instructor
 */
package edu.du.ict4315.parking.charges.strategy;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.ParkingPermit;
import java.time.Duration;
import java.time.LocalDateTime;

/*
 * Note on how to proceed:
 *   1) Create this interface to return charges to the caller (Money)
 *   2) Create a strategy class that represents what we do now.
 *      * Base rate for the lot, with 20% discount for COMPACT cars
 *   3) Update ParkingLot to use the strategy (assigned to the original algorithm)
 *      * If this is done correctly, your JUnit tests should all pass with no change.
 *   4) Create alternate strategies
 *      * First, create and run JUnit tests that check the computations of the strategies
 *      * Then you will need to modify existing JUnit tests to account for the strategy.
 */
/**
 * This interface should represent a method for computing parking charges
 * representing all the information that should be considered. How the
 * information is used is completely internal to each strategy.
 *
 * The information present in our application for parking include: the date and
 * time parking started and/or finished The permit (which can lead back to the
 * Customer and Car) The base rate of the lot (or the lot itself if needed)
 *
 * This interface is written assuming the charges accrue on entry; A Duration,
 * or a pair of date/times, may be appropriate if charges accrue on exit
 *
 * Individual strategies are also stored in this package.
 *
 * @author michael
 */
public interface ParkingChargeStrategy {

    public Money getParkingCharge(Money baseRate, LocalDateTime time,
            Duration duration, ParkingPermit p);
}
