package edu.du.ict4315.parking.charges.factory;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.Car;
import edu.du.ict4315.parking.CarType;
import edu.du.ict4315.parking.Customer;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingPermit;
import edu.du.ict4315.parking.RealParkingOffice;
import edu.du.ict4315.parking.charges.decorator.ParkingChargeCalculator;
import java.time.LocalDateTime;

// More a smoke test than a useful main.
public class Main {

  private static final Class<?>[] factories = {
    FactoryDesignatedDayNineToSix.class,
    FactoryFavorLargeCarsLateEntry.class,
    FactoryFavorSmallCarsEarlyIn.class,
    FactoryFlatRate.class,
    FactoryOriginalAlgorithm.class
  };

  public static RealParkingOffice parkingOffice;
  public static String customerId;
  public static String car1Id;
  public static String car2Id;

  public static void initOffice() {
    parkingOffice = new RealParkingOffice();
    // Create a customer
    Customer customer = new Customer();
    customer.setFirstName("John");
    customer.setLastName("Doe");
    customer.setPhoneNumber("303-333-1111");
    customerId = parkingOffice.register(customer);
    // Give the customer two cars
    Car car = new Car();
    car.setLicensePlate("ABC-123");
    car.setOwner(customer);
    car.setType(CarType.SUV);
    car1Id = parkingOffice.register(car);
    car = new Car();
    car.setLicensePlate("DEF-456");
    car.setOwner(customer);
    car.setType(CarType.COMPACT);
    car2Id = parkingOffice.register(car);
  }

  public static void main(String[] args) throws Exception {

    for (Class<?> cls : factories) {
      FactoryParkingChargeCalculator f = (FactoryParkingChargeCalculator) cls.
          getDeclaredConstructor().newInstance();

      ParkingChargeCalculator calculator = f.getCalculator();
      System.out.println(calculator);
    }

    initOffice();

    LocalDateTime entry = LocalDateTime.parse("2022-02-06T09:00:01");
    LocalDateTime leave2h = LocalDateTime.parse("2022-02-06T11:00:01");
    ParkingLot lot = parkingOffice.getParkingLot("W");

    String[] carIds = {car1Id, car2Id};
    FactoryParkingChargeCalculator[] factories = {
      new FactoryDesignatedDayNineToSix(),
      new FactoryFavorLargeCarsLateEntry(),
      new FactoryFavorSmallCarsEarlyIn(),
      new FactoryFlatRate(),
      new FactoryOriginalAlgorithm()
    };

    System.out.println("\n\nBeginning test\n");
    for (FactoryParkingChargeCalculator factory : factories) {
      System.out.println("Using factory " + factory);
      lot.setParkingChargeCalculatorFactory(factory);
      for (String carId : carIds) {
        ParkingPermit permit = parkingOffice.getParkingPermit(carId);
        if (lot.enterLot(entry, carId)) {
          // Ready to read charges
        } else {
          lot.exitLot(lot.getEntryTime(permit), leave2h, carId);
          // log.getEntryTime(car1Id) is the same as entry.
        }

        Money amount = parkingOffice.getParkingCharges(permit);
        System.out.println("Car: " + permit + " charged " + amount);

      }
    }
  }

}
