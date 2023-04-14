package no.ntnu.idata2001.g23.model.actions;

import no.ntnu.idata2001.g23.exceptions.unchecked.NullValueException;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.itemhandling.FullInventoryException;
import no.ntnu.idata2001.g23.model.items.Item;

/**
 * Adds an item to the players inventory.
 */
public class InventoryAction implements Action {
    private final Item item;

    /**
     * Makes an inventory action.
     *
     * @param item The item to add to the player's inventory.
     */
    public InventoryAction(Item item) {
        if (item == null) {
            throw new NullValueException("\"item\" cannot be null");
        }
        this.item = item;
    }

    @Override
    public void execute(Player player) throws FullInventoryException {
        player.getInventory().addItem(item);
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
        InventoryAction inventoryAction = (InventoryAction) obj;
        return item.equals(inventoryAction.item);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + item.hashCode();
        return hash;
    }
}
