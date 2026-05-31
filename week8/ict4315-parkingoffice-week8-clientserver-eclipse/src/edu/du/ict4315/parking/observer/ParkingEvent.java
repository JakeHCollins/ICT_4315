package edu.du.ict4315.parking.observer;

import java.time.LocalDateTime;

import edu.du.ict4315.parking.ParkingLot;

public class ParkingEvent {

    private final ParkingLot parkingLot;
    private final LocalDateTime in;
    private final LocalDateTime out;
    private final String parkingPermitId;
    private final boolean exit;

    public ParkingEvent(ParkingLot lot, LocalDateTime in, LocalDateTime out, String permitId, boolean isExit) {
        parkingLot = lot;
        this.in = in;
        if (out == null) {
            this.out = LocalDateTime.now();
        } else {
            this.out = out;
        }
        this.parkingPermitId = permitId;
        this.exit = isExit;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public LocalDateTime getIn() {
        return in;
    }

    public LocalDateTime getOut() {
        return out;
    }

    public String getParkingPermitId() {
        return parkingPermitId;
    }

    public boolean isExit() {
        return exit;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Parking event:");
        sb.append("\n");
        sb.append("    Lot id:    ");
        sb.append(parkingLot.getId());
        sb.append("\n");
        sb.append("    Time in:   ");
        sb.append(in);
        sb.append("\n");
        if (out != null) {
            sb.append("    Time out:  ");
            sb.append(out);
            sb.append("\n");
        }
        sb.append("    Permit id: ");
        sb.append(parkingPermitId);
        return sb.toString();
    }

}
