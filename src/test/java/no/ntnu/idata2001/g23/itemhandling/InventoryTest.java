package no.ntnu.idata2001.g23.itemhandling;

import no.ntnu.idata2001.g23.exceptions.unchecked.*;
import no.ntnu.idata2001.g23.items.Item;
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
        inventoryMiscItem = ItemFactory.makeItem("Test item");
        inventorySword = ItemFactory.makeItem("Test sword");
        validInventory = new Inventory(3);
        validInventory.addItem(inventoryMiscItem);
        validInventory.addItem(inventorySword);

        otherItem = ItemFactory.makeItem("Another test item");
    }

    @Test
    void testCreationOfInventoryWithInvalidSize() {
        assertThrows(NegativeOrZeroNumberException.class, () -> new Inventory(0));
    }

    @Test
    void testCreationOfInventoryWithValidParameters() {
        assertEquals(3, validInventory.getSize());
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
    void testAddItemWithFullInventory() {
        validInventory.addItem(otherItem);
        assertThrows(FullInventoryException.class, () -> validInventory.addItem(otherItem));
    }

    @Test
    void testValidAddingOfItem() {
        assertDoesNotThrow(() -> validInventory.addItem(otherItem));
        assertTrue(validInventory.hasItem(otherItem));
    }

    @Test
    void testRemovingOfItemWithInvalidIndex() {
        assertThrows(NumberOutOfRangeException.class, () -> validInventory.removeItem(-1));
        assertThrows(NumberOutOfRangeException.class, () -> validInventory.removeItem(4));
    }

    @Test
    void testValidRemovingOfItem() {
        assertDoesNotThrow(() -> validInventory.removeItem(0));
        assertFalse(validInventory.hasItem(inventoryMiscItem));
        assertNull(validInventory.getItem(1));
    }

    @Test
    void testInventoryHasSubsetOfItems() {
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

    @Test
    void testSetInventorySizeToInvalidValue() {
        assertThrows(NegativeOrZeroNumberException.class, () -> validInventory.setInventorySize(-1));
    }

    @Test
    void testReductionOfInventorySizeToLessThanTheAmountOfItems() {
        assertThrows(NotEmptyException.class, () -> validInventory.setInventorySize(1));
    }

    @Test
    void testValidSettingOfInventorySize() {
        assertDoesNotThrow(() -> validInventory.setInventorySize(2));
        assertDoesNotThrow(() -> validInventory.setInventorySize(50));
    }
}
