package no.ntnu.idata2001.g23.exceptions.unchecked;

/**
 * Thrown when an element in a collection does not exist.
 */
public class ElementNotFoundException extends RuntimeException {
    public ElementNotFoundException(String message) {
        super(message);
    }
}
