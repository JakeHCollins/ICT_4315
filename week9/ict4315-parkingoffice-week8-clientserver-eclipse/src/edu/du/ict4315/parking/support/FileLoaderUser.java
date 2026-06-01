/*
 * Course ICT4315
 * Author: Instructor
 */
package edu.du.ict4315.parking.support;

import edu.du.ict4315.parking.RealParkingOffice;
import static edu.du.ict4315.parking.support.FileLoader.loadCsvFile;
import static edu.du.ict4315.parking.support.FileLoader.writeCsvFile;
import java.util.logging.Logger;

/**
 *
 * @author michael
 */
public class FileLoaderUser {

    private static final Logger logger = Logger.getLogger(FileLoaderUser.class.getName());

    public static void loadCsvUserFile(String filePath, RealParkingOffice office) {
        String[][] records = loadCsvFile(filePath);
        for (String[] record : records) {
            try {
                User.setUser(record);
            }
            catch (Exception ex) {
                logger.severe(
                        "Problem with record [" + String.join(", ", record) + "]. Skipping: " + ex);
            }
        }
    }

    // Only ADMIN personnel can save the user file (example)
    public static void saveCsvUserFile(String filePath, RealParkingOffice office, String username,
            String passwd) {
        String[] header = {"# id", "customerId", "role", "passwdHash"};
        User requester = User.authorizeUser(username, passwd);
        if (requester == null) {
            logger.severe("Error: user " + username + " not authenticated");
            return;
        } else if (requester.getRole() != UserRole.ADMIN) {
            logger.severe("Error: user " + username + " not authorized to overwrite passwords");
            return;
        }
        int count = writeCsvFile(filePath, User.getUserInfo(), header);
        logger.info("Wrote user file with " + count + " records");
    }
}
