package no.ntnu.idata2001.g23.model.actions;

import no.ntnu.idata2001.g23.model.entities.Entity;
import no.ntnu.idata2001.g23.model.itemhandling.FullInventoryException;

/**
 * Represents a future change in the player's state. Changes the player's state along the way.
 */
public interface Action {
    void execute(Entity entity) throws FullInventoryException;
}
