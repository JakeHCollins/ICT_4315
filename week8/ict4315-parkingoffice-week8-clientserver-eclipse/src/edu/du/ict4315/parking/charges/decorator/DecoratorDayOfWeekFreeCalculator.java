/*
 * Course: ICT
 * File: 
 * Author: Instructor
 */
package edu.du.ict4315.parking.charges.decorator;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingPermit;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author michael
 */
public class DecoratorDayOfWeekFreeCalculator extends ParkingChargeCalculatorDecorator {

    private final ParkingChargeCalculator calculator;
    List<DayOfWeek> freeDays = List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
    private final String description;

    public DecoratorDayOfWeekFreeCalculator(ParkingChargeCalculator calculator) {
        this.calculator = calculator;
        description = "Free days of week: " + freeDays.toString();
    }

    public DecoratorDayOfWeekFreeCalculator(ParkingChargeCalculator calculator, DayOfWeek[] freeDays) {
        this.calculator = calculator;
        this.freeDays = Arrays.asList(freeDays);
        description = "Free days of week: " + this.freeDays.toString();
    }

    @Override
    public List<String> getDescription() {
        List<String> list = calculator.getDescription();
        list.add(description);
        return list;
    }

    private boolean isFreeDayOfWeek(LocalDateTime d) {
        if (freeDays.contains(d)) {
            return true;
        }
        return false;
    }

    @Override
    public Money getParkingCharge(LocalDateTime in, LocalDateTime out, ParkingPermit p, ParkingLot lot) {
        // We will go through day by day.
        // Loop will start at beginning of 2nd day (if any)
        LocalDateTime start = in.plusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = in.withHour(23).withMinute(59).withSecond(59);

        // First day
        Money result = Money.of("0.00");

        if (!isFreeDayOfWeek(in)) {
            result = Money.add(result, calculator.getParkingCharge(in, endOfDay, p, lot));
        }
        while (start.isBefore(out)) {
            if (!isFreeDayOfWeek(start)) {
                result = Money.add(result, calculator.getParkingCharge(start, endOfDay, p, lot));
            }
            start = start.plusDays(1);
            endOfDay = endOfDay.plusDays(1);
        }
        return result;
    }
}
