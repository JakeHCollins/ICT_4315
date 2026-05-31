////////////////////
// This class represents the Parking Lot
// File: ParkingLotChargeOnExit.java
// Author: M. I. Schwartz
////////////////////
package edu.du.ict4315.parking;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.charges.decorator.ParkingChargeCalculator;
import edu.du.ict4315.parking.charges.factory.FactoryOriginalAlgorithm;
import edu.du.ict4315.parking.charges.factory.FactoryParkingChargeCalculator;
import java.time.Duration;
import java.time.LocalDateTime;

public class ParkingLotChargeOnExit extends ParkingLot {

    public ParkingLotChargeOnExit(String id, String name, Address address) {
        super(id, name, address, Money.of(5.00), 50,
                new FactoryOriginalAlgorithm());
    }

    public ParkingLotChargeOnExit(String id, String name, Address address,
            int capacity, Money baseRate) {
        super(id, name, address, baseRate, capacity,
                new FactoryOriginalAlgorithm());
    }

    public ParkingLotChargeOnExit(String id, String name, Address address,
            Money baseRate, int capacity) {
        super(id, name, address, baseRate, capacity,
                new FactoryOriginalAlgorithm());
    }

    public ParkingLotChargeOnExit(String id, String name, Address address,
            Money baseRate, int capacity,
            FactoryParkingChargeCalculator factory) {
        super(id, name, address, baseRate, capacity, factory);
    }

    @Override
    public Money getParkingCharges(ParkingPermit permit, LocalDateTime ldt, Duration duration) {
        ParkingChargeCalculator calculator = this.getParkingChargesCalculatorFactory().getCalculator();
        Money money = calculator.getParkingCharge(ldt, ldt.plus(duration), permit, this);
        return money;
    }

    @Override
    protected boolean enterStatus() {
        return false;
    }

}
