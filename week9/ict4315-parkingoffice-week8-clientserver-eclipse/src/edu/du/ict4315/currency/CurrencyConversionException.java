package edu.du.ict4315.currency;

public class CurrencyConversionException extends RuntimeException {

    private static final long serialVersionUID = -6459223342242685383L;

    public CurrencyConversionException(String message) {
        super(message);
    }
}
