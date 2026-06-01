/*
 * Course ICT4315
 * Author: Instructor
 */
package edu.du.ict4315.parking.support;

import edu.du.ict4315.parking.Customer;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingPermit;
import edu.du.ict4315.parking.RealParkingOffice;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michael
 */
public class ParameterCheckUtilities {
    ////////////////////
    // TODO: Complete the checks.
    // The below check routines are just sketches. 
    // What should the acceptability rules be?
    // What should the defaults be?
    // Should commands / parameters be edited (trimmed, case changed, length limited)?
    ////////////////////

    private final RealParkingOffice parkingOffice;

    private static final Logger logger = Logger.getLogger(ParameterCheckUtilities.class.getName());

    public ParameterCheckUtilities(RealParkingOffice parkingOffice) {
        this.parkingOffice = parkingOffice;
    }

    public static boolean checkNumberOfParameters(int expected, int provided) {
        boolean result = true;
        if (provided < expected) {
            logger.log(Level.SEVERE, "Not enough parameters! Expected {0} received {1}",
                    new Object[]{expected, provided});
            result = false;
        }
        return result;
    }

    public static String checkName(String name) {
        String result = name;
        return result;
    }

    public static LocalDateTime checkDateTime(String dateTime) {
        LocalDateTime result = LocalDateTime.now();
        try {
            result = LocalDateTime.parse(dateTime);
        }
        catch (DateTimeParseException ex) {
            logger.log(Level.INFO, "Cannot parse datetime {0}: {1}", new Object[]{dateTime, ex});
        }
        return result;
    }

    public Customer checkCustomer(String customerId) {
        Customer customer = parkingOffice.getCustomer(customerId);
        return customer;
    }

    public ParkingLot checkParkingLot(String lotId) {
        ParkingLot result = parkingOffice.getParkingLot(lotId);
        return result;
    }

    public static String checkLicensePlate(String plate) {
        String result = plate;
        return result;
    }

    public ParkingPermit checkParkingPermit(String permitId) {
        ParkingPermit result;
        result = parkingOffice.getParkingPermit(permitId);
        return result;
    }

}
