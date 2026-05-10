package edu.du.ict4315.parking.charges.strategy;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.CarType;
import edu.du.ict4315.parking.ParkingPermit;

import java.time.LocalDateTime;

public class PeakHourSurchargeSUVStrategy implements ParkingChargeStrategy {

    private static final int PEAK_START = 7;
    private static final int PEAK_END   = 9;

    @Override
    public Money calculateCharge(Money baseRate, ParkingPermit permit, LocalDateTime entryTime) {
        Money charge = baseRate;

        int hour = entryTime.getHour();
        boolean isPeak = (hour >= PEAK_START && hour < PEAK_END);
        if (isPeak) {
            charge = Money.times(charge, 1.50);
        }

        if (permit.getCar().getType() == CarType.SUV) {
            charge = Money.times(charge, 1.25);
        }

        return charge;
    }
}