package edu.du.ict4315.parking;

import com.google.inject.Inject;
import edu.du.ict4315.currency.Money;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TransactionManager {

    private static final Logger logger = Logger.getLogger(TransactionManager.class.getName());

    private final List<ParkingTransaction> transactions = new ArrayList<>();

    @Inject
    public TransactionManager() {
    }

    public ParkingTransaction park(ParkingEvent event) {
        ParkingLot lot = event.getLot();
        ParkingPermit permit = event.getPermit();
        ParkingTransaction transaction = null;
        if (lot != null && permit != null) {
            Money money = lot.getParkingCharges(permit, event.getEventTime());
            transaction = new ParkingTransaction(event.getEventTime(), permit, lot, money);
            transactions.add(transaction);
        }
        return transaction;
    }

    public ParkingTransaction park(LocalDateTime d, ParkingPermit p, ParkingLot l) {
        ParkingTransaction transaction = null;
        if (l != null && p != null) {
            Money money = l.getParkingCharges(p, d);
            transaction = new ParkingTransaction(d, p, l, money);
            transactions.add(transaction);
        }
        return transaction;
    }

    public Money getParkingCharges(Customer c) {
        List<ParkingTransaction> customerTransactions = transactions.stream()
                .filter(t -> t.getPermit().getCar().getOwner().equals(c))
                .collect(Collectors.toList());

        return customerTransactions.stream()
                .map(t -> t.getChargedAmount())
                .reduce(Money.of(0.0), (a, b) -> Money.add(a, b));
    }

    public Money getParkingCharges(ParkingPermit p) {
        return transactions.stream()
                .filter(t -> t.getPermit().equals(p))
                .map(t -> t.getChargedAmount())
                .reduce(Money.of(0.0), (a, b) -> Money.add(a, b));
    }
}s