package edu.du.ict4315.parking.clients;

@SuppressWarnings("serial")
public class CommandException extends RuntimeException {

    public CommandException(String message) {
        super(message);
    }
}
