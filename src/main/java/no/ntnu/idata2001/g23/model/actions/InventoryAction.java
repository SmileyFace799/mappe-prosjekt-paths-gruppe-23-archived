package no.ntnu.idata2001.g23.model.actions;

import no.ntnu.idata2001.g23.model.entities.Entity;
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
            throw new IllegalArgumentException("\"item\" cannot be null");
        }
        this.item = item;
    }

    @Override
    public void execute(Entity entity) {
        entity.getInventory().addItem(item);
    }

    @Override
    public String getDetails() {
        return "Gain \"" + item.getName() + "\"";
    }

    /**
     * Test for content equality between two objects.
     *
     * @param obj The object to compare to this one
     * @return True if the argument object is an inventory action with matching parameters
     */
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

    /**
     * Compute a hashCode using the rules found in "Effective java" by Joshua Bloch.
     *
     * @return A hashCode for the inventory action, using all its parameters
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + item.hashCode();
        return hash;
    }
}
