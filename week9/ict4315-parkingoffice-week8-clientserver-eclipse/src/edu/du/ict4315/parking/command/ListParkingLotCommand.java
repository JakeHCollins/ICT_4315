/*
 * File: ListParkingLot.java
 * Author: Instructor
 */
package edu.du.ict4315.parking.command;

import edu.du.ict4315.parking.RealParkingOffice;
import java.util.Properties;

/**
 *
 * @author michael
 */
public class ListParkingLotCommand implements Command {

    private final RealParkingOffice parkingOffice;

    private static final String commandName = "LISTLOTS";
    private static final String displayName = "List Parking Lot ids";

    public ListParkingLotCommand(RealParkingOffice parkingOffice) {
        this.parkingOffice = parkingOffice;
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String execute(Properties params) {
        // Currently parameters are ignored
        return String.join(",", parkingOffice.getLotIds());
    }

}
