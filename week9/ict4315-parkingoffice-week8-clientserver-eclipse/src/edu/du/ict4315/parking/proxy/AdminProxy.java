package edu.du.ict4315.parking.proxy;

import java.time.LocalDateTime;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.Address;
import edu.du.ict4315.parking.Car;
import edu.du.ict4315.parking.Customer;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingOfficeAdminProxy;
import edu.du.ict4315.parking.ParkingPermit;
import edu.du.ict4315.parking.ParkingTransaction;
import edu.du.ict4315.parking.support.User;
import edu.du.ict4315.parking.support.UserRole;

public class AdminProxy implements ParkingOfficeAdminProxy{
  ParkingOfficeAdminProxy realOffice;
  User adminUser;
  
  public AdminProxy(ParkingOfficeAdminProxy office, User user) {
    realOffice = office;
    adminUser = user;
  }

  @Override
  public ParkingTransaction park(LocalDateTime in, ParkingPermit permit, ParkingLot lot) {
    return realOffice.park(in, permit, lot);
  }

  @Override
  public Customer getCustomer(String id) {
    return realOffice.getCustomer(id);
  }

  @Override
  public ParkingPermit getParkingPermit(String id) {
    return realOffice.getParkingPermit(id);
  }

  @Override
  public ParkingLot getParkingLot(String id) {
    return realOffice.getParkingLot(id);
  }

  @Override
  public Address getParkingOfficeAddress() {
    return realOffice.getParkingOfficeAddress();
  }

  @Override
  public String register(Car car) {
    return realOffice.register(car);
  }

  @Override
  public String register(Customer customer) {
    return realOffice.register(customer);
  }

  @Override
  public String getParkingOfficeName() {
    return realOffice.getParkingOfficeName();
  }

  @Override
  public void setParkingOfficeAddress(Address addr) {
    if ( adminUser != null && adminUser.getRole() == UserRole.ADMIN ) {
      realOffice.setParkingOfficeAddress(addr);
    }    
  }

  @Override
  public void setParkingOfficeName(String name) {
    if (adminUser != null && adminUser.getRole() == UserRole.ADMIN) {
      realOffice.setParkingOfficeName(name);
    }
  }

  @Override
  public Money getParkingCharges(Customer customer) {
    return realOffice.getParkingCharges(customer);
  }

  @Override
  public Money getParkingCharges(ParkingPermit permit) {
    return realOffice.getParkingCharges(permit);
  }
}
