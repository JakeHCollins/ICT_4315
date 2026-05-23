package edu.du.ict4315.parking.charges.decorator;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.CarType;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingPermit;
import java.time.LocalDateTime;

public class CompactCarDiscountDecorator extends ParkingChargeCalculatorDecorator {

    public CompactCarDiscountDecorator(ParkingChargeCalculator calculator) {
        super(calculator);
    }

    @Override
    public Money getParkingCharge(LocalDateTime entryTime, ParkingLot lot, ParkingPermit permit) {
        Money charge = super.getParkingCharge(entryTime, lot, permit);
        if (permit.getCar().getType() == CarType.COMPACT) {
            charge = Money.times(charge, 0.80);
        }
        return charge;
    }
}