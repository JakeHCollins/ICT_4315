package edu.du.ict4315.parking.service;

import edu.du.ict4315.parking.Address;
import edu.du.ict4315.parking.Command;
import edu.du.ict4315.parking.Customer;
import edu.du.ict4315.parking.RealParkingOffice;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterCustomerCommand implements Command {

    private static final Logger logger = Logger.getLogger(RegisterCustomerCommand.class.getName());

    private final RealParkingOffice office;

    public RegisterCustomerCommand(RealParkingOffice office) {
        this.office = office;
    }

    @Override
    public String getCommandName() {
        return "CUSTOMER";
    }

    @Override
    public String getDisplayName() {
        return "Register Customer";
    }

    @Override
    public String execute(Properties params) throws IllegalArgumentException {
        checkParameters(params);

        // Resolve lastName — already validated so at least one of these is non-blank
        String lastName = params.getProperty("lastName");
        if (isBlank(lastName)) {
            lastName = params.getProperty("arg0", "");
        }

        Address address = new Address.Builder()
                .withStreetAddress1(params.getProperty("street1", ""))
                .withStreetAddress2(params.getProperty("street2", ""))
                .withCity(params.getProperty("city", ""))
                .withState(params.getProperty("state", ""))
                .withZip(params.getProperty("zip", ""))
                .build();

        Customer customer = new Customer();
        customer.setFirstName(params.getProperty("firstName", ""));
        customer.setLastName(lastName);
        customer.setPhoneNumber(params.getProperty("phoneNumber", ""));
        customer.setAddress(address);

        String customerId = office.register(customer);
        logger.log(Level.INFO, "Registered customer with id: {0}", customerId);
        return customerId;
    }

    /**
     * @throws IllegalArgumentException if no usable name can be found
     */
    private void checkParameters(Properties params) {
        if (params == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        boolean hasLastName = !isBlank(params.getProperty("lastName"));
        boolean hasPositional = !isBlank(params.getProperty("arg0"));
        if (!hasLastName && !hasPositional) {
            throw new IllegalArgumentException("lastName is required");
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}