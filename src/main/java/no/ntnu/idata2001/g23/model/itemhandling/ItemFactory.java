package no.ntnu.idata2001.g23.model.itemhandling;

import no.ntnu.idata2001.g23.exceptions.unchecked.ElementNotFoundException;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.MiscItem;
import no.ntnu.idata2001.g23.model.items.Weapon;

/**
 * A collection of all the items used in the game.
 */
public class ItemFactory {
    private ItemFactory() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Makes an item based on the item's name.
     *
     * @param name The name of the item to make.
     * @return The newly made item, of specified quantity.
     */
    public static Item makeItem(String name) {
        Item item;
        switch (name) {
            case "Test item" -> item = new MiscItem(500, name, "Test description");
            case "Another test item" -> item = new MiscItem(500, name,
                    "Another test description");
            case "Test sword" -> item = new Weapon(5, 0.25, 500,
                    name, "Test description");
            case "Big Chungus Blade" -> item = new Weapon(69, 0.25, 500,
                    name, "Blade of the Chungus, UwU");
            case "Large gold nugget" -> item = new MiscItem(5000,
                    name, "A large chunk of solid gold. Sells for good money");
            default -> throw new ElementNotFoundException("No sword with name \"" + name + "\"");
        }
        return item;
    }
}
