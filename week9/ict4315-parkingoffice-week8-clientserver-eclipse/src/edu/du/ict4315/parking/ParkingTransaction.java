//////////////////////////
// This class represents a parking transaction event.
// This class is immutable.
// File: ParkingTransaction.java
// Author: M. I. Schwartz
//////////////////////////
package edu.du.ict4315.parking;

import java.time.Instant;
import java.time.LocalDateTime;

import edu.du.ict4315.currency.Money;

// Note: Modified to track both dateIn and dateOut
// An enter-only lot will use the same value for both.

public class ParkingTransaction {

    private Instant transactionDate;
    private LocalDateTime dateIn;
    private LocalDateTime dateOut;
    private ParkingPermit permit;
    private ParkingLot parkingLot;
    private Money chargedAmount;

    public ParkingTransaction(LocalDateTime dIn, LocalDateTime dOut, ParkingPermit p, ParkingLot l, Money m) {
        transactionDate = Instant.now();
        dateIn = dIn;
        dateOut = dOut;
        permit = p;
        parkingLot = l;
        chargedAmount = m;
    }

    public Money getChargedAmount() {
        return chargedAmount;
    }

    public ParkingPermit getPermit() {
        return permit;
    }

    public LocalDateTime getDateIn() {
        return dateIn;
    }

    public LocalDateTime getDateOut() {
        return dateIn;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("Transaction: ");
        sb.append("logged: ");
        sb.append(transactionDate);
        sb.append("::: parked: ");
        sb.append(dateIn);
        sb.append(" -- ");
        sb.append(dateOut);
        sb.append("; permit: ");
        sb.append(permit.getId());
        sb.append("; lot: ");
        sb.append(parkingLot.getName());
        sb.append("; charges: ");
        sb.append(chargedAmount);

        return sb.toString();
    }
}
