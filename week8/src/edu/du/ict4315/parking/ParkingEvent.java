package edu.du.ict4315.parking;

import java.time.LocalDateTime;

public record ParkingEvent(ParkingLot lot, ParkingPermit permit, LocalDateTime eventTime,
                           edu.du.ict4315.parking.ParkingEvent.EventType eventType) {
    public enum EventType {ENTER, EXIT}

}