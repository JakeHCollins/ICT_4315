////////////////////
// This object is at the heart of the Parking System.
// It is called "RealParkingOffice" in anticipation of creating a ParkingOffice interface later
// File: RealParkingOffice.java
// Author: M. I. Schwartz
////////////////////
package edu.du.ict4315.parking;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.support.FileLoaderParkingLot;
import edu.du.ict4315.parking.support.FileLoaderUser;
import edu.du.ict4315.parking.support.IdMaker;
import edu.du.ict4315.parking.support.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RealParkingOffice implements ParkingOfficeAdminProxy {

    private String parkingOfficeName;
    private final List<Customer> listOfCustomers;
    private final List<ParkingLot> listOfParkingLots;
    private Address parkingOfficeAddress;
    private final PermitManager permitManager;
    private final TransactionManager transactionManager;

    // TODO: All of existing Customers, Cars, Permits, Lots should be persisted
    // TODO: These interfaces should be dependency-injected, not constructed
    // If an add lot method is ever added, the transaction manager must observer it.
    public RealParkingOffice() {
        parkingOfficeName = "Not set";
        listOfCustomers = new ArrayList<>();
        listOfParkingLots = new ArrayList<>();
        parkingOfficeAddress = new Address.Builder().build();
        permitManager = new PermitManager();
        transactionManager = new TransactionManager(this);

        ParkingLot[] lots = FileLoaderParkingLot.loadCsvFileParkingLot("data/parking_lots_du.csv");
        listOfParkingLots.addAll(Arrays.asList(lots));

        for (ParkingLot lot : listOfParkingLots) {
            lot.addParkingObserver(transactionManager);
        }

        FileLoaderParkingLot.loadCsvFileParkingLotFactory("data/parking_lot_factories.csv", this);

        FileLoaderUser.loadCsvUserFile("data/users.csv", this);
    }

    public String register(Customer c) {
        if (c.getId().isBlank()) {
            c.setId(IdMaker.makeId("CUST"));
        }
        listOfCustomers.add(c);
        return c.getId();
    }

    public String register(Car c) {
        return permitManager.register(c).getId();
    }

    // For overflow and for testing purposes we allow
    // parking lots to be added and removed
    public String register(ParkingLot lot) {
        listOfParkingLots.add(lot);
        lot.addParkingObserver(transactionManager);
        return lot.getId();
    }

    public void unregister(ParkingLot lot) {
        listOfParkingLots.remove(lot);
        lot.removeParkingObserver(transactionManager);
    }

    public ParkingTransaction park(LocalDateTime d, ParkingPermit p, ParkingLot l) {
        // l.enterLot(d, p.getId());
        return transactionManager.park(d, p, l);
    }

    public ParkingTransaction leave(LocalDateTime in, LocalDateTime out, ParkingPermit p, ParkingLot l) {
        // l.exitLot(in, out, p.getId());
        return transactionManager.leave(in, out, p, l);
    }

    public Money getParkingCharges(ParkingPermit p) {
        return transactionManager.getParkingCharges(p);
    }

    public Money getParkingCharges(Customer c) {
        return transactionManager.getParkingCharges(c);
    }

    public String getParkingOfficeName() {
        return parkingOfficeName;
    }

    public void setParkingOfficeName(String parkingOfficeName) {
        this.parkingOfficeName = parkingOfficeName;
    }

    public Address getParkingOfficeAddress() {
        return parkingOfficeAddress;
    }

    public void setParkingOfficeAddress(Address parkingOfficeAddress) {
        this.parkingOfficeAddress = parkingOfficeAddress;
    }

    // The provided user and password are the administrator credentials
    public void saveUserFile(String user, String password) {
        FileLoaderUser.saveCsvUserFile("data/users.csv", this, user, password);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Parking Office: ");
        sb.append(parkingOfficeName);
        sb.append("\n");
        sb.append(parkingOfficeAddress);
        sb.append("\n");
        sb.append("Customer List\n");
        sb.append(listOfCustomers);
        sb.append("\n");
        sb.append("User list\n");
        sb.append(String.join("\n", User.getUsers()));
        sb.append("\n");
        sb.append("Parking Lots\n");
        sb.append(listOfParkingLots);
        sb.append("\n");

        return sb.toString();
    }

    public Customer getCustomer(String id) {
        Customer result = null;
        for (Customer c : listOfCustomers) {
            if (c.getId().equals(id)) {
                result = c;
                break;
            }
        }
        return result;
    }

    public String[] getCustomerIds() {
        return listOfCustomers.stream().map(customer -> customer.getId()).toArray(String[]::new);
    }

    public String[] getLotIds() {
        return listOfParkingLots.stream().map(lot -> lot.getId()).toArray(String[]::new);
    }

    // Delegation from User
    public User authorizeUser(String id, String passwd) {
        return User.authorizeUser(id, passwd);
    }

    // Use delegation
    public ParkingPermit getParkingPermit(String id) {
        return permitManager.findPermit(id);
    }

    public ParkingLot getParkingLot(String id) {
        ParkingLot result = null;
        for (ParkingLot p : listOfParkingLots) {
            if (p.getId().equals(id)) {
                result = p;
                break;
            }
        }

        return result;
    }
}
