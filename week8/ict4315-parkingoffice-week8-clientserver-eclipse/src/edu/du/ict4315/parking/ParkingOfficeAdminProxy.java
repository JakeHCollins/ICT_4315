/*
 * Course: ICT4315
 * File: ParkingOfficeAdminProxy.java
 * Author: Instructor
 */
package edu.du.ict4315.parking;

/**
 *
 * @author michael
 */
import edu.du.ict4315.currency.Money;

public interface ParkingOfficeAdminProxy extends ParkingOfficeClientProxy {

  void setParkingOfficeAddress(Address addr);

  void setParkingOfficeName(String name);

  Money getParkingCharges(Customer customer);

  Money getParkingCharges(ParkingPermit permit);
  // Interfaces to add, remove ParkingLot, ParkingPermit, Customer, Car from storage

}
