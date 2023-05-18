package no.ntnu.idata2001.g23.model.misc;

import java.util.ArrayList;
import java.util.List;
import no.ntnu.idata2001.g23.model.items.Item;

/**
 * Any inventory used by an entity.
 */
public class Inventory {
    private final List<Item> contents;

    public Inventory() {
        contents = new ArrayList<>();
    }

    public List<Item> getContents() {
        return contents;
    }

    /**
     * Gets the item at a specified index in the inventory.
     *
     * @param index The index to get the item at.
     * @return The item at the specified index. If there is no item at the specified index,
     *         this will return {@code null}.
     */
    public Item getItem(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("int \"index\" must be 0 or greater");
        }
        Item foundItem = null;
        if (index < contents.size()) {
            foundItem = contents.get(index);
        }
        return foundItem;
    }

    public boolean hasItem(Item item) {
        return contents.contains(item);
    }

    /**
     * Checks if the inventory contains a smaller subset of items.
     *
     * @param items The subset of items to check for.
     * @return if the provided subset of items all exist within the inventory.
     */
    public boolean hasItems(Inventory items) {
        return items.getContents().stream().allMatch(targetMatch ->
                contents.stream().anyMatch(inventoryItem ->
                        inventoryItem.equals(targetMatch)));
    }

    /**
     * Adds an item to the inventory.
     *
     * @param item The item to add to the inventory.
     */
    public void addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("\"item\" cannot be null");
        }
        contents.add(item);
    }

    /**
     * Removes an item from the inventory.
     *
     * @param item The item to remove
     * @throws IllegalArgumentException If the item to remove is not present in the inventory
     */
    public void removeItem(Item item) {
        if (item == null || !contents.contains(item)) {
            throw new IllegalArgumentException("\"item\" is not present in the inventory");
        }
        contents.remove(item);
    }
}
