package no.ntnu.idata2001.g23.itemhandling;

import no.ntnu.idata2001.g23.exceptions.unchecked.NegativeOrZeroNumberException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NotEmptyException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NumberOutOfRangeException;
import no.ntnu.idata2001.g23.items.Item;
import no.ntnu.idata2001.g23.items.MiscItem;
import no.ntnu.idata2001.g23.items.weapons.Sword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
    private MiscItem inventoryMiscItem;
    private Sword inventorySword;
    private Inventory<Item> validInventory;

    @BeforeEach
    void before() {
        inventoryMiscItem = new MiscItem(500, "Test name", "Test description", 8);
        inventorySword = new Sword(5, 0.25, 500,
                "Test name", "Test description");
        validInventory = new Inventory<>(4);
        validInventory.changeItem(0, inventoryMiscItem);
        validInventory.changeItem(2, inventorySword);
    }

    @Test
    void testCreationOfInventoryWithInvalidSize() {
        assertThrows(NegativeOrZeroNumberException.class, () -> new Inventory<>(0));
    }

    @Test
    void testCreationOfInventoryWithValidParameters() {
        assertEquals(4, validInventory.getSize());
        assertEquals(inventoryMiscItem, validInventory.getItem(0));
        assertNull(validInventory.getItem(1));
        assertEquals(inventorySword, validInventory.getItem(2));
    }

    @Test
    void testGetItemWithInvalidIndex() {
        assertThrows(NumberOutOfRangeException.class, () -> validInventory.getItem(-1));
        assertThrows(NumberOutOfRangeException.class, () -> validInventory.getItem(4));
    }

    @Test
    void testChangeItemWithInvalidIndex() {
        assertThrows(NumberOutOfRangeException.class, () ->
                validInventory.changeItem(-1, inventoryMiscItem));
        assertThrows(NumberOutOfRangeException.class, () ->
                validInventory.changeItem(4, inventoryMiscItem));
    }

    @Test
    void testValidChangingOfItem() {
        assertEquals(inventoryMiscItem, assertDoesNotThrow(
                () -> validInventory.changeItem(0, null)
        ));
        assertNull(validInventory.getItem(0));

        assertNull(assertDoesNotThrow(
                () -> validInventory.changeItem(1, inventoryMiscItem)
        ));
        assertEquals(inventoryMiscItem, validInventory.getItem(1));
    }

    @Test
    void testSetInventorySizeToInvalidValue() {
        assertThrows(NegativeOrZeroNumberException.class, () -> validInventory.setInventorySize(-1));
    }

    @Test
    void testReductionOfInventoryWithNotEmptyReduceRange() {
        assertThrows(NotEmptyException.class, () -> validInventory.setInventorySize(2));
    }

    @Test
    void testValidSettingOfInventorySize() {
        assertDoesNotThrow(() -> validInventory.setInventorySize(3));
        assertDoesNotThrow(() -> validInventory.setInventorySize(50));
    }
}
