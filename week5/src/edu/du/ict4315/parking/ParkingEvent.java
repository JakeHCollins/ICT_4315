package edu.du.ict4315.parking;

import java.time.LocalDateTime;

public class ParkingEvent {
    public enum EventType { ENTER, EXIT }

    private final ParkingLot lot;
    private final ParkingPermit permit;
    private final LocalDateTime eventTime;
    private final EventType eventType;

    public ParkingEvent(ParkingLot lot, ParkingPermit permit, LocalDateTime eventTime, EventType eventType) {
        this.lot = lot;
        this.permit = permit;
        this.eventTime = eventTime;
        this.eventType = eventType;
    }

    public ParkingLot getLot() {
        return lot;
    }

    public ParkingPermit getPermit() {
        return permit;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public EventType getEventType() {
        return eventType;
    }
}