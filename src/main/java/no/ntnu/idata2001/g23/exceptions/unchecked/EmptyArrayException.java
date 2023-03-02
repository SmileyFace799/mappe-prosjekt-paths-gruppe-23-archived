package no.ntnu.idata2001.g23.exceptions.unchecked;

/**
 * Thrown when an array is unexpectedly null or empty.
 */
public class EmptyArrayException extends RuntimeException {
    public EmptyArrayException(String message) {
        super(message);
    }
}
