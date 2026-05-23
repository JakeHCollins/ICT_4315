package edu.du.ict4315.parking.charges.factory;

import edu.du.ict4315.parking.charges.decorator.CompactCarDiscountDecorator;
import edu.du.ict4315.parking.charges.decorator.FlatRateCalculator;
import edu.du.ict4315.parking.charges.decorator.ParkingChargeCalculator;
import edu.du.ict4315.parking.charges.decorator.PeakHourSurchargeDecorator;

public class PeakHourCalculatorFactory implements ParkingChargeCalculatorFactory {

    @Override
    public ParkingChargeCalculator getCalculator() {
        return new PeakHourSurchargeDecorator(
                new CompactCarDiscountDecorator(
                        new FlatRateCalculator()));
    }
}