package edu.du.ict4315.parking;

import java.util.Properties;

/**
 * Command interface for the Parking Office command pattern.
 * All (non-legacy) parking service commands will implement this interface.
 */
public interface Command {

    String getCommandName();

    String getDisplayName();

    String execute(Properties params) throws IllegalArgumentException;
}