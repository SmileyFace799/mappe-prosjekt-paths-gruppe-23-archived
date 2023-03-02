package no.ntnu.idata2001.g23.itemhandling;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import no.ntnu.idata2001.g23.exceptions.unchecked.EmptyArrayException;
import no.ntnu.idata2001.g23.exceptions.unchecked.InvalidArraySizeException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NegativeOrZeroNumberException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NullValueException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NumberOutOfRangeException;
import no.ntnu.idata2001.g23.items.Item;

/**
 * A pool of items that can be generated.
 */
public class LootPool {
    private final Map<Integer, Double> generationQuantityWeightings;
    private final Map<String, List<Integer>> generationItems;
    private final Random random = new Random();

    /**
     * Creates a loot pool that can generate random items.
     *
     * @param generationQuantities A map of two numbers, representing how many or few items
     *                             the loot pool can generate. The key is how many items
     *                             to generate, while the value is a relative weighting,
     *                             representing the chance for the key to be picked as the
     *                             number of items to generate.
     * @param generationItems      A map of possible items to generate, where the key is the
     *                             name of the item, while the value is a list of exactly 2
     *                             numbers, representing the minimum & maximum amount of the
     *                             item to generate. If the list value is {@code null}, the
     *                             loot pool will always generate exactly 1 item if the item
     *                             is picked.
     */
    public LootPool(
            Map<Integer, Double> generationQuantities,
            Map<String, List<Integer>> generationItems
    ) {
        //Makes sure none of the parameters are null
        if (generationQuantities == null || generationQuantities.isEmpty()) {
            throw new EmptyArrayException("The generation quantities can't be empty");
        }
        if (generationItems == null || generationItems.isEmpty()) {
            throw new EmptyArrayException("The generation items can't be empty");
        }
        //Validates generationQuantityWeighting
        if (generationQuantities.keySet().stream().anyMatch(generationQuantity ->
                generationQuantity < 0 || generationQuantity > generationItems.size())) {
            throw new NumberOutOfRangeException("All generation quantities must be between "
                    + "0 and the number of items able to be generated"
            );
        }
        if (generationQuantities.values().stream().anyMatch(quantityWeighting ->
                quantityWeighting <= 0)) {
            throw new NegativeOrZeroNumberException(
                    "The quantity weighting for a quantity must be positive");
        }
        //Validates generationItems
        if (generationItems.keySet().stream().anyMatch(Objects::isNull)) {
            throw new NullValueException("The name of an item in the loot pool cannot be null");
        }
        if (generationItems.values().stream().anyMatch(itemQuantityRange ->
                itemQuantityRange != null && itemQuantityRange.size() != 2)) {
            throw new InvalidArraySizeException(
                    "The item quantity range for an item should be a range of exactly 2 integers");
        }
        if (generationItems.values().stream().anyMatch(itemQuantityRange ->
                itemQuantityRange != null && itemQuantityRange.stream().anyMatch(i -> i <= 0))) {
            throw new NegativeOrZeroNumberException(
                    "Minimum quantity to generate of an item must be above 0");
        }
        this.generationQuantityWeightings = generationQuantities;
        this.generationItems = generationItems;
    }

    /**
     * Randomly generates items from the loot pool.
     *
     * @return A list of generated items.
     */
    public List<Item> generate() {
        double generationQuantityDeterminant = random.nextDouble(
                generationQuantityWeightings.values().stream().reduce(0d, Double::sum));
        Iterator<Integer> generationQuantityIterator =
                generationQuantityWeightings.keySet().iterator();
        int generationQuantity = 0;
        while (generationQuantityDeterminant > 0) {
            generationQuantity = generationQuantityIterator.next();
            generationQuantityDeterminant -= generationQuantityWeightings.get(generationQuantity);
        }
        List<String> itemNames = new ArrayList<>(generationItems.keySet().stream().toList());
        List<Item> generatedItems = new ArrayList<>();
        for (int i = generationQuantity - 1; i >= 0; i--) {
            String itemToGenerate = itemNames.remove(random.nextInt(itemNames.size()));
            List<Integer> generationQuantityRange = generationItems.get(itemToGenerate);
            if (generationQuantityRange == null) {
                generatedItems.add(ItemFactory.makeItem(itemToGenerate));
            } else {
                generatedItems.add(ItemFactory.makeItem(itemToGenerate, random.nextInt(
                        generationQuantityRange.get(0), generationQuantityRange.get(1) + 1)));
            }
        }
        return generatedItems;
    }
}
