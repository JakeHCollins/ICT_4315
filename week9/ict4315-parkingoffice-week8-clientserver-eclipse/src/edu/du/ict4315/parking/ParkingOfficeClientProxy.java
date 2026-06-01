/*
 * Course: ICT4315
 * File: ParkingOfficeClientProxy.java
 * Author: Instructor
 */
package edu.du.ict4315.parking;

/**
 *
 * @author michael
 */
import java.time.LocalDateTime;

/*
 * Trying to keep the interface to the minimum.
 * TODO: Should this be an abstract base class instead?
 */
public interface ParkingOfficeClientProxy {

  ParkingTransaction park(LocalDateTime in, ParkingPermit permit, ParkingLot lot);

  Customer getCustomer(String id);

  ParkingPermit getParkingPermit(String id);

  ParkingLot getParkingLot(String id);

  Address getParkingOfficeAddress();

  String register(Car car);

  String register(Customer customer);

  String getParkingOfficeName();
}
