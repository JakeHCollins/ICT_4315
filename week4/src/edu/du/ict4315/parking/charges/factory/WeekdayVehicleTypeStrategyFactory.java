package edu.du.ict4315.parking.charges.factory;

import edu.du.ict4315.parking.charges.strategy.ParkingChargeStrategy;
import edu.du.ict4315.parking.charges.strategy.WeekdayVehicleTypeStrategy;

public class WeekdayVehicleTypeStrategyFactory implements ParkingChargeStrategyFactory {

    @Override
    public ParkingChargeStrategy getStrategy() {
        return new WeekdayVehicleTypeStrategy();
    }
}