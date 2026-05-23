package edu.du.ict4315.parking.charges.decorator;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingPermit;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class WeekendDiscountDecorator extends ParkingChargeCalculatorDecorator {

    public WeekendDiscountDecorator(ParkingChargeCalculator calculator) {
        super(calculator);
    }

    @Override
    public Money getParkingCharge(LocalDateTime entryTime, ParkingLot lot, ParkingPermit permit) {
        Money charge = super.getParkingCharge(entryTime, lot, permit);
        DayOfWeek day = entryTime.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            charge = Money.times(charge, 0.75);
        }
        return charge;
    }
}