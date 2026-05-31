////////////////////
// This class represents the Parking Lot
// File: ParkingLotChargeOnEntry.java
// Author: M. I. Schwartz
////////////////////
package edu.du.ict4315.parking;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.charges.factory.FactoryParkingChargeCalculator;
import java.time.Duration;
import java.time.LocalDateTime;

public class ParkingLotChargeOnEntry extends ParkingLot {

    // java.lang.String,
    // java.lang.String,
    // edu.du.ict4315.parking.Address,
    // edu.du.ict4315.currency.Money,
    // int
    public ParkingLotChargeOnEntry(String id, String name, Address address,
            int capacity, Money baseRate) {
        super(id, name, address, capacity, Money.of(5.00));
    }

    public ParkingLotChargeOnEntry(String id, String name, Address address,
            Money baseRate, int capacity) {
        super(id, name, address, capacity, Money.of(5.00));
    }

    public ParkingLotChargeOnEntry(String id, String name, Address address,
            Money baseRate,
            int capacity,
            FactoryParkingChargeCalculator factory) {
        super(id, name, address, baseRate, capacity, factory);
    }

    @Override
    public Money getParkingCharges(ParkingPermit permit, LocalDateTime ldt, Duration duration) {
        // ParkingChargeStrategy strategy = getParkingChargesFactory().getStrategy();
        // assignment and then return in case of need of debugging breakpoint
        // Money money = strategy.getParkingCharge(getBaseRate(), ldt, duration, permit);
        Money money = this.getParkingChargesCalculatorFactory().getCalculator().getParkingCharge(ldt, permit, this);
        return money;
    }

    @Override
    protected boolean enterStatus() {
        return true;
    }

}
