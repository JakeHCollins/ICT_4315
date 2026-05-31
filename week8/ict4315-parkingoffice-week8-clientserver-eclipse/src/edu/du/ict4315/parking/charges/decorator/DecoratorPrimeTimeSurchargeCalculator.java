package edu.du.ict4315.parking.charges.decorator;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingPermit;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Default surcharge is 25% from 9 AM to 6 PM (was DecoratorNineToSix)
 * @author michael
 */
public class DecoratorPrimeTimeSurchargeCalculator extends ParkingChargeCalculatorDecorator {

    private ParkingChargeCalculator calculator;
    private String description = "25% prime-time surcharge";
    int startHourSurcharge = 9;
    int endHourSurcharge = 18;
    double surcharge = 0.25;

    public DecoratorPrimeTimeSurchargeCalculator(ParkingChargeCalculator calc) {
        calculator = calc;
    }
    
    public DecoratorPrimeTimeSurchargeCalculator(ParkingChargeCalculator calc,
            int startHour, int endHour, double surcharge) {
        calculator = calc;
        startHourSurcharge = startHour;
        endHourSurcharge = endHour;
        this.surcharge = surcharge;
    }

    @Override
    public List<String> getDescription() {
        List<String> list = calculator.getDescription();
        list.add(description);
        return list;
    }

    // 25% surcharge if entry from 9 AM to 6 PM
    @Override
    public Money getParkingCharge(LocalDateTime in, LocalDateTime out, ParkingPermit p, ParkingLot lot) {
        Money result = calculator.getParkingCharge(in, out, p, lot);
        if (in != null) {
            if (in.getHour() >= startHourSurcharge && in.getHour() < endHourSurcharge) {
                result = Money.times(result, 1 + surcharge);
            }
        }
        return result;
    }
}
