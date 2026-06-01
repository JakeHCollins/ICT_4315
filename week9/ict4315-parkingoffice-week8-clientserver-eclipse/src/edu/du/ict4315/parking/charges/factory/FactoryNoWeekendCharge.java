package edu.du.ict4315.parking.charges.factory;

// Class: ICT4315
// File: NoWeekendCharge.java
// Author: Instructor
import edu.du.ict4315.parking.charges.decorator.DecoratorCompactCarDiscountCalculator;
import edu.du.ict4315.parking.charges.decorator.DecoratorDayOfWeekFreeCalculator;
import edu.du.ict4315.parking.charges.decorator.DecoratorWeekendEntryFreeCalculator;
import edu.du.ict4315.parking.charges.decorator.FlatRateCalculator;
import edu.du.ict4315.parking.charges.decorator.ParkingChargeCalculator;
import edu.du.ict4315.parking.charges.strategy.ParkingChargeStrategy;
import edu.du.ict4315.parking.charges.strategy.NoWeekendCharge;
import java.time.DayOfWeek;

/*
 * Note that one advantage of a factory is that it has control over when the construction happens
 * (here at application load time)
 * The factory can take advantage of sharing objects when it makes sense.
 */
public class FactoryNoWeekendCharge implements FactoryParkingCharges, FactoryParkingChargeCalculator {

    private static final ParkingChargeStrategy strategy = new NoWeekendCharge();

    @Override
    public ParkingChargeStrategy getStrategy() {
        return strategy;
    }

    @Override
    public String toString() {
        return "Factory: " + strategy.toString();
    }

    @Override
    public ParkingChargeCalculator getCalculator() {
        DayOfWeek[] days = { DayOfWeek.SATURDAY, DayOfWeek.SUNDAY };
        return new DecoratorDayOfWeekFreeCalculator(
                new DecoratorCompactCarDiscountCalculator(
                    new FlatRateCalculator()),days);
    }

}
