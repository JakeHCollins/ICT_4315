package edu.du.ict4315.parking.charges.strategy;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.CarType;
import edu.du.ict4315.parking.ParkingPermit;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class WeekdayVehicleTypeStrategy implements ParkingChargeStrategy {

    @Override
    public Money calculateCharge(Money baseRate, ParkingPermit permit, LocalDateTime entryTime) {
        Money charge = baseRate;

        DayOfWeek day = entryTime.getDayOfWeek();
        boolean isWeekend = (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY);
        if (isWeekend) {
            charge = Money.times(charge, 0.75);
        }

        if (permit.getCar().getType() == CarType.COMPACT) {
            charge = Money.times(charge, 0.90);
        }

        return charge;
    }
}