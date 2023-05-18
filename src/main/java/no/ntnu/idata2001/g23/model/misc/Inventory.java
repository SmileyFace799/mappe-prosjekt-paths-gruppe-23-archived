package no.ntnu.idata2001.g23.model.misc;

import java.util.ArrayList;
import java.util.List;
import no.ntnu.idata2001.g23.exceptions.unchecked.ElementNotFoundException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NegativeOrZeroNumberException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NotEmptyException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NullValueException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NumberOutOfRangeException;
import no.ntnu.idata2001.g23.model.items.Item;

/**
 * Any inventory used by an entity.
 */
public class Inventory {
    private final List<Item> contents;
    private int inventorySize = 1;

    public Inventory(int inventorySize) {
        contents = new ArrayList<>();
        setInventorySize(inventorySize);
    }

    public int getSize() {
        return inventorySize;
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
        if (index < 0 || index >= inventorySize) {
            throw new NumberOutOfRangeException(
                    "int \"index\" must be between 0 and the inventory size");
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
    public void addItem(Item item) throws FullInventoryException {
        if (item == null) {
            throw new NullValueException("\"item\" cannot be null");
        }
        if (contents.size() == inventorySize) {
            throw new FullInventoryException("Cannot add item, inventory is full");
        }
        contents.add(item);
    }

    /**
     * Removes an item from the inventory.
     *
     * @param item The item to remove
     * @throws ElementNotFoundException If the item to remove is not present in the inventory
     */
    public void removeItem(Item item) {
        if (item == null || !contents.contains(item)) {
            throw new ElementNotFoundException("\"item\" is not present in the inventory");
        }
        contents.remove(item);
    }

    /**
     * Sets a new inventory size.
     *
     * @param newSize The new size of the inventory. If the size is reduced,
     *                the inventory cannot contain any items in the range that is to be removed.
     */
    public void setInventorySize(int newSize) {
        if (newSize <= 0) {
            throw new NegativeOrZeroNumberException("int \"newSize\" must be greater than 0");
        }
        if (newSize < contents.size()) {
            throw new NotEmptyException("Cannot reduce inventory size: "
                    + "The range that is to be removed is not empty");
        }
        inventorySize = newSize;
    }
}
