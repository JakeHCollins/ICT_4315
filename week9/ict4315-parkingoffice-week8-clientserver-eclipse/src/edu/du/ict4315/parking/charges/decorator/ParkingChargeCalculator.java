package edu.du.ict4315.parking.charges.decorator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingPermit;

public abstract class ParkingChargeCalculator {
  
  private String description = "Parking Charge Description: ";
  
  public Money getParkingCharge(LocalDateTime in, ParkingPermit p, ParkingLot lot) {
    // An entry-only lot can use this default implementation
    return getParkingCharge(in, in, p, lot);
  }

  public abstract Money getParkingCharge(LocalDateTime in, LocalDateTime out, ParkingPermit p, ParkingLot lot) ;

  public List<String> getDescription() {
    List<String> list = new ArrayList<>();
    list.add(description);
    return list;
  }
  
  @Override
  public String toString() {
    return String.join("-", getDescription());
  }
}
