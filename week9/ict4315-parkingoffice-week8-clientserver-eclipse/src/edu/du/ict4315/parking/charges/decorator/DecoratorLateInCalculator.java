package edu.du.ict4315.parking.charges.decorator;

import java.time.LocalDateTime;
import java.util.List;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingPermit;

public class DecoratorLateInCalculator extends ParkingChargeCalculatorDecorator {

  private ParkingChargeCalculator calculator;
  private String description = "Late in 30% discount";
  
  public DecoratorLateInCalculator(ParkingChargeCalculator calc) {
    calculator = calc;
  }
  
  @Override
  public List<String> getDescription() {
    List<String> list = calculator.getDescription();
    list.add(description);
    return list;
  }

  @Override
  public Money getParkingCharge(LocalDateTime in, LocalDateTime out, ParkingPermit p, ParkingLot lot) {
    Money charges = calculator.getParkingCharge(in, out, p, lot);
    // Discount late in (after 5 PM) by 30%
    if ( in.getHour() >= 17 ) {
      charges = Money.div(Money.times(charges, 7), 10); // 7/10
    }
    return charges;
  }

}
