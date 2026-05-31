/*
 * Course ICT4315
 * Author: Instructor
 */
package edu.du.ict4315.parking;

import java.time.LocalDateTime;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author michael
 */
public class PermitList {

    private final Map<String, LocalDateTime> permitList;

    public PermitList() {
        permitList = new HashMap<>();
    }

    public int addCar(String permitId, LocalDateTime in) {
        permitList.put(permitId, in);
        return permitList.size();
    }

    public int removeCar(String permitId) {
        LocalDateTime in = permitList.remove(permitId);
        if (in == null) {
            // Car with that permit was never added!
        }
        return permitList.size();
    }

    public LocalDateTime getLocalDateTime(String permitId) {
        return permitList.get(permitId);
    }

    public int size() {
        return permitList.size();
    }

    public String[] getList() {
        return permitList.keySet().toArray(String[]::new);
    }
}
