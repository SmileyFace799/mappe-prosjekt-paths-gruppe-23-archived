package no.ntnu.idata2001.g23.exceptions.unchecked;

/**
 * Thrown when items are attempted added to an inventory, after it is already full.
 */
public class FullInventoryException extends RuntimeException {
    public FullInventoryException(String message) {
        super(message);
    }
}
