package edu.du.ict4315.parking.charges.factory;

import edu.du.ict4315.parking.charges.decorator.ParkingChargeCalculator;

public interface ParkingChargeCalculatorFactory {
    ParkingChargeCalculator getCalculator();
}