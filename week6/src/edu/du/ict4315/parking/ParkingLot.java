package edu.du.ict4315.parking;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.charges.decorator.ParkingChargeCalculator;
import edu.du.ict4315.parking.charges.factory.ParkingChargeCalculatorFactory;
import edu.du.ict4315.parking.charges.factory.StandardCalculatorFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    private String id;
    private String name;
    private Address address;
    private Money baseRate = Money.of(5.00);
    private ParkingChargeCalculatorFactory calculatorFactory = new StandardCalculatorFactory();
    private final List<ParkingAction> observers = new ArrayList<>();

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

    public ParkingLot(String id, String name, Address address, Money baseRate, ParkingChargeCalculatorFactory calculatorFactory) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.baseRate = baseRate;
        this.calculatorFactory = calculatorFactory;
    }

    public void addObserver(ParkingAction observer) {
        observers.add(observer);
    }

    public void removeObserver(ParkingAction observer) {
        observers.remove(observer);
    }

    private void notifyObservers(ParkingEvent event) {
        for (ParkingAction observer : observers) {
            observer.update(event);
        }
    }

    public void enter(ParkingPermit permit) {
        ParkingEvent event = new ParkingEvent(this, permit, LocalDateTime.now(), ParkingEvent.EventType.ENTER);
        notifyObservers(event);
    }

    public void exit(ParkingPermit permit) {
        ParkingEvent event = new ParkingEvent(this, permit, LocalDateTime.now(), ParkingEvent.EventType.EXIT);
        notifyObservers(event);
    }

    public Money getParkingCharges(ParkingPermit p, LocalDateTime in) {
        ParkingChargeCalculator calculator = calculatorFactory.getCalculator();
        return calculator.getParkingCharge(in, this, p);
    }

    public Money getBaseRate() {
        return baseRate;
    }

    public ParkingChargeCalculatorFactory getCalculatorFactory() {
        return calculatorFactory;
    }

    public void setCalculatorFactory(ParkingChargeCalculatorFactory calculatorFactory) {
        this.calculatorFactory = calculatorFactory;
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append("\n");
        sb.append(name);
        sb.append("\n");
        sb.append(address);
        return sb.toString();
    }
}