package no.ntnu.idata2001.g23.exceptions.checked;

/**
 * Thrown when items are attempted added to an inventory, after it is already full.
 */
public class FullInventoryException extends Exception {
    public FullInventoryException(String message) {
        super(message);
    }
}
