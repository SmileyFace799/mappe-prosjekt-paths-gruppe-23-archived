package no.ntnu.idata2001.g23.exceptions.unchecked;

/**
 * Thrown when a string is unexpectedly null or blank.
 */
public class BlankStringException extends RuntimeException {
    public BlankStringException(String message) {
        super(message);
    }
}
