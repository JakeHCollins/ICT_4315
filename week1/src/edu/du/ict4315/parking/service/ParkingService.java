package edu.du.ict4315.parking.service;

import edu.du.ict4315.parking.Command;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingPermit;
import edu.du.ict4315.parking.ParkingTransaction;
import edu.du.ict4315.parking.RealParkingOffice;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParkingService {

    private final RealParkingOffice parkingOffice;
    private final Map<String, Command> commands = new HashMap<>();

    private static final Logger logger = Logger.getLogger(ParkingService.class.getName());

    public ParkingService(RealParkingOffice parkingOffice) {
        this.parkingOffice = parkingOffice;

        // NOTE: Need to add new commands here to register as we convert the legacy ones or add new ones.
        List<Function<RealParkingOffice, Command>> factories = List.of(RegisterCustomerCommand::new, RegisterCarCommand::new);
        factories.forEach(f -> register(f.apply(parkingOffice)));
    }

    /**
     * Registers a Command so it can be dispatched by performCommand().
     */
    public void register(Command command) {
        commands.put(command.getCommandName(), command);
        logger.log(Level.INFO, "Registered command: {0}", command.getCommandName());
    }

    /**
     * Dispatches the named command using the provided positional/key=value arguments.
     * Returns a result string prefixed with the command name.
     * If the command throws IllegalArgumentException, the error message is returned instead.
     */
    public String performCommand(String command, String[] args) {
        logger.log(Level.INFO, "Performing {0} command with args: {1}", new Object[]{command, Arrays.toString(args)});

        Properties params = parseArgs(args);

        Command cmd = commands.get(command);
        if (cmd != null) {
            try {
                return command + ": " + cmd.execute(params);
            } catch (IllegalArgumentException e) {
                logger.log(Level.WARNING, "Command {0} failed: {1}", new Object[]{command, e.getMessage()});
                return command + ": Cannot execute - " + e.getMessage();
            }
        }

        return switch (command) {
            case "PARK" -> handlePark(params);
            case "CHARGES" -> command + ": Charges not implemented yet";
            default -> command + ": Command unknown";
        };
    }

    /**
     * Converts a "key=value" args array into a Properties object.
     * Still allows direct arg usage and slots them as positions like arg0, arg1, ... .
     */
    private static Properties parseArgs(String[] args) {
        Properties props = new Properties();
        int positional = 0;
        if (args == null) return props;
        for (String arg : args) {
            String[] parts = arg.split("=", 2);
            // args with key=value
            if (parts.length == 2) {
                props.setProperty(parts[0].trim(), parts[1].trim());
            } else {
                // args set in direct positions
                props.setProperty("arg" + positional, arg.trim());
                positional++;
            }
        }
        return props;
    }

    private String handlePark(Properties params) {
        String lotId = params.getProperty("lotId", params.getProperty("arg0"));
        String permitId = params.getProperty("permitId", params.getProperty("arg1"));
        String dateStr = params.getProperty("dateTime", params.getProperty("arg2"));

        if (lotId == null || permitId == null || dateStr == null) {
            return "PARK: Cannot park - wrong number of parameters";
        }

        LocalDateTime dateTime = parseDateTime(dateStr);
        ParkingLot lot = parkingOffice.getParkingLot(lotId);
        ParkingPermit permit = parkingOffice.getParkingPermit(permitId);

        ParkingTransaction transaction = parkingOffice.park(dateTime, permit, lot);
        if (transaction != null) {
            return "PARK: " + transaction.getChargedAmount().toString();
        }
        return "PARK: Parking transaction failed - parameters invalid";
    }

    private static LocalDateTime parseDateTime(String dateTime) {
        try {
            return LocalDateTime.parse(dateTime);
        } catch (DateTimeParseException ex) {
            logger.log(Level.INFO, "Cannot parse datetime {0}", dateTime);
            return LocalDateTime.now();
        }
    }
}