package edu.du.ict4315.parking;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.charges.strategy.ParkingChargeStrategy;
import edu.du.ict4315.parking.charges.strategy.WeekdayVehicleTypeStrategy;
import java.time.LocalDateTime;

public class ParkingLot {
    private String id;
    private String name;
    private Address address;
    private Money baseRate = Money.of(5.00);
    private ParkingChargeStrategy chargeStrategy = new WeekdayVehicleTypeStrategy();

    public ParkingLot(String id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public ParkingLot(String id, String name, Address address, Money baseRate) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.baseRate = baseRate;
    }

    public ParkingLot(String id, String name, Address address, Money baseRate, ParkingChargeStrategy chargeStrategy) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.baseRate = baseRate;
        this.chargeStrategy = chargeStrategy;
    }

    public Money getParkingCharges(ParkingPermit p, LocalDateTime in) {
        return chargeStrategy.calculateCharge(baseRate, p, in);
    }

    public Money getBaseRate() {
        return baseRate;
    }

    public ParkingChargeStrategy getChargeStrategy() {
        return chargeStrategy;
    }

    public void setChargeStrategy(ParkingChargeStrategy chargeStrategy) {
        this.chargeStrategy = chargeStrategy;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append("\n");
        sb.append(name);
        sb.append("\n");
        sb.append(address);
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public void enterLot(LocalDateTime in, String permitId) {
    }

    public void exitLot(LocalDateTime in, LocalDateTime out, String permitId) {
    }
}