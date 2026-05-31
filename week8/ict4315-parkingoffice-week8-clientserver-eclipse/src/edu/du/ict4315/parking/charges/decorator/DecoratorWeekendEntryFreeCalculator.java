package edu.du.ict4315.parking.charges.decorator;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingPermit;

public class DecoratorWeekendEntryFreeCalculator extends ParkingChargeCalculatorDecorator {

  private final ParkingChargeCalculator calculator;
  private final String description = "Weekend free discount";
  
  public DecoratorWeekendEntryFreeCalculator( ParkingChargeCalculator calculator) {
    this.calculator = calculator;
  }
  
  @Override
  public List<String> getDescription() {
    List<String> list = calculator.getDescription();
    list.add(description);
    return list;
  }

  private boolean isWeekend(LocalDateTime d) {
    if ( d.getDayOfWeek() == DayOfWeek.SATURDAY || d.getDayOfWeek() == DayOfWeek.SUNDAY ) {
      return true;
    }
    return false;
  }
  
  @Override
  public Money getParkingCharge(LocalDateTime in, LocalDateTime out, ParkingPermit p, ParkingLot lot) {
    Money result = calculator.getParkingCharge(in, out, p, lot);
    // No discount if the data isn't complete
    if ( in != null && out != null ) {
      if ( isWeekend(in) || isWeekend(out) ) {
         result =  Money.of(0.0);
      }
    }
    return result;
  }

}
