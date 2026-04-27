package edu.du.ict4315.parking;

import edu.du.ict4315.currency.Money;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TransactionManager {

  private static final Logger logger = Logger.getLogger(TransactionManager.class.getName());

  private List<ParkingTransaction> transactions = new ArrayList<>();
  private RealParkingOffice office;

  public TransactionManager(RealParkingOffice office) {
    this.office = office;
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
            .filter(transaction -> transaction.getPermit().getCar().getOwner().equals(c))
            .collect(Collectors.toList());

    return customerTransactions.stream()
            .map(transaction -> transaction.getChargedAmount())
            .reduce(Money.of(0.0), (a, b) -> Money.add(a, b));
  }

  public Money getParkingCharges(ParkingPermit p) {
    return transactions.stream()
            .filter(transaction -> transaction.getPermit().equals(p))
            .map(transaction -> transaction.getChargedAmount())
            .reduce(Money.of(0.0), (a, b) -> Money.add(a, b));
  }
}