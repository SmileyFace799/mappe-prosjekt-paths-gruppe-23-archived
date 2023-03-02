package no.ntnu.idata2001.g23.items;

import no.ntnu.idata2001.g23.exceptions.unchecked.BlankStringException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NegativeNumberException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NumberOutOfRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MiscItemTest {
    private MiscItem validMiscItem;

    @BeforeEach
    void before() {
        validMiscItem = new MiscItem(500, "Test name", "Test description", 8);
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
    void testCreationOfMistItemWithInvalidStackSize() {
        assertThrows(NumberOutOfRangeException.class, () -> new MiscItem(
                500, "Test name", "Test description", 0
        ));
        assertThrows(NumberOutOfRangeException.class, () -> new MiscItem(
                500, "Test name", "Test description", 17
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
        assertEquals(8, validMiscItem.getCurrentStackSize());
        assertEquals(4000, validMiscItem.getStackValue());
    }

    @Test
    void testStackSizeIncreaseWithInvalidAmount() {
        assertThrows(NumberOutOfRangeException.class, () ->
                validMiscItem.increaseStack(0));
        assertThrows(NumberOutOfRangeException.class, () ->
                validMiscItem.increaseStack(17));
    }


    @Test
    void testStackSizeIncreaseWithinMaxSize() {
        int extras = assertDoesNotThrow(() -> validMiscItem.increaseStack(4));
        assertEquals(12, validMiscItem.getCurrentStackSize());
        assertEquals(0, extras);
    }

    @Test
    void testStackSizeIncreaseAboveMaxSize() {
        int extras = assertDoesNotThrow(() -> validMiscItem.increaseStack(12));
        assertEquals(16, validMiscItem.getCurrentStackSize());
        assertEquals(4, extras);
    }

    @Test
    void testStackSizeDecreaseWithInvalidAmount() {
        assertThrows(NumberOutOfRangeException.class, () ->
                validMiscItem.decreaseStack(0));
        assertThrows(NumberOutOfRangeException.class, () ->
                validMiscItem.decreaseStack(8));
    }

    @Test
    void testStackSizeDecreaseWithValidAmount() {
        assertDoesNotThrow(() -> validMiscItem.decreaseStack(4));
        assertEquals(4, validMiscItem.getCurrentStackSize());
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
        assertNotEquals(new MiscItem(500,
                "Test name", "Test description", 4), validMiscItem);

        assertEquals(new MiscItem(500,
                "Test name", "Test description", 8), validMiscItem);
    }

}
