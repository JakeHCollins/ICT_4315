package edu.du.ict4315.parking;

import com.google.inject.Inject;
import java.util.List;

public class ParkingObserver implements ParkingAction {

    private final TransactionManager transactionManager;

    @Inject
    public ParkingObserver(TransactionManager transactionManager, List<ParkingLot> lots) {
        this.transactionManager = transactionManager;
        for (ParkingLot lot : lots) {
            lot.addObserver(this);
        }
    }

    @Override
    public void update(ParkingEvent event) {
        if (event.getEventType() == ParkingEvent.EventType.ENTER) {
            transactionManager.park(event);
        }
    }
}