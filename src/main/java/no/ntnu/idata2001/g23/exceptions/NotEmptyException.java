package no.ntnu.idata2001.g23.exceptions;

/**
 * Thrown when a collection of objects is unexpectedly not empty.
 */
public class NotEmptyException extends RuntimeException {
    public NotEmptyException(String message) {
        super(message);
    }
}
