package edu.du.ict4315.parking.charges.decorator;

import java.time.LocalDateTime;
import java.util.List;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingPermit;

public class DecoratorEarlyInCalculator extends ParkingChargeCalculatorDecorator {

  private ParkingChargeCalculator calculator;
  private String description = "Early in 10% discount";
  private int earlyHour = 8;
  private double discount = 0.9;
  
  public DecoratorEarlyInCalculator(ParkingChargeCalculator calc) {
    calculator = calc;
  }
  
  public DecoratorEarlyInCalculator(ParkingChargeCalculator calc, int hour, double discount) {
      calculator = calc;
      earlyHour = hour;
      this.discount = discount;
  }
  
  @Override
  public List<String> getDescription() {
    List<String> list = calculator.getDescription();
    list.add(description);
    return list;
  }

  @Override
  public Money getParkingCharge(LocalDateTime in, LocalDateTime out, ParkingPermit p, ParkingLot lot) {
    Money result = calculator.getParkingCharge(in, out, p, lot);
    if ( in.getHour() < earlyHour ) {
      result = Money.times(result, discount);
    }
    // TODO: Add duration computation
    return result;
  }

}
