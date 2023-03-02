package no.ntnu.idata2001.g23.exceptions.unchecked;

/**
 * Thrown when a number is unexpectedly negative.
 */
public class NegativeNumberException extends RuntimeException {
    public NegativeNumberException(String message) {
        super(message);
    }
}
