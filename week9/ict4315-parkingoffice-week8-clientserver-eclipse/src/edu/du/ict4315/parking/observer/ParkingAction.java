package edu.du.ict4315.parking.observer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

public class ParkingAction {

    PropertyChangeSupport observers;

    public ParkingAction(Object source) {
        observers = new PropertyChangeSupport(source);
    }

    public void addParkingObserver(ParkingObserver observer) {
        observers.addPropertyChangeListener(observer);
    }

    public void removeParkingObserver(ParkingObserver observer) {
        observers.removePropertyChangeListener(observer);
    }

    public void parkingNotification(ParkingEvent parkingEvent) {
        PropertyChangeEvent event;
        event = new PropertyChangeEvent(parkingEvent, null, null, parkingEvent);
        observers.firePropertyChange(event);
    }

}
