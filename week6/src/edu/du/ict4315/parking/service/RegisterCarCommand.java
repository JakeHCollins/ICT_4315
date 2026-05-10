package edu.du.ict4315.parking.service;

import edu.du.ict4315.parking.Car;
import edu.du.ict4315.parking.CarType;
import edu.du.ict4315.parking.Command;
import edu.du.ict4315.parking.Customer;
import edu.du.ict4315.parking.RealParkingOffice;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterCarCommand implements Command {

    private static final Logger logger = Logger.getLogger(RegisterCarCommand.class.getName());

    private final RealParkingOffice office;

    public RegisterCarCommand(RealParkingOffice office) {
        this.office = office;
    }

    @Override
    public String getCommandName() {
        return "CAR";
    }

    @Override
    public String getDisplayName() {
        return "Register Car";
    }

    @Override
    public String execute(Properties params) throws IllegalArgumentException {
        checkParameters(params);

        String licensePlate = params.getProperty("license_plate");
        String customerId   = params.getProperty("customer");
        String carTypeStr   = params.getProperty("carType", "COMPACT").toUpperCase();

        Customer owner = office.getCustomer(customerId);
        if (owner == null) {
            throw new IllegalArgumentException(
                    "Customer with id '" + customerId + "' not found");
        }

        CarType carType;
        try {
            carType = CarType.valueOf(carTypeStr);
        } catch (IllegalArgumentException e) {
            logger.log(Level.INFO, "Unknown car type ''{0}'', defaulting to COMPACT", carTypeStr);
            carType = CarType.COMPACT;
        }

        Car car = new Car(carType, licensePlate, owner);
        String permitId = office.register(car);
        logger.log(Level.INFO, "Registered car, permit id: {0}", permitId);
        return permitId;
    }

    private void checkParameters(Properties params) {
        if (params == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        if (isBlank(params.getProperty("license_plate"))) {
            throw new IllegalArgumentException("license_plate is required");
        }
        if (isBlank(params.getProperty("customer"))) {
            throw new IllegalArgumentException("customer is required");
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}