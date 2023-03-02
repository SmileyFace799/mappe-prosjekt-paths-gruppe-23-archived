package no.ntnu.idata2001.g23.goals;

import no.ntnu.idata2001.g23.entities.Player;
import no.ntnu.idata2001.g23.exceptions.unchecked.NullValueException;
import no.ntnu.idata2001.g23.itemhandling.Inventory;

/**
 * Represents an expected inventory of items.
 */
public class InventoryGoal implements Goal {
    private final Inventory mandatoryItems;

    /**
     * Makes an inventory goal for the player to achieve.
     *
     * @param mandatoryItems The items that the player must possess to complete this goal.
     */
    public InventoryGoal(Inventory mandatoryItems) {
        if (mandatoryItems == null) {
            throw new NullValueException("mandatory items cannot be null");
        }
        this.mandatoryItems = mandatoryItems;
    }

    @Override
    public boolean isFulfilled(Player player) {
        return player.getInventory().hasItems(mandatoryItems);
    }
}