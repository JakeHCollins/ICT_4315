package edu.du.ict4315.parking.charges.factory;

// Class: ICT4315
// File: PrimeTimeSurchargeNoWeekendCharge.java
// Author: Instructor
import edu.du.ict4315.parking.charges.decorator.DecoratorDayOfWeekFreeCalculator;
import edu.du.ict4315.parking.charges.decorator.DecoratorPrimeTimeSurchargeCalculator;
import edu.du.ict4315.parking.charges.decorator.FlatRateCalculator;
import edu.du.ict4315.parking.charges.decorator.ParkingChargeCalculator;
import edu.du.ict4315.parking.charges.strategy.ParkingChargeStrategy;
import edu.du.ict4315.parking.charges.strategy.PrimeTimeSurchargeNoWeekendCharge;

/*
 * Note that one advantage of a factory is that it has control over when the construction happens
 * (here at application load time)
 * The factory can take advantage of sharing objects when it makes sense.
 */
public class FactoryPrimeTimeSurchargeNoWeekendCharge implements FactoryParkingCharges, FactoryParkingChargeCalculator {

    private static final ParkingChargeStrategy strategy = new PrimeTimeSurchargeNoWeekendCharge();

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
        return new DecoratorPrimeTimeSurchargeCalculator(
                 new DecoratorDayOfWeekFreeCalculator(
                   new FlatRateCalculator()));
    }

}
