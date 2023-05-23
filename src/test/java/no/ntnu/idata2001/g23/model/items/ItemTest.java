package no.ntnu.idata2001.g23.model.items;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    private Item validItem;

    @BeforeEach
    void before() {
        validItem = new Item("Test name", "Test description");
    }

    @Test
    void testCreationOfItemWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> new Item(
                null, "Test description"
        ));
        assertThrows(IllegalArgumentException.class, () -> new Item(
                "  ", "Test description"
        ));
    }

    @Test
    void testCreationOfItemWithInvalidDescription() {
        assertThrows(IllegalArgumentException.class, () -> new Item(
                "Test name", null
        ));
        assertThrows(IllegalArgumentException.class, () -> new Item(
                "Test name", "  "
        ));
    }

    @Test
    void testCreationOfItemWithValidParameters() {
        assertEquals("Test name", validItem.getName());
        assertEquals("Test description", validItem.getDescription());
    }

    @Test
    void testItemEquals() {
        assertEquals(validItem, validItem);
        assertNotEquals(null, validItem);
        assertNotEquals(new Object(), validItem);

        assertNotEquals(new Item("Different name", "Test description"), validItem);
        assertNotEquals(new Item("Test name", "Different description"), validItem);

        assertEquals(new Item("Test name", "Test description"), validItem);
    }

}
