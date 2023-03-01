package no.ntnu.idata2001.g23.actions;

import no.ntnu.idata2001.g23.entities.Player;
import no.ntnu.idata2001.g23.items.Item;

/**
 * Adds an item to the players inventory.
 */
public class InventoryAction implements Action {
    private Item item;

    InventoryAction(Item item) {
        this.item = item;
    }
    @Override
    public void execute(Player player) {
        int index = 0;
        while (player.getInventory().hasItem(index)) {
            index++;
        }
        player.addToInventory(index, item);
    }
}
