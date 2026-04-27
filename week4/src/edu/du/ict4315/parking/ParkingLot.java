package edu.du.ict4315.parking;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.charges.factory.ParkingChargeStrategyFactory;
import edu.du.ict4315.parking.charges.factory.WeekdayVehicleTypeStrategyFactory;
import edu.du.ict4315.parking.charges.strategy.ParkingChargeStrategy;
import java.time.LocalDateTime;

public class ParkingLot {
    private String id;
    private String name;
    private Address address;
    private Money baseRate = Money.of(5.00);
    private ParkingChargeStrategyFactory strategyFactory = new WeekdayVehicleTypeStrategyFactory();

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

    public ParkingLot(String id, String name, Address address, Money baseRate, ParkingChargeStrategyFactory strategyFactory) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.baseRate = baseRate;
        this.strategyFactory = strategyFactory;
    }

    public Money getParkingCharges(ParkingPermit p, LocalDateTime in) {
        ParkingChargeStrategy strategy = strategyFactory.getStrategy();
        return strategy.calculateCharge(baseRate, p, in);
    }

    public Money getBaseRate() {
        return baseRate;
    }

    public ParkingChargeStrategyFactory getStrategyFactory() {
        return strategyFactory;
    }

    public void setStrategyFactory(ParkingChargeStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
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