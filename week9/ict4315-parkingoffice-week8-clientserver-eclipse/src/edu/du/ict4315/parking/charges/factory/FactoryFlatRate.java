package edu.du.ict4315.parking.charges.factory;

import edu.du.ict4315.parking.charges.decorator.FlatRateCalculator;
import edu.du.ict4315.parking.charges.decorator.ParkingChargeCalculator;
import edu.du.ict4315.parking.charges.strategy.FlatRate;
import edu.du.ict4315.parking.charges.strategy.ParkingChargeStrategy;

/*
 * Note that one advantage of a factory is that it has control over when the construction happens
 * (here at application load time)
 * The factory can take advantage of sharing objects when it makes sense.
 */
public class FactoryFlatRate implements FactoryParkingCharges, FactoryParkingChargeCalculator  {

  private static FlatRate strategy = new FlatRate();
  
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
    return new FlatRateCalculator();
  }
  

}
