package no.ntnu.idata2001.g23.model.items;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    private Item validItem;

    @BeforeEach
    void before() {
        validItem = new Item(500, "Test name", "Test description");
    }

    @Test
    void testCreationOfItemWithInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> new Item(-500,
                "Test name", "Test description"));
    }

    @Test
    void testCreationOfItemWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> new Item(
                500, null, "Test description"
        ));
        assertThrows(IllegalArgumentException.class, () -> new Item(
                500, "  ", "Test description"
        ));
    }

    @Test
    void testCreationOfItemWithInvalidDescription() {
        assertThrows(IllegalArgumentException.class, () -> new Item(
                500, "Test name", null
        ));
        assertThrows(IllegalArgumentException.class, () -> new Item(
                500, "Test name", "  "
        ));
    }

    @Test
    void testCreationOfItemWithZeroValue() {
        Item unsellableItem = new Item(0, "Test name", "Test description");
        assertEquals(0, unsellableItem.getValue());
        assertFalse(unsellableItem.isSellable());
    }

    @Test
    void testCreationOfItemWithValidParameters() {
        assertEquals(500, validItem.getValue());
        assertTrue(validItem.isSellable());
        assertEquals("Test name", validItem.getName());
        assertEquals("Test description", validItem.getDescription());
    }

    @Test
    void testItemEquals() {
        assertEquals(validItem, validItem);
        assertNotEquals(null, validItem);
        assertNotEquals(new Object(), validItem);

        assertNotEquals(new Item(250,
                "Test name", "Test description"), validItem);
        assertNotEquals(new Item(500,
                "Different name", "Test description"), validItem);
        assertNotEquals(new Item(500,
                "Test name", "Different description"), validItem);

        assertEquals(new Item(500,
                "Test name", "Test description"), validItem);
    }

}
