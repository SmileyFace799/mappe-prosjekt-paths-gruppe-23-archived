package no.ntnu.idata2001.g23.item_handling;

import no.ntnu.idata2001.g23.exceptions.EmptyArrayException;
import no.ntnu.idata2001.g23.exceptions.NullValueException;
import no.ntnu.idata2001.g23.items.Item;
import no.ntnu.idata2001.g23.items.MiscItem;
import no.ntnu.idata2001.g23.items.weapons.Sword;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class AllItems {
    private final List<Item> items;
    private final Random random;

    private AllItems(List<Item> items) {
        if (items == null || items.isEmpty()) {
            throw new EmptyArrayException("List<item> \"items\" cannot be null or empty");
        }
        this.items = items;
        random = new Random();
    }

    /**
     * Makes an {@code ItemList} with all the actual items used in the game.
     *
     * @return A complete list of every item in the game.
     */
    public static AllItems getGameItems() {
        return new AllItems(List.of(
                new Sword(69, 0.25, 500,
                        "Big Chungus Blade", "Blade of the Chungus, UwU"),
                new MiscItem(5000, "Large gold nugget",
                        "A large chunk of solid gold. Sells for good money")
        ));
    }

    /**
     * Makes an itemList with a set of test items.
     *
     * @return A list of some test items.
     */
    public static AllItems getTestItems() {
        return new AllItems(List.of(
                new MiscItem(500, "Test name", "Test description"),
                new MiscItem(250, "Another name", "Another description"),
                new Sword(5, 0.25, 500,
                        "Sword name", "Sword description")
        ));
    }

    public void setRandomSeed(long seed) {
        random.setSeed(seed);
    }

    /**
     * Returns a copy of a random item from the list, based on the filter condition passed.
     * The copy is created so that {@code original != copy} is true,
     * while {@code original.equals(copy)} is also true.
     * Passing a seed to the constructor when initiating an ItemList will make the randomness consistent.
     *
     * @param filter A lambda expression that takes an Item, and returns a boolean,
     *               representing if the item should be in the draw pool of filtered items.
     * @return A random item that fits the filtered criteria.
     */
    public Item getItem(Predicate<Item> filter) {
        List<Item> filteredItems = items.stream().filter(filter).toList();
        if (filteredItems.isEmpty()) {
            throw new NullValueException("No available items matching the item filter");
        }
        Item item = filteredItems.get(random.nextInt(filteredItems.size()));
        Class<? extends Item> itemClass = item.getClass();
        Item copy;
        try {
            copy = itemClass.getConstructor(itemClass).newInstance(item);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            //TODO: Ask Arne what to do here bruh
            throw new RuntimeException(e);
        }
        return copy;
    }

    public <T extends Item> T getItem(Class<T> typeFilter, Predicate<T> filter) {
        Predicate<Item> itemFilter = item -> typeFilter.isInstance(item) && filter.test(typeFilter.cast(item));
        return typeFilter.cast(getItem(itemFilter));
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Items:");
        for (Item item : items) {
            str.append("\n\n").append(item);
        }
        return str.toString();
    }
}
