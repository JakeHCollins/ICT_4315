/*
 * Course ICT4315
 * Author: Instructor
 */
package edu.du.ict4315.parking.support;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michael
 */
public class PropertiesUtilities {

    // Pairs are expected to be key=value, separated by new lines
    public static Properties loadProperties(String kvPairs) {
        Properties properties = new Properties();
        InputStream is = new ByteArrayInputStream(kvPairs.getBytes());

        try {
            properties.load(is);
            is.close();
        }
        catch (IOException ex) {
            Logger.getLogger(PropertiesUtilities.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalArgumentException("Properties not loaded. Improperly formatted");
        }

        return properties;
    }

    // Utility for turning "First Name" into "firstname"
    public static String displayNameToPropertyName(String key) {
        // Remove spaces, make lower case (word characters only)
        // If using Unicode, perhaps \\s would be better
        return key.replaceAll("\\W", "").toLowerCase();
    }
}
