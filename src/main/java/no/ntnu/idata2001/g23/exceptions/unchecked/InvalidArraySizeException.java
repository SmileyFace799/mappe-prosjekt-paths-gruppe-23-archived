package no.ntnu.idata2001.g23.exceptions.unchecked;

/**
 * Thrown when an array is of an unexpected size.
 */
public class InvalidArraySizeException extends RuntimeException {
    public InvalidArraySizeException(String message) {
        super(message);
    }
}
