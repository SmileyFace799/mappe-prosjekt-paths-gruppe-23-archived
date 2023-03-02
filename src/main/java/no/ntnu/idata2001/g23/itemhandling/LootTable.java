package no.ntnu.idata2001.g23.itemhandling;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import no.ntnu.idata2001.g23.exceptions.unchecked.EmptyArrayException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NullValueException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NumberOutOfRangeException;
import no.ntnu.idata2001.g23.items.Item;

/**
 * A loot table, that holds multiple loot pools which it can generate loot from.
 */
public class LootTable {
    private final Map<LootPool, Double> lootPools;
    private final Random random = new Random();

    /**
     * Makes a loot table.
     *
     * @param lootPools A map with loot pools as keys, and the chance of that loot pool generating
     *                  items when generating the loot table.
     */
    public LootTable(Map<LootPool, Double> lootPools) {

        if (lootPools == null || lootPools.isEmpty()) {
            throw new EmptyArrayException("Map of loot pools cannot be empty or null");
        }
        if (lootPools.keySet().stream().anyMatch(Objects::isNull)) {
            throw new NullValueException("Loot pool cannot be null");
        }
        if (lootPools.values().stream().anyMatch(generateChance ->
                generateChance <= 0 || generateChance > 1)) {
            throw new NumberOutOfRangeException("Chance to generate a loot pool must be greater"
                    + "than 0, and 1 at most");
        }
        this.lootPools = lootPools;
    }

    /**
     * Generates items from the loot table.
     *
     * @return The items generated from the table.
     */
    public List<Item> generate() {
        List<Item> generatedItems = new ArrayList<>();
        for (Map.Entry<LootPool, Double> lootEntry : lootPools.entrySet()) {
            if (random.nextDouble() < lootEntry.getValue()) {
                generatedItems.addAll(lootEntry.getKey().generate());
            }
        }
        return generatedItems;
    }
}
