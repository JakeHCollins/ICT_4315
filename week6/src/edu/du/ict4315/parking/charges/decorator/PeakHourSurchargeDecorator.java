package edu.du.ict4315.parking.charges.decorator;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingPermit;
import java.time.LocalDateTime;

public class PeakHourSurchargeDecorator extends ParkingChargeCalculatorDecorator {

    private static final int PEAK_START = 7;
    private static final int PEAK_END = 9;

    public PeakHourSurchargeDecorator(ParkingChargeCalculator calculator) {
        super(calculator);
    }

    @Override
    public Money getParkingCharge(LocalDateTime entryTime, ParkingLot lot, ParkingPermit permit) {
        Money charge = super.getParkingCharge(entryTime, lot, permit);
        int hour = entryTime.getHour();
        if (hour >= PEAK_START && hour < PEAK_END) {
            charge = Money.times(charge, 1.50);
        }
        return charge;
    }
}