package no.ntnu.idata2001.g23.model.itemhandling;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import no.ntnu.idata2001.g23.exceptions.unchecked.BlankStringException;
import no.ntnu.idata2001.g23.exceptions.unchecked.ElementNotFoundException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NullValueException;
import no.ntnu.idata2001.g23.model.items.Item;

/**
 * Makes & provides items used in a game.
 */
public class ItemProvider {
    private final Map<String, Supplier<Item>> itemSupplierMap;

    /**
     * Makes an empty item provider.
     */
    public ItemProvider() {
        this.itemSupplierMap = new HashMap<>();
    }

    /**
     * Adds an item that the item provider can provide.
     *
     * @param identifier The unique identifier for the item to be provided
     * @param itemSupplier A supplier that makes the item to be provided
     * @throws BlankStringException If identifier is {@code null} or blank
     * @throws NullValueException If itemSupplier is {@code null}
     */
    public void addProvidableItem(String identifier, Supplier<Item> itemSupplier) {
        if (identifier == null || identifier.isBlank()) {
            throw new BlankStringException("String \"name\" cannot be blank");
        }
        if (itemSupplier == null) {
            throw new NullValueException("\"itemSupplier\" cannot be null");
        }
        itemSupplierMap.put(identifier, itemSupplier);
    }

    /**
     * Provide an item given it's identifier.
     *
     * @param identifier The unique identifier for the item to provide
     * @return The provided item
     * @throws BlankStringException If identifier is {@code null} or blank
     * @throws ElementNotFoundException If the identifier is not associated with any item
     */
    public Item provideItem(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new BlankStringException("String \"identifier\" cannot be blank");
        }
        if (!itemSupplierMap.containsKey(identifier)) {
            throw new ElementNotFoundException("No item associated with the specified identifier");
        }
        return itemSupplierMap.get(identifier).get();
    }
}
