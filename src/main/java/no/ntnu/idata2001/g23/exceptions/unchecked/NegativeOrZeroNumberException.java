package no.ntnu.idata2001.g23.exceptions.unchecked;

/**
 * Thrown when a number is unexpectedly zero or negative.
 */
public class NegativeOrZeroNumberException extends RuntimeException {
    public NegativeOrZeroNumberException(String message) {
        super(message);
    }
}
