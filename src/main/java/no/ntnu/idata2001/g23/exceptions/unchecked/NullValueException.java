package no.ntnu.idata2001.g23.exceptions.unchecked;

/**
 * Thrown when a value is unexpectedly null.
 */
public class NullValueException extends RuntimeException {
    public NullValueException(String message) {
        super(message);
    }
}
