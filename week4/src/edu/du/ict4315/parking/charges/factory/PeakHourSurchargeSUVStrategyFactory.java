package edu.du.ict4315.parking.charges.factory;

import edu.du.ict4315.parking.charges.strategy.ParkingChargeStrategy;
import edu.du.ict4315.parking.charges.strategy.PeakHourSurchargeSUVStrategy;

public class PeakHourSurchargeSUVStrategyFactory implements ParkingChargeStrategyFactory {

    @Override
    public ParkingChargeStrategy getStrategy() {
        return new PeakHourSurchargeSUVStrategy();
    }
}