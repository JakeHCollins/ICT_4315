/*
 * File: ListCustomerCommand.java
 * Author: Instructor
 */
package edu.du.ict4315.parking.command;

import edu.du.ict4315.parking.RealParkingOffice;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author michael
 */
public class ListCustomerCommand implements Command {

    private static final Logger logger = Logger.getLogger(ListCustomerCommand.class.getName());

    private final RealParkingOffice parkingOffice;

    private static final String commandName = "LISTCUST";
    private static final String displayName = "List Customer ids";

    public ListCustomerCommand(RealParkingOffice parkingOffice) {
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
        logger.info("Returning " + String.join(", ", parkingOffice.getCustomerIds()));
        return String.join(",", parkingOffice.getCustomerIds());
    }

}
