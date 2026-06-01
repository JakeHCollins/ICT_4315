/*
 * Course ICT4315
 * Author: Instructor
 */
package edu.du.ict4315.parking;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.charges.factory.FactoryParkingChargeCalculator;
import edu.du.ict4315.parking.charges.factory.FactoryOriginalAlgorithm;
import edu.du.ict4315.parking.observer.ParkingAction;
import edu.du.ict4315.parking.observer.ParkingEvent;
import edu.du.ict4315.parking.observer.ParkingObserver;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * ParkingLot needs a way to getParkingCharges and track current # of cars in
 * the lot. Assume even pay-on-entry lots have a way of tracking exits from the
 * lot Inputs: in time for pay-on-entry lots in time and out time for
 * pay-on-exit lots (or in time and duration)
 *
 * @author michael
 *
 */
public abstract class ParkingLot {

    private final String id;
    private final String name;
    private Address address;
    private Money baseRate;
    private int capacity;
    /* Old way: Use a FactoryParkingCharges object
    // Use the chargeStrategy to compute charges.
    private FactoryParkingCharges parkingChargesFactory;
    */
    /* New way: Use a FactoryParkingChargeCalculator */
    private FactoryParkingChargeCalculator parkingChargeCalculatorFactory;
    
    // The permit list tracks cars that are in the lot and their entry times
    private final PermitList list;
    // Make the ParkingLot observable
    ParkingAction observable = new ParkingAction(this);

    public ParkingLot(String id, String name, Address address,
            int capacity, Money baseRate) {
        this(id, name, address, baseRate, capacity, 
            (FactoryParkingChargeCalculator)new FactoryOriginalAlgorithm());
    }

    public ParkingLot(String id, String name, Address address,
            Money baseRate,
            int capacity,
            FactoryParkingChargeCalculator factory) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.baseRate = baseRate;
        this.capacity = capacity;
        list = new PermitList();

        this.parkingChargeCalculatorFactory = factory;
    }

    public void setBaseRate(Money baseRate) {
        this.baseRate = baseRate;
    }

    public Money getParkingCharges(ParkingPermit permit, LocalDateTime ldt, Duration duration) {
        
        // assignment and then return in case of need of debugging breakpoint
        // ParkingChargeStrategy chargeStrategy = parkingChargesFactory.getStrategy();
        // Money money = chargeStrategy.getParkingCharge(baseRate, ldt, duration, permit);
        Money money = parkingChargeCalculatorFactory.getCalculator().getParkingCharge(ldt,permit,this);
        return money;
    }

    public final Money getBaseRate() {
        return baseRate;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("Parking lot: id: ");
        sb.append(id);
        sb.append("; name: ");
        sb.append(name);
        sb.append("; type: ");
        sb.append(enterStatus() ? "on entry" : "on exit");
        sb.append("; address: ");
        sb.append(address);
        sb.append("; status: ");
        sb.append(getLoad());
        sb.append("/");
        sb.append(getCapacity());

        return sb.toString();
    }

    public final String getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final Address getAddress() {
        return address;
    }

    // Only valid when called after entry and before exit
    public final LocalDateTime getEntryTime(ParkingPermit permit) {
        return list.getLocalDateTime(permit.getId());
    }

    // getDuration will return the correct result only if called before exitLot.
    public final Duration getDuration(ParkingPermit permit, LocalDateTime outTime) {
        LocalDateTime inTime = getEntryTime(permit);
        Duration duration;
        if (enterStatus()) {
            // Charge on entry lot
            duration = Duration.ZERO;
        } else if (inTime != null) {
            duration = Duration.between(inTime, outTime);
        } else {
            duration = Duration.ofDays(3L); // Charge 3 days if no entry record
        }
        return duration;
    }

    // If enterStatus() returns true, this is a pay-on-entry lot
    abstract protected boolean enterStatus();

    // If exitStatus() returns true, this is a pay-on-exit lot
    // By design, a lot cannot charge both on entry and exit
    protected final boolean exitStatus() {
        return !enterStatus(); // always the opposite of enterStatus()
    }

    // Method for permit-required-on-enter lot
    public final boolean enterLot(LocalDateTime in, String permitId) {
        list.addCar(permitId, in);
        // Create and post a ParkingEvent: lot, inTime, outTime, permit id 
        ParkingEvent event = new ParkingEvent(this, in, null, permitId, false);
        observable.parkingNotification(event);
        return enterStatus(); // no charge for entering
    }

    // Method for permit-required-on-exit lot. Removes entry data from PermitList
    // Only the permit id is needed.
    public final boolean exitLot(LocalDateTime in, LocalDateTime out, String permitId) {
        list.removeCar(permitId);
        // Create and post a ParkingEvent: lot, inTime, outTime, permit id 
        ParkingEvent event = new ParkingEvent(this, in, out, permitId, true);
        observable.parkingNotification(event);
        return exitStatus(); // charge at exit
    }

    public final int getCapacity() {
        return capacity;
    }

    public final void setCapacity(int capacity) {
        if (capacity >= 0) {
            this.capacity = capacity;
        }
    }

    public final int getLoad() {
        return list.size();
    }

    public FactoryParkingChargeCalculator getParkingChargesCalculatorFactory() {
        return parkingChargeCalculatorFactory;
    }

    public void setParkingChargeCalculatorFactory(FactoryParkingChargeCalculator parkingChargeCalculatorFactory) {
        this.parkingChargeCalculatorFactory = parkingChargeCalculatorFactory;
    }

    // Add delegation to observable
    // Delegation
    public void addParkingObserver(ParkingObserver observer) {
        observable.addParkingObserver(observer);
    }

    public void removeParkingObserver(ParkingObserver observer) {
        observable.removeParkingObserver(observer);
    }

}
