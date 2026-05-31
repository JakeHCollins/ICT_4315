/*
 * Course ICT4315
 * Author: Instructor
 */
package edu.du.ict4315.parking.command;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingPermit;
import edu.du.ict4315.parking.RealParkingOffice;
import edu.du.ict4315.parking.support.ParameterCheckUtilities;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * File: ParkCommand.java
 *
 * @author michael
 */
public class ParkCommand implements Command {

  private final String commandName = "PARK";
  private final String displayName = "Park Car";

  private static final Logger logger = Logger.getLogger(ParkCommand.class.getName());

  private final RealParkingOffice parkingOffice;
  private final ParameterCheckUtilities check;

  public ParkCommand(RealParkingOffice office) {
    parkingOffice = office;
    check = new ParameterCheckUtilities(office);
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
    // Parking requires a dateTime, permit, and lot
    String result = "TODO";
    LocalDateTime dateTime = LocalDateTime.now();
    String ldt = params.getProperty("localdatetime", "");

    // If no localdatetime is provided, use the current time
    if (!ldt.isEmpty()) {
      dateTime = LocalDateTime.parse(ldt);
    }

    String permitId = params.getProperty("permitid", "");
    ParkingPermit permit = check.checkParkingPermit(permitId);
    String lotId = params.getProperty("lotid", "");
    ParkingLot lot = check.checkParkingLot(lotId);

    // Check for missing parameters
    if (permit == null) {
      logger.info("Can't park: missing parking permit id");
      throw new IllegalArgumentException("Can't park: Missing permit");
    }

    if (lot == null) {
      logger.info("Can't park: missing lot id");
      throw new IllegalArgumentException("Can't park: Missing lot");
    }

    // Call park()
    LocalDateTime entry = lot.getEntryTime(permit);
    Duration duration = Duration.ZERO;
    Money charges = Money.of(0);
    if (entry == null) { // Car not in lot
      if (lot.enterLot(dateTime, permit.getId())) {
        charges = lot.getParkingCharges(permit, dateTime, duration);
      }
    } else { // Car is in the lot
      lot.exitLot(entry, dateTime, permitId);
      duration = Duration.between(entry, dateTime);
      charges = lot.getParkingCharges(permit, entry, duration);
    }

    System.out.println(charges);
    logger.info("Parking car: ");
    return charges.toString();
  }

}
