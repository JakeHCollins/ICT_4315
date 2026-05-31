package edu.du.ict4315.parking.charges.decorator;

import java.time.LocalDateTime;
import java.util.List;

import edu.du.ict4315.currency.Money;
import edu.du.ict4315.parking.Address;
import edu.du.ict4315.parking.ParkingLot;
import edu.du.ict4315.parking.ParkingLotChargeOnExit;
import edu.du.ict4315.parking.ParkingPermit;
import java.time.Duration;
import java.time.LocalTime;
import java.util.logging.Logger;

public class FlatRateCalculator extends ParkingChargeCalculator {
    private static final Logger logger = Logger.getLogger(FlatRateCalculator.class.getName());

    private String description = "Flat rate";

    @Override
    public Money getParkingCharge(LocalDateTime in, LocalDateTime out, ParkingPermit p, ParkingLot lot) {
        
        Duration duration = Duration.ZERO;
        if (in != null && out != null ) {
            duration = Duration.between(in, out);
        }
        Money amount = lot.getBaseRate();
        for ( LocalDateTime start = in.plusDays(1).toLocalDate().atStartOfDay();
                start.isBefore(out);
                start = start.plusDays(1)) {
            logger.info("    One more loop...");
            amount = Money.add(amount, lot.getBaseRate());
        }

        logger.info("in: "+in+"; out: "+out+"; lot: "+lot+"; amount: "+amount);
        
        return amount;
    }

    public List<String> getDescription() {
        List<String> list = super.getDescription();
        list.add(description);
        return list;
    }

    @Override
    public String toString() {
        return String.join(" ", getDescription());
    }

}
