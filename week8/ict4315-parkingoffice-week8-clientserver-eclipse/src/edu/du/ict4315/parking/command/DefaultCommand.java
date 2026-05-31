/*
 * Course ICT4315
 * Author: Instructor
 * This class handles all unregistered commands
 */
package edu.du.ict4315.parking.command;

import edu.du.ict4315.parking.support.PropertiesUtilities;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michael
 */
public class DefaultCommand implements Command {

    private static final Logger logger = Logger.getLogger(DefaultCommand.class.getName());

    private String registerName;

    public DefaultCommand(String name) {
        registerName = PropertiesUtilities.displayNameToPropertyName(name);
    }

    @Override
    public String getCommandName() {
        return "No Such Command";
    }

    @Override
    public String getDisplayName() {
        return "This command name is not supported (" + registerName + ")";
    }

    @Override
    public String execute(Properties params) {
        logger.log(Level.SEVERE, "Requested unsupported command: " + registerName);
        return getDisplayName();
    }

}
