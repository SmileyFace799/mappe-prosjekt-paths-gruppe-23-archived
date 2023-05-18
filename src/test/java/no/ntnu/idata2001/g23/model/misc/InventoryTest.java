package no.ntnu.idata2001.g23.model.misc;

import no.ntnu.idata2001.g23.exceptions.unchecked.*;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.MiscItem;
import no.ntnu.idata2001.g23.model.items.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
    private Item inventoryMiscItem;
    private Item inventorySword;
    private Item otherItem;
    private Inventory validInventory;

    @BeforeEach
    void before() {
        inventoryMiscItem = new MiscItem(
                500, "Test item", "Test description");
        inventorySword = new Weapon(5, 0.25, 500,
                "Test sword", "Test description");
        validInventory = new Inventory(3);
        validInventory.addItem(inventoryMiscItem);
        validInventory.addItem(inventorySword);

        otherItem = new MiscItem(500, "Another test item",
                "Another test description");
    }

    @Test
    void testCreationOfInventoryWithInvalidSize() {
        assertThrows(NegativeOrZeroNumberException.class, () -> new Inventory(0));
    }

    @Test
    void testCreationOfInventoryWithValidParameters() {
        assertEquals(inventoryMiscItem, validInventory.getItem(0));
        assertEquals(inventorySword, validInventory.getItem(1));
        assertNull(validInventory.getItem(2));
    }

    @Test
    void testGetItemWithInvalidIndex() {
        assertThrows(NumberOutOfRangeException.class, () -> validInventory.getItem(-1));
        assertThrows(NumberOutOfRangeException.class, () -> validInventory.getItem(4));
    }

    @Test
    void testInventoryHasItem() {
        assertTrue(validInventory.hasItem(inventoryMiscItem));
        assertTrue(validInventory.hasItem(inventorySword));
        assertFalse(validInventory.hasItem(otherItem));
    }

    @Test
    void testAddItemWithInvalidItem() {
        assertThrows(NullValueException.class, () ->
                validInventory.addItem(null));
    }

    @Test
    void testValidAddingOfItem() {
        assertDoesNotThrow(() -> validInventory.addItem(otherItem));
        assertTrue(validInventory.hasItem(otherItem));
    }

    @Test
    void testRemovingOfInvalidItem() {
        assertThrows(ElementNotFoundException.class, () -> validInventory.removeItem(null));
        assertThrows(ElementNotFoundException.class, () -> validInventory.removeItem(otherItem));
    }

    @Test
    void testValidRemovingOfItem() {
        assertDoesNotThrow(() -> validInventory.removeItem(inventoryMiscItem));
        assertFalse(validInventory.hasItem(inventoryMiscItem));
        assertNull(validInventory.getItem(1));
    }

    @Test
    void testInventoryHasSubsetOfItems() throws FullInventoryException {
        Inventory subset1 = new Inventory(2);
        subset1.addItem(otherItem);
        subset1.addItem(inventorySword);
        Inventory subset2 = new Inventory(2);
        subset2.addItem(otherItem);
        subset2.addItem(inventoryMiscItem);
        Inventory subset3 = new Inventory(2);
        subset3.addItem(inventorySword);
        subset3.addItem(inventoryMiscItem);

        assertFalse(validInventory.hasItems(subset1));
        assertFalse(validInventory.hasItems(subset2));
        assertTrue(validInventory.hasItems(subset3));
    }
}
