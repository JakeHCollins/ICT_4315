package edu.du.ict4315.parking.charges.factory;

import edu.du.ict4315.parking.charges.decorator.DecoratorCompactCarDiscountCalculator;
import edu.du.ict4315.parking.charges.decorator.DecoratorWeekendEntryFreeCalculator;
import edu.du.ict4315.parking.charges.decorator.FlatRateCalculator;
import edu.du.ict4315.parking.charges.decorator.ParkingChargeCalculator;
import edu.du.ict4315.parking.charges.strategy.OriginalAlgorithm;
import edu.du.ict4315.parking.charges.strategy.ParkingChargeStrategy;

/*
 * Note that one advantage of a factory is that it has control over when the construction happens
 * (here at application load time)
 * The factory can take advantage of sharing objects when it makes sense.
 */
public class FactoryOriginalAlgorithm implements FactoryParkingCharges, FactoryParkingChargeCalculator {

  private static ParkingChargeStrategy strategy = new OriginalAlgorithm();
  
  @Override
  public ParkingChargeStrategy getStrategy() {
    return strategy;
  }

  @Override
  public String toString() {
    return "Factory: "+strategy.toString();
  }

  @Override
  public ParkingChargeCalculator getCalculator() {
    return new DecoratorCompactCarDiscountCalculator(
                    new FlatRateCalculator());
  }

}
