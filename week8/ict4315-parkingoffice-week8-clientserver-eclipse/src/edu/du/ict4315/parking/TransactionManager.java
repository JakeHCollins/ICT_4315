package edu.du.ict4315.parking;

import java.beans.PropertyChangeEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.observer.ParkingEvent;
import edu.du.ict4315.parking.observer.ParkingObserver;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionManager implements ParkingObserver {

    private static final Logger logger = Logger.getLogger(TransactionManager.class.getName());

    private final List<ParkingTransaction> transactions = new ArrayList<>();
    private final RealParkingOffice office;

    public TransactionManager(RealParkingOffice office) {
        this.office = office;
    }

    public ParkingTransaction park(LocalDateTime enterTime, ParkingPermit permit, ParkingLot lot) {
        ParkingTransaction transaction = null;
        if (lot != null && permit != null) {
            if (lot.enterStatus()) { // if charge upon entry
                Money money = lot.getParkingCharges(permit, enterTime, lot.getDuration(permit, enterTime));
                transaction = new ParkingTransaction(enterTime, enterTime, permit, lot, money);
                transactions.add(transaction);
            }
        } else {
            logger.log(Level.INFO, "Parameter null: {0}{1}",
                    new Object[]{(lot == null) ? "lot " : "",
                        (permit == null) ? "permit " : ""});
        }

        return transaction;
    }

    public ParkingTransaction leave(LocalDateTime entryTime, LocalDateTime exitTime, ParkingPermit permit,
            ParkingLot parkingLot) {
        ParkingTransaction transaction = null;
        // LocalDateTime entryTime = parkingLot.getEntryTime(permit);
        if (permit != null) {
            if (parkingLot.exitStatus()) {
                // Duration is 3 days if no entry registered
                if (entryTime == null) {
                    entryTime = exitTime.minus(Duration.ofDays(3L));
                }
                Duration duration = Duration.between(entryTime, exitTime);
                // Discounts go by entry time
                Money money = parkingLot.getParkingCharges(permit, entryTime, duration);
                transaction = new ParkingTransaction(entryTime, exitTime, permit, parkingLot, money);
                transactions.add(transaction);
            }
        } else {
            logger.info(
                    "Parameter null: " + ((parkingLot == null) ? "lot " : "") + ((permit == null) ? "permit " : ""));
        }
        return transaction;

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ParkingEvent event = (ParkingEvent) evt.getNewValue();
        ParkingPermit permit = office.getParkingPermit(event.getParkingPermitId());
        if (permit != null) {
            if (event.isExit()) {
                // LocalDateTime exitTime, ParkingPermit permit,
                leave(event.getIn(), event.getOut(), permit, event.getParkingLot());
            } else {
                park(event.getIn(), permit, event.getParkingLot());
            }
        } else {
            logger.severe("Permit " + event.getParkingPermitId() + " is not registered");
        }
    }

    public Money getParkingCharges(Customer c) {
        List<ParkingTransaction> customerTransactions;
        // First, let's get a list of the transactions for the customer.
        customerTransactions = transactions.stream()
                .filter(transaction -> transaction.getPermit().getCar().getOwner().equals(c))
                .collect(Collectors.toList());

        // Now lets add up all the charged amounts
        Money result = customerTransactions.stream()
                .map(transaction -> transaction.getChargedAmount())
                .reduce(Money.of(0.0), (a, b) -> Money.add(a, b));

        return result;
    }

    public Money getParkingCharges(ParkingPermit p) {
        return transactions.stream()
                .filter(transaction -> transaction.getPermit().equals(p))
                .map(transaction -> transaction.getChargedAmount())
                .reduce(Money.of(0.0), (a, b) -> Money.add(a, b));
    }

}
