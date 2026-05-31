/*
 * Course ICT4315
 * Author: Instructor
 */
package edu.du.ict4315.parking.command;

import edu.du.ict4315.parking.Customer;
import edu.du.ict4315.parking.RealParkingOffice;
import edu.du.ict4315.parking.support.ParameterCheckUtilities;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author michael
 */
public class RegisterCustomerCommand implements Command {

    private final RealParkingOffice parkingOffice;
    private final ParameterCheckUtilities check;
    private static final Logger logger = Logger.getLogger(RegisterCustomerCommand.class.getName());

    private final String commandName = "CUSTOMER";
    private final String displayName = "Register Customer";

    public RegisterCustomerCommand(RealParkingOffice parkingOffice) {
        this.parkingOffice = parkingOffice;
        check = new ParameterCheckUtilities(parkingOffice);
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
        // Ensure enough info is present to create a Customer (lastName)
        String lastName = ParameterCheckUtilities.checkName(params.getProperty("lastname"));

        if (lastName == null || lastName.isBlank()) {
            logger.info("Cannot create customer without an acceptable last name");
            logger.info("Properties: " + params);
            throw new IllegalArgumentException("Missing last name for customer");
        }
        // Create the customer (note that here a Builder pattern for customer might help
        Customer customer = new Customer();
        customer.setFirstName(ParameterCheckUtilities.checkName(params.getProperty("firstname", "")));
        customer.setLastName(ParameterCheckUtilities.checkName(params.getProperty("lastname")));
        customer.setPhoneNumber(ParameterCheckUtilities.checkName(params.getProperty("phonenumber", "")));
        logger.info("Registering customer " + customer);
        // Register the customer
        return parkingOffice.register(customer);
    }

}
