package no.ntnu.idata2001.g23.itemhandling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.ntnu.idata2001.g23.exceptions.unchecked.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LootPoolTest {
    private static final Map<String, List<Integer>> lootItems = new HashMap<>();
    private LootPool validLootPool;

    @BeforeAll
    static void beforeAll() {
        lootItems.put("Test item", List.of(4, 12));
        lootItems.put("Another test item", null);
    }

    @BeforeEach
    void beforeEach() {
        validLootPool = new LootPool(Map.of(0, 2.5, 1, 7.5), lootItems);
    }

    @Test
    void testCreationOfLootPoolWithInvalidGenerationQuantities() {
        assertThrows(EmptyArrayException.class, () ->
                new LootPool(null, lootItems));
        Map<Integer, Double> invalidQuantities = new HashMap<>();
        assertThrows(EmptyArrayException.class, () -> new LootPool(invalidQuantities, lootItems));
        invalidQuantities.put(-1, 1d);
        assertThrows(NumberOutOfRangeException.class, () ->
                new LootPool(invalidQuantities, lootItems));
        invalidQuantities.remove(-1);
        invalidQuantities.put(3, 1d);
        assertThrows(NumberOutOfRangeException.class, () ->
                new LootPool(invalidQuantities, lootItems));
        invalidQuantities.remove(3);
        invalidQuantities.put(2, 0d);
        assertThrows(NegativeOrZeroNumberException.class, () ->
                new LootPool(invalidQuantities, lootItems));
    }

    @Test
    void testCreationOfLootWithInvalidGenerationItems() {
        HashMap<Integer, Double> validQuantities = new HashMap<>();
        validQuantities.put(1, 1d);
        assertThrows(EmptyArrayException.class, () ->
                new LootPool(validQuantities, null));
        HashMap<String, List<Integer>> invalidLootItems = new HashMap<>();
        assertThrows(EmptyArrayException.class, () ->
                new LootPool(validQuantities, invalidLootItems));
        invalidLootItems.put(null, List.of(1, 2));
        assertThrows(NullValueException.class, () ->
                new LootPool(validQuantities, invalidLootItems));
        invalidLootItems.remove(null);
        invalidLootItems.put("Test item", List.of(0));
        assertThrows(InvalidArraySizeException.class, () ->
                new LootPool(validQuantities, invalidLootItems));
        invalidLootItems.put("Test item", List.of(-1, 2));
        assertThrows(NegativeOrZeroNumberException.class, () ->
                new LootPool(validQuantities, invalidLootItems));
    }

    @Test
    void testValidCreationOfLootPool() {
        assertDoesNotThrow(() -> validLootPool.generate());
    }
}
