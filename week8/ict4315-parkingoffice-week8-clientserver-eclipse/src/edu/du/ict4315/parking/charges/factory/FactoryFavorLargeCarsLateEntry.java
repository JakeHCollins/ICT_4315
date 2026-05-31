package edu.du.ict4315.parking.charges.factory;

import edu.du.ict4315.parking.charges.decorator.DecoratorLargeCarDiscountCalculator;
import edu.du.ict4315.parking.charges.decorator.DecoratorLateInCalculator;
import edu.du.ict4315.parking.charges.decorator.FlatRateCalculator;
import edu.du.ict4315.parking.charges.decorator.ParkingChargeCalculator;
import edu.du.ict4315.parking.charges.strategy.FavorLargeCarsLateEntry;
import edu.du.ict4315.parking.charges.strategy.ParkingChargeStrategy;

public class FactoryFavorLargeCarsLateEntry implements FactoryParkingCharges, FactoryParkingChargeCalculator  {

  /*
   * Note that one advantage of a factory is that it has control over when the construction happens
   * (here at application load time)
   * The factory can take advantage of sharing objects when it makes sense.
   */

  private static final ParkingChargeStrategy strategy = new FavorLargeCarsLateEntry();
  
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
    return new DecoratorLateInCalculator(new DecoratorLargeCarDiscountCalculator(new FlatRateCalculator()));
  }

}
