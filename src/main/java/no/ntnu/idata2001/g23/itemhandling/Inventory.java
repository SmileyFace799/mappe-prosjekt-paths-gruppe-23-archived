package no.ntnu.idata2001.g23.itemhandling;

import java.util.HashMap;
import java.util.Map;
import no.ntnu.idata2001.g23.exceptions.NegativeOrZeroNumberException;
import no.ntnu.idata2001.g23.exceptions.NotEmptyException;
import no.ntnu.idata2001.g23.exceptions.NumberOutOfRangeException;
import no.ntnu.idata2001.g23.items.Item;

/**
 * Any inventory used by an entity.
 *
 * @param <I> The type of items to be stored in the inventory.
 *            Can be any class extending {@code Item}.
 */
public class Inventory<I extends Item> {
    private final Map<Integer, I> contents;
    private int inventorySize = 1;

    public Inventory(int inventorySize) {
        contents = new HashMap<>();
        setInventorySize(inventorySize);
    }

    public int getSize() {
        return inventorySize;
    }

    /**
     * Gets the item at a specified index in the inventory.
     *
     * @param index The index to get the item at.
     * @return The item at the specified index. If there is no item at the specified index,
     *         this will return {@code null}.
     */
    public I getItem(int index) {
        if (index < 0 || index >= inventorySize) {
            throw new NumberOutOfRangeException(
                    "int \"index\" must be between 0 and the inventory size");
        }
        return contents.get(index);
    }

    public boolean hasItem(int index) {
        return contents.containsKey(index);
    }

    /**
     * Changes an item in the inventory at a specified index.
     *
     * @param index The index to put the specified item at.
     * @param item  The item to put into the inventory. If this is {@code null},
     *              it will remove the item at the specified index.
     */
    public I changeItem(int index, I item) {
        if (index < 0 || index >= inventorySize) {
            throw new NumberOutOfRangeException(
                    "int \"index\" must be between 0 and the inventory size");
        }
        I returnItem;
        if (item == null) {
            returnItem = contents.remove(index);
        } else {
            returnItem = contents.put(index, item);
        }
        return returnItem;
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
        if (newSize < inventorySize) {
            for (int i = newSize; i < inventorySize; i++) {
                if (hasItem(i)) {
                    throw new NotEmptyException("Cannot reduce inventory size: "
                            + "The range that is to be removed is not empty");
                }
            }
        }
        inventorySize = newSize;
    }
}
