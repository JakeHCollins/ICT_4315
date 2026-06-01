package edu.du.ict4315.parking.charges.factory;

import edu.du.ict4315.parking.charges.decorator.DecoratorDesignatedDayCalculator;
import edu.du.ict4315.parking.charges.decorator.DecoratorPrimeTimeSurchargeCalculator;
import edu.du.ict4315.parking.charges.decorator.FlatRateCalculator;
import edu.du.ict4315.parking.charges.decorator.ParkingChargeCalculator;
import edu.du.ict4315.parking.charges.strategy.DesignatedDayNineToSix;
import edu.du.ict4315.parking.charges.strategy.ParkingChargeStrategy;

/*
 * Note that one advantage of a factory is that it has control over when the construction happens
 * (here at application load time)
 * The factory can take advantage of sharing objects when it makes sense.
 * Each factory can do this a different way.
 */

public class FactoryDesignatedDayNineToSix implements FactoryParkingCharges, FactoryParkingChargeCalculator {

  private static String summary = "Factory " + new DesignatedDayNineToSix().toString();
  @Override
  public ParkingChargeStrategy getStrategy() {
    return new DesignatedDayNineToSix();
  }

  @Override
  public String toString() {
    return summary;
  }

  @Override
  public ParkingChargeCalculator getCalculator() {
    return new DecoratorDesignatedDayCalculator(
        new DecoratorPrimeTimeSurchargeCalculator(
            new FlatRateCalculator(),9,18,0.25));
  }
}
