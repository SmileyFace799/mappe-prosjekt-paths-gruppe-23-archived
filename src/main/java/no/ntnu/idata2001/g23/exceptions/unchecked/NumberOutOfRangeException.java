package no.ntnu.idata2001.g23.exceptions.unchecked;

/**
 * Thrown when a number is unexpectedly outside a specified range.
 */
public class NumberOutOfRangeException extends RuntimeException {
    public NumberOutOfRangeException(String message) {
        super(message);
    }
}
