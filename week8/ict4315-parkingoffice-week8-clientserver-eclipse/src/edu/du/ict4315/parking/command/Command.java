//////////////////////////
// File: Command.java
// Author: Instructor
// This file represents the commands the server supports.
//////////////////////////
package edu.du.ict4315.parking.command;

import java.util.Properties;

public interface Command {

    public String getCommandName();

    public String getDisplayName();

    public String execute(Properties params);
}
