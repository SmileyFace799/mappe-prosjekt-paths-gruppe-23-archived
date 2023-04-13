package no.ntnu.idata2001.g23.model.items.weapons;

import no.ntnu.idata2001.g23.exceptions.unchecked.BlankStringException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NegativeNumberException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NumberOutOfRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SwordTest {
    private Sword validSword;

    @BeforeEach
    void before() {
        validSword = new Sword(5, 0.25, 500,
                "Test name", "Test description");
    }

    @Test
    void testCreationOfSwordWithInvalidBaseDamage() {
        assertThrows(NegativeNumberException.class, () -> new Sword(
                -5, 0.25, 500,
                "Test name", "Test description"
        ));
    }

    @Test
    void testCreationOfSwordWithInvalidBaseCritChance() {
        assertThrows(NumberOutOfRangeException.class, () -> new Sword(
                5, -0.25, 500,
                "Test name", "Test description"
        ));
        assertThrows(NumberOutOfRangeException.class, () -> new Sword(
                5, 1.25, 500,
                "Test name", "Test description"
        ));
    }

    @Test
    void testCreationOfSwordWithInvalidValue() {
        assertThrows(NegativeNumberException.class, () -> new Sword(
                5, 0.25, -500,
                "Test name", "Test description"));
    }

    @Test
    void testCreationOfSwordWithInvalidName() {
        assertThrows(BlankStringException.class, () -> new Sword(
                5, 0.25, 500,
                null, "Test description"
        ));
        assertThrows(BlankStringException.class, () -> new Sword(
                5, 0.25, 500,
                "  ", "Test description"
        ));
    }

    @Test
    void testCreationOfSwordWithInvalidDescription() {
        assertThrows(BlankStringException.class, () -> new Sword(
                5, 0.25, 500,
                "Test name", null
        ));
        assertThrows(BlankStringException.class, () -> new Sword(
                5, 0.25, 500,
                "Test name", "  "
        ));
    }

    @Test
    void testCreationOfSwordWithValidParameters() {
        assertEquals(5, validSword.getBaseDamage());
        assertEquals(0.25, validSword.getBaseCritChance());
        assertEquals(500, validSword.getValue());
        assertTrue(validSword.isSellable());
        assertEquals("Test name", validSword.getName());
        assertEquals("Test description", validSword.getDescription());
    }

    @Test
    void testCreationOfSwordWithZeroValue() {
        Sword unsellableSword = new Sword(
                5, 0.25, 0,
                "Test name", "Test description");
        assertEquals(0, unsellableSword.getValue());
        assertFalse(unsellableSword.isSellable());
    }

    @Test
    void testSwordEquals() {
        assertEquals(validSword, validSword);
        assertNotEquals(null, validSword);
        assertNotEquals(new Object(), validSword);

        assertNotEquals(new Sword(10, 0.25, 500,
                "Test name", "Test description"), validSword);
        assertNotEquals(new Sword(5, 0.5, 500,
                "Test name", "Test description"), validSword);
        assertNotEquals(new Sword(5, 0.25, 250,
                "Test name", "Test description"), validSword);
        assertNotEquals(new Sword(5, 0.25, 500,
                "Different name", "Test description"), validSword);
        assertNotEquals(new Sword(5, 0.25, 500,
                "Test name", "Different description"), validSword);

        assertEquals(new Sword(5, 0.25, 500,
                "Test name", "Test description"), validSword);
    }

}
