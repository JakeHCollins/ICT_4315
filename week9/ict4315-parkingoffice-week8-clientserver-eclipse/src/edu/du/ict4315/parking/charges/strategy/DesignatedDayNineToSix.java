package edu.du.ict4315.parking.charges.strategy;

/*
 * This is one parking charge strategy implementation
 * Author: M I Schwartz
 */
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.ParkingPermit;
import java.time.Duration;

// Doubles the cost on designated days
// When parking between 9 and 6, adds 25% for that day
// Used for graduations and other special events and the nearby lots
public class DesignatedDayNineToSix implements ParkingChargeStrategy {

    private static LocalDate[] negotiatedDays = {
        LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 17),
        LocalDate.of(2022, 2, 21), LocalDate.of(2022, 5, 30),
        LocalDate.of(2022, 6, 20), LocalDate.of(2022, 7, 4),
        LocalDate.of(2022, 9, 5), LocalDate.of(2022, 10, 10),
        LocalDate.of(2022, 11, 11), LocalDate.of(2022, 12, 25),
        LocalDate.of(2022, 12, 26)
    };
    private Set<LocalDate> designatedDays = new HashSet<>(Arrays.asList(negotiatedDays));

    private Money dayCharge(LocalDateTime time, Money baseRate) {
        Money result = baseRate;
        if (designatedDays.contains(time.toLocalDate())) {
            result = Money.times(result, 2);
        }
        if (time.getHour() >= 9 && time.getHour() < 18) {
            result = Money.add(result, Money.div(baseRate, 4));
        }

        return result;
    }

    @Override
    public Money getParkingCharge(Money baseRate, LocalDateTime in,
            Duration duration, ParkingPermit p) {

        Money charges = dayCharge(in, baseRate);
        LocalDateTime out = in.plus(duration);

        LocalDateTime day = in.plusDays(1).toLocalDate().atStartOfDay();
        while (day.isBefore(out)) {
            charges = Money.add(charges, dayCharge(day, baseRate));
            day = day.plusDays(1L);
        }
        return charges;
    }

    public boolean addSpecialDay(LocalDate date) {
        return designatedDays.add(date);
    }

    public boolean removeSpecialDay(LocalDate date) {
        return designatedDays.remove(date);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Designated days and times charging strategy");
        return sb.toString();
    }

}
