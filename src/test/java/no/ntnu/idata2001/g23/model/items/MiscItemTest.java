package no.ntnu.idata2001.g23.model.items;

import no.ntnu.idata2001.g23.exceptions.unchecked.BlankStringException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NegativeNumberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MiscItemTest {
    private MiscItem validMiscItem;

    @BeforeEach
    void before() {
        validMiscItem = new MiscItem(500, "Test name", "Test description");
    }

    @Test
    void testCreationOfMiscItemWithInvalidValue() {
        assertThrows(NegativeNumberException.class, () -> new MiscItem(-500,
                "Test name", "Test description"));
    }

    @Test
    void testCreationOfMiscItemWithInvalidName() {
        assertThrows(BlankStringException.class, () -> new MiscItem(
                500, null, "Test description"
        ));
        assertThrows(BlankStringException.class, () -> new MiscItem(
                500, "  ", "Test description"
        ));
    }

    @Test
    void testCreationOfMiscItemWithInvalidDescription() {
        assertThrows(BlankStringException.class, () -> new MiscItem(
                500, "Test name", null
        ));
        assertThrows(BlankStringException.class, () -> new MiscItem(
                500, "Test name", "  "
        ));
    }

    @Test
    void testCreationOfMiscItemWithZeroValue() {
        MiscItem unsellableMiscItem = new MiscItem(0, "Test name", "Test description");
        assertEquals(0, unsellableMiscItem.getValue());
        assertFalse(unsellableMiscItem.isSellable());
    }

    @Test
    void testCreationOfMiscItemWithValidParameters() {
        assertEquals(500, validMiscItem.getValue());
        assertTrue(validMiscItem.isSellable());
        assertEquals("Test name", validMiscItem.getName());
        assertEquals("Test description", validMiscItem.getDescription());
    }

    @Test
    void testMiscItemEquals() {
        assertEquals(validMiscItem, validMiscItem);
        assertNotEquals(null, validMiscItem);
        assertNotEquals(new Object(), validMiscItem);

        assertNotEquals(new MiscItem(250,
                "Test name", "Test description"), validMiscItem);
        assertNotEquals(new MiscItem(500,
                "Different name", "Test description"), validMiscItem);
        assertNotEquals(new MiscItem(500,
                "Test name", "Different description"), validMiscItem);

        assertEquals(new MiscItem(500,
                "Test name", "Test description"), validMiscItem);
    }

}
