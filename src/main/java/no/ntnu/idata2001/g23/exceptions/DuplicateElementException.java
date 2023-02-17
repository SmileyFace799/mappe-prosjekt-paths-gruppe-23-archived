package no.ntnu.idata2001.g23.exceptions;

/**
 * Thrown when an element in a collection already exists.
 */
public class DuplicateElementException extends RuntimeException {
    public DuplicateElementException(String errorMessage) {
        super(errorMessage);
    }
}
