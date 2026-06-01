/*
 * Course ICT4315
 * Author: Instructor
 */
package edu.du.ict4315.parking.command;

import edu.du.ict4315.parking.Car;
import edu.du.ict4315.parking.CarType;
import edu.du.ict4315.parking.Customer;
import edu.du.ict4315.parking.RealParkingOffice;
import edu.du.ict4315.parking.support.ParameterCheckUtilities;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author michael
 */
public class RegisterCarCommand implements Command {

    private static final Logger logger = Logger.getLogger(RegisterCarCommand.class.getName());

    private final RealParkingOffice parkingOffice;
    private final ParameterCheckUtilities check;

    private final String commandName = "CAR";
    private final String displayName = "Register Car";

    public RegisterCarCommand(RealParkingOffice parkingOffice) {
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
        // Requires a licensePlate and a customer id
        String licensePlate = ParameterCheckUtilities.checkLicensePlate(params.getProperty("license"));
        String customerId = params.getProperty("customer");

        if (licensePlate == null) {
            logger.info("Can't register car: missing license");
            throw new IllegalArgumentException("Can't register car: Missing license plate");
        }

        if (customerId == null) {
            logger.info("Can't register car: missing customer id");
            throw new IllegalArgumentException("Can't register car: Missing customer");
        }

        Customer customer = check.checkCustomer(customerId);
        Car car = new Car(CarType.SUV, licensePlate, customer);

        logger.info("Registering car " + car);

        return parkingOffice.register(car);
    }

}
