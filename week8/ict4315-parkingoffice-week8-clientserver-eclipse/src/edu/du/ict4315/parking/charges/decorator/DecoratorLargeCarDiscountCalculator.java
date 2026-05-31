package edu.du.ict4315.parking.charges.decorator;

import java.time.LocalDateTime;
import java.util.List;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.CarType;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingPermit;

public class DecoratorLargeCarDiscountCalculator extends ParkingChargeCalculatorDecorator {

  private String description = "SUV 20% discount";
  private ParkingChargeCalculator calculator;
  
  public DecoratorLargeCarDiscountCalculator(ParkingChargeCalculator calculator) {
    this.calculator = calculator;
  }
  
  @Override
  public List<String> getDescription() {
    List<String> result = calculator.getDescription();
    result.add(description);
    return result;
  }

  @Override
  public Money getParkingCharge(LocalDateTime in, LocalDateTime out, ParkingPermit p, ParkingLot lot) {
    Money result = calculator.getParkingCharge(in, out, p, lot);
    if ( p != null && p.getCar() != null ) {
      if ( p.getCar().getType() == CarType.SUV ) {
        // 80% = 4/5
        result = Money.div(Money.times(result, 4), 5);
      }
    }
    return result;
  }

}
