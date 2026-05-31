package edu.du.ict4315.parking.proxy;

import java.time.LocalDateTime;

import edu.du.ict4315.parking.Address;
import edu.du.ict4315.parking.Car;
import edu.du.ict4315.parking.Customer;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingOfficeClientProxy;
import edu.du.ict4315.parking.ParkingPermit;
import edu.du.ict4315.parking.ParkingTransaction;

// TODO: Perhaps both proxies need a user.
// If the client had a user, getParkingCharges could be provided to the client (own user).
// Also, getCustomer could be restricted to just hirself.
public class ClientProxy implements ParkingOfficeClientProxy {

  private ParkingOfficeClientProxy realOffice;
  
  public ClientProxy(ParkingOfficeClientProxy office) {
    realOffice = office;
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

}
