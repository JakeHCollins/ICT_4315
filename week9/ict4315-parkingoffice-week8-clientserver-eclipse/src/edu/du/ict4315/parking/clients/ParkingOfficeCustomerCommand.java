package edu.du.ict4315.parking.clients;

import java.util.Properties;

import edu.du.ict4315.parking.support.Command;
import edu.du.ict4315.parking.Address;
import edu.du.ict4315.parking.Customer;
import edu.du.ict4315.parking.RealParkingOffice;

public class ParkingOfficeCustomerCommand implements Command {

    private RealParkingOffice parkingOffice;

    public ParkingOfficeCustomerCommand(RealParkingOffice office) {
        parkingOffice = office;
    }

    private String commandName = "CUSTOMER";
    private String displayName = "Register Customer";

    private boolean checkParameters(Properties parameters) {
        boolean result = true;

        result = parameters.containsKey("id")
                && parameters.containsKey("firstName")
                && parameters.containsKey("lastName")
                && parameters.containsKey("phoneNumber")
                && parameters.containsKey("streetAddress1")
                && parameters.containsKey("city")
                && parameters.containsKey("state")
                && parameters.containsKey("zip");

        return result;
    }

    @Override
    public String execute(Properties parameters) {
        // Customer id assigned by PO?
        if (checkParameters(parameters)) {
            Customer customer;
            Address address;
            address = new Address.Builder()
                    .withStreetAddress1(parameters.getProperty("streetAddress1"))
                    .withStreetAddress2(parameters.getProperty("streetAddress2"))
                    .withCity(parameters.getProperty("city"))
                    .withState(parameters.getProperty("state"))
                    .withZip(parameters.getProperty("zip"))
                    .build();
            customer = new Customer(parameters.getProperty("id"),
                    parameters.getProperty("firstName"),
                    parameters.getProperty("lastName"),
                    parameters.getProperty("phoneNumber"),
                    address
            );

            parkingOffice.register(customer);
            return customer.getId();
        } else {
            throw new CommandException("Can't parse: " + commandName + "\n" + parameters.toString());
        }
    }

    public String getCommandName() {
        return commandName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
