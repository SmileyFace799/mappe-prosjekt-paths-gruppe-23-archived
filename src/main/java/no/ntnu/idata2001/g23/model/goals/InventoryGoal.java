package no.ntnu.idata2001.g23.model.goals;

import java.util.Objects;
import no.ntnu.idata2001.g23.exceptions.unchecked.NullValueException;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.items.Item;

/**
 * Represents an expected inventory of items.
 */
public class InventoryGoal implements Goal {
    private final Item mandatoryItem;

    /**
     * Makes an inventory goal for the player to achieve.
     *
     * @param mandatoryItem The item that the player must possess to complete this goal.
     */
    public InventoryGoal(Item mandatoryItem) {
        if (mandatoryItem == null) {
            throw new NullValueException("Item \"mandatoryItem\" cannot be null");
        }
        this.mandatoryItem = mandatoryItem;
    }

    @Override
    public boolean isFulfilled(Player player) {
        return player.getInventory().hasItem(mandatoryItem);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        InventoryGoal inventoryGoal = (InventoryGoal) obj;
        return Objects.equals(mandatoryItem, inventoryGoal.mandatoryItem);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + mandatoryItem.hashCode();
        return hash;
    }
}