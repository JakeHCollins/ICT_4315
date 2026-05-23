package edu.du.ict4315.parking.charges.decorator;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingPermit;
import java.time.LocalDateTime;

public abstract class ParkingChargeCalculatorDecorator extends ParkingChargeCalculator {

    private final ParkingChargeCalculator calculator;

    public ParkingChargeCalculatorDecorator(ParkingChargeCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public Money getParkingCharge(LocalDateTime entryTime, ParkingLot lot, ParkingPermit permit) {
        return calculator.getParkingCharge(entryTime, lot, permit);
    }
}