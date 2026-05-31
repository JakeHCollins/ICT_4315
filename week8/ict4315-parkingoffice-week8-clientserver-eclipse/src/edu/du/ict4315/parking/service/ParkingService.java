///////////////////////////////
// File: ParkingService.java
// Author: Instructor
// This file is the seed for how the ParkingOffice server handles remote commands.
//
// Parking Service Commands:
//     Display name       command name      parameters
//     Register customer  CUSTOMER          first name
//     Register car       CAR               license plate, owner id
//     Park               PARK              permit id, lot id, datetime (now)
//
// TODO: Consider if performCommand should take a Properties object
///////////////////////////////
package edu.du.ict4315.parking.service;

import edu.du.ict4315.parking.RealParkingOffice;
import edu.du.ict4315.parking.command.Command;
import edu.du.ict4315.parking.command.DefaultCommand;
import edu.du.ict4315.parking.command.ListCustomerCommand;
import edu.du.ict4315.parking.command.ListParkingLotCommand;
import edu.du.ict4315.parking.command.ParkCommand;
import edu.du.ict4315.parking.command.RegisterCarCommand;
import edu.du.ict4315.parking.command.RegisterCustomerCommand;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParkingService {

    private static final Logger logger = Logger.getLogger(ParkingService.class.getName());

    public ParkingService(RealParkingOffice parkingOffice) {
        this.parkingOffice = parkingOffice;
        // Register the known commands
        register(new RegisterCarCommand(parkingOffice));
        register(new RegisterCustomerCommand(parkingOffice));
        register(new ParkCommand(parkingOffice));
        register(new ListCustomerCommand(parkingOffice));
        register(new ListParkingLotCommand(parkingOffice));
    }

    protected final RealParkingOffice parkingOffice;

    // Map of the command names to their implementations
    private Map<String, Command> commands = new TreeMap<>();

    private void register(Command command) {
        commands.put(command.getCommandName(), command);
    }

    // Notice how this version of performCommand puts all the logic in the
    // implementation classes. 
    // Shared logic can be put in a support class.
    // To be a bit more robust, all Property keys are converted to lower case.
    // Values are left alone
    public String performCommand(String commandName, String[] args) {
        // Look up the command
        Command command = commands.getOrDefault(commandName, new DefaultCommand(commandName));
        logger.info("Received command: " + command.getDisplayName());
        logger.info("  Args: " + String.join("||", args));
        // Convert the args into a Properties (split on equals, discard if no =)
        Properties properties = new Properties();
        for (String string : args) {
            if (string.isBlank()) {
                break;
            }
            String[] keyValue = string.split("=");
            if (keyValue.length == 2) {
                properties.put(keyValue[0].toLowerCase(), keyValue[1]);
            } else {
                logger.info("Ignoring parameter " + string + ". Malformed.");
            }
        }
        // Execute the command with the properties
        return command.execute(properties);
    }

    public String[] listCommands() {
        return commands.keySet().toArray(String[]::new);
    }

    public String handleInput(String command, Map<String, String> data) {
        logger.log(Level.INFO, "data: {0}", data.toString());
        String[] args = data.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .toArray(String[]::new);
        return performCommand(command, args);
    }
}
