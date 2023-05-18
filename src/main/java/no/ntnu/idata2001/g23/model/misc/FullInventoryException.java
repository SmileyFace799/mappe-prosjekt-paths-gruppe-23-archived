package no.ntnu.idata2001.g23.model.misc;

/**
 * Thrown when items are attempted added to an inventory, after it is already full.
 * TODO: Remove this, obsolete
 */
public class FullInventoryException extends RuntimeException {
    public FullInventoryException(String message) {
        super(message);
    }
}
