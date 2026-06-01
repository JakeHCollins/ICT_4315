/**
 *
 */
package edu.du.ict4315.parking.support;

import java.util.Properties;

/**
 * The Command interface allows Command objects to be used as a collection, even
 * though the implementations are completely unrelated. Parameters are provided
 * to execute() via a Properties object. Parameter values are required to be
 * Strings, and the Command objects might require them to be converted to a
 * different kind of object.
 *
 * @author michael
 *
 */
public interface Command {

    public String getCommandName();

    public String getDisplayName();

    public String execute(Properties parameters);
}
