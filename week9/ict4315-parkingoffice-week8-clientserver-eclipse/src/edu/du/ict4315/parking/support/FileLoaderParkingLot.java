/*
 * Course ICT4315
 * Author: Instructor
 */
package edu.du.ict4315.parking.support;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.Address;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingLotChargeOnEntry;
import edu.du.ict4315.parking.RealParkingOffice;
import edu.du.ict4315.parking.charges.factory.FactoryOriginalAlgorithm;
import edu.du.ict4315.parking.charges.factory.FactoryParkingChargeCalculator;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michael
 */
public class FileLoaderParkingLot {

    private static final Logger logger = Logger.getLogger(FileLoaderParkingLot.class.getName());

    // TODO: Currently this class only loads ParkingLotChargeOnEntry.
    // Add one more field to choose ParkingLot derived class
    public static ParkingLot[] loadCsvFileParkingLot(String filePath) {
        List<ParkingLot> result = new ArrayList<>();
        String[][] records = FileLoader.loadCsvFile(filePath);
        Address address = null;
        Money baseRate = Money.of(1.00);
        int capacity;
        for (String[] record : records) {
            try {
                address = new Address.Builder()
                        .withStreetAddress1(record[2])
                        .withStreetAddress2(record[3])
                        .withCity(record[4])
                        .withState(record[5])
                        .withZip(record[6])
                        .build();
                // record[8] represents the capacity
                // record[9] specifies the charging algorithm. If a number, it is a flat rate
                double rate = Double.valueOf(record[7]);
                baseRate = Money.of(rate);
            }
            catch (NullPointerException npe) {
                System.err.println("Missing data");
            }
            catch (NumberFormatException nfe) {
                System.err.println("Could not convert to Money: |" + record[7] + "|");
            }
            catch (ArrayIndexOutOfBoundsException oob) {
                System.err.println("Parking lot record format problem: |" + String.join(",", record) + "|");
            }

            if (record.length > 8) {
                capacity = Integer.parseInt(record[8]);
            } else {
                capacity = 50;
            }

            Class<? extends ParkingLot> clazz = ParkingLotChargeOnEntry.class;
            if (record.length > 9) {
                clazz = getParkingLotClass(record[9]);
            }

            ParkingLot lot;
            try {
                lot = clazz.getConstructor(
                        String.class, String.class, Address.class,
                        Money.class, int.class
                ).newInstance(
                        record[0], record[1], address, baseRate, capacity
                );
            }
            catch (NoSuchMethodException
                    | InstantiationException
                    | IllegalAccessException
                    | IllegalArgumentException
                    | InvocationTargetException ex) {
                lot = new ParkingLotChargeOnEntry(record[0], record[1], address,
                        capacity, baseRate);
                Logger.getLogger(FileLoaderParkingLot.class.getName()).log(Level.SEVERE, null, ex);
            }

            result.add(lot);
        }
        return result.toArray(ParkingLot[]::new);
    }

    public static void loadCsvFileParkingLotFactory(String filePath, RealParkingOffice office) {
        String[][] records = FileLoader.loadCsvFile(filePath);
        for (String[] record : records) {
            try {
                ParkingLot lot = office.getParkingLot(record[0]);
                if (lot != null) {
                    FactoryParkingChargeCalculator factory = getFactory(record[1]);
                    if (factory != null) {
                        lot.setParkingChargeCalculatorFactory(factory);
                        logger.info("Lot " + record[0] + " set to factory " + factory.toString());
                    } else {
                        logger.log(Level.INFO, "Factory class |{0}| not found.", record[1]);
                    }
                } else {
                    logger.log(Level.INFO, "Parking lot with id |{0}| not found.", record[0]);
                }

            }
            catch (Exception ex) {
                System.err.println("Problem with record [" + String.join(", ", record) + "] (" + ex.getMessage() + "). Skipping.");
            }
        }
    }

    private static FactoryParkingChargeCalculator getFactory(String name) {
        // Turn the class name into the object

        FactoryParkingChargeCalculator factory;

        try {
            factory = (FactoryParkingChargeCalculator) (Class.forName(name).getDeclaredConstructor().newInstance());
        }
        catch (ClassNotFoundException | NoSuchMethodException | SecurityException
                | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException ex) {
            factory = new FactoryOriginalAlgorithm();
            Logger.getLogger(FileLoaderParkingLot.class.getName()).log(Level.SEVERE, null, ex);
        }

        return factory;
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends ParkingLot> getParkingLotClass(String name) {
        try {
            return (Class<? extends ParkingLot>) (Class.forName(name));
        }
        catch (ClassNotFoundException
                | SecurityException
                | IllegalArgumentException ex) {
            return ParkingLotChargeOnEntry.class;
        }
    }

}
