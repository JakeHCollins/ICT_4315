package edu.du.ict4315.parking.charges.strategy;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.ParkingPermit;

import java.time.LocalDateTime;

public interface ParkingChargeStrategy {
    Money calculateCharge(Money baseRate, ParkingPermit permit, LocalDateTime entryTime);
}