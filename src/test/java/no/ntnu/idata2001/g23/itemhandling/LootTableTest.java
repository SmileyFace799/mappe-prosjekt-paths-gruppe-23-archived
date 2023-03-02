package no.ntnu.idata2001.g23.itemhandling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.ntnu.idata2001.g23.exceptions.unchecked.EmptyArrayException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NullValueException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NumberOutOfRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LootTableTest {
    private LootPool validLootPool;
    private LootTable validLootTable;

    @BeforeEach
    void before() {
        validLootPool = new LootPool(
                Map.of(1, 1d),
                Map.of("Test item", List.of(4, 12))
        );
        validLootTable = new LootTable(Map.of(validLootPool, 0.5));
    }

    @Test
    void testCreationOfLootTableWithNoLootPoolMap() {
        assertThrows(EmptyArrayException.class, () -> new LootTable(null));
        Map<LootPool, Double> lootPoolMap = new HashMap<>();
        assertThrows(EmptyArrayException.class, () -> new LootTable(lootPoolMap));
        lootPoolMap.put(null, 0.5);
        assertThrows(NullValueException.class, () -> new LootTable(lootPoolMap));
        lootPoolMap.remove(null);
        lootPoolMap.put(validLootPool, 0d);
        assertThrows(NumberOutOfRangeException.class, () -> new LootTable(lootPoolMap));
        lootPoolMap.put(validLootPool, 1.1);
        assertThrows(NumberOutOfRangeException.class, () -> new LootTable(lootPoolMap));
    }

    @Test
    void testCreationOfLootTableWithValidParameters() {
        assertDoesNotThrow(() -> validLootTable.generate());
    }
}
