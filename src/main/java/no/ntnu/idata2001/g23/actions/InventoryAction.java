package no.ntnu.idata2001.g23.actions;

import no.ntnu.idata2001.g23.entities.Player;
import no.ntnu.idata2001.g23.exceptions.unchecked.NullValueException;
import no.ntnu.idata2001.g23.items.Item;

/**
 * Adds an item to the players inventory.
 */
public class InventoryAction implements Action {
    private final Item item;

    public InventoryAction(Item item) {
        if (item == null) {
            throw new NullValueException("\"item\" cannot be null");
        }
        this.item = item;
    }

    @Override
    public void execute(Player player) {
        player.getInventory().addItem(item);
        //TODO: Handle inventory being full
    }
}
