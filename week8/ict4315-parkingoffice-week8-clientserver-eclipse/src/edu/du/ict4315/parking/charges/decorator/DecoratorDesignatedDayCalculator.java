package edu.du.ict4315.parking.charges.decorator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingPermit;

public class DecoratorDesignatedDayCalculator extends ParkingChargeCalculatorDecorator {
  private ParkingChargeCalculator calculator;
  private String description = "Designated day with doubled charges";
  
  public DecoratorDesignatedDayCalculator(ParkingChargeCalculator calc) {
    calculator = calc;
  }

  private static LocalDate[] days = {
      LocalDate.of(2020, 1, 1),   LocalDate.of(2020, 1, 20),  
      LocalDate.of(2020, 2, 17),  LocalDate.of(2020, 5, 25),
      LocalDate.of(2020, 6, 30),  LocalDate.of(2020, 7, 3),
      LocalDate.of(2020, 7, 4),   LocalDate.of(2020, 7, 5), 
      LocalDate.of(2020, 7, 6),   LocalDate.of(2020, 7, 7), 
      LocalDate.of(2020, 7, 8),   LocalDate.of(2020, 7, 9), 
      LocalDate.of(2020, 7, 10),  LocalDate.of(2020, 7, 11), 
      LocalDate.of(2020, 9, 7),   LocalDate.of(2020,10, 12),
    };
    private Set<LocalDate> designatedDays = new HashSet<>(Arrays.asList(days));

  @Override
  public List<String> getDescription() {
    List<String> list = calculator.getDescription();
    list.add(description);
    return list;
  }

  @Override
  public Money getParkingCharge(LocalDateTime in, LocalDateTime out, ParkingPermit p, ParkingLot lot) {
    Money result = calculator.getParkingCharge(in, out, p, lot);
    if ( designatedDays.contains(in.toLocalDate()) || designatedDays.contains(out.toLocalDate())) {
      result = Money.times(result,2); // 2
    }
    return result;
  }

  public boolean addSpecialDay(LocalDate date) {
    return designatedDays.add(date);
  }
  
  public boolean removeSpecialDay(LocalDate date) {
    return designatedDays.remove(date);
  }

}
