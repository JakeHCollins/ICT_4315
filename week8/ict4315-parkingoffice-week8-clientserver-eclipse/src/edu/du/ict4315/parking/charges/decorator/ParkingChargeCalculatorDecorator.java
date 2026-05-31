package edu.du.ict4315.parking.charges.decorator;

import java.util.List;

public abstract class ParkingChargeCalculatorDecorator extends ParkingChargeCalculator {
  // Derived classes implement by adding a description to the super() value for getDescription
  @Override
  public abstract List<String> getDescription(); 
}
