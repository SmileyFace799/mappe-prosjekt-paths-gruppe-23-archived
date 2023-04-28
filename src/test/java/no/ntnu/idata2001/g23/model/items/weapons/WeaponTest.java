package no.ntnu.idata2001.g23.model.items.weapons;

import no.ntnu.idata2001.g23.exceptions.unchecked.BlankStringException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NegativeNumberException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NumberOutOfRangeException;
import no.ntnu.idata2001.g23.model.items.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {
    private Weapon validWeapon;

    @BeforeEach
    void before() {
        validWeapon = new Weapon(5, 0.25, 500,
                "Test name", "Test description");
    }

    @Test
    void testCreationOfWeaponWithInvalidBaseDamage() {
        assertThrows(NegativeNumberException.class, () -> new Weapon(
                -5, 0.25, 500,
                "Test name", "Test description"
        ));
    }

    @Test
    void testCreationOfWeaponWithInvalidBaseCritChance() {
        assertThrows(NumberOutOfRangeException.class, () -> new Weapon(
                5, -0.25, 500,
                "Test name", "Test description"
        ));
        assertThrows(NumberOutOfRangeException.class, () -> new Weapon(
                5, 1.25, 500,
                "Test name", "Test description"
        ));
    }

    @Test
    void testCreationOfWeaponWithInvalidValue() {
        assertThrows(NegativeNumberException.class, () -> new Weapon(
                5, 0.25, -500,
                "Test name", "Test description"));
    }

    @Test
    void testCreationOfWeaponWithInvalidName() {
        assertThrows(BlankStringException.class, () -> new Weapon(
                5, 0.25, 500,
                null, "Test description"
        ));
        assertThrows(BlankStringException.class, () -> new Weapon(
                5, 0.25, 500,
                "  ", "Test description"
        ));
    }

    @Test
    void testCreationOfWeaponWithInvalidDescription() {
        assertThrows(BlankStringException.class, () -> new Weapon(
                5, 0.25, 500,
                "Test name", null
        ));
        assertThrows(BlankStringException.class, () -> new Weapon(
                5, 0.25, 500,
                "Test name", "  "
        ));
    }

    @Test
    void testCreationOfWeaponWithValidParameters() {
        assertEquals(5, validWeapon.getBaseDamage());
        assertEquals(0.25, validWeapon.getBaseCritChance());
        assertEquals(500, validWeapon.getValue());
        assertTrue(validWeapon.isSellable());
        assertEquals("Test name", validWeapon.getName());
        assertEquals("Test description", validWeapon.getDescription());
    }

    @Test
    void testCreationOfWeaponWithZeroValue() {
        Weapon unsellableWeapon = new Weapon(
                5, 0.25, 0,
                "Test name", "Test description");
        assertEquals(0, unsellableWeapon.getValue());
        assertFalse(unsellableWeapon.isSellable());
    }

    @Test
    void testWeaponEquals() {
        assertEquals(validWeapon, validWeapon);
        assertNotEquals(null, validWeapon);
        assertNotEquals(new Object(), validWeapon);

        assertNotEquals(new Weapon(10, 0.25, 500,
                "Test name", "Test description"), validWeapon);
        assertNotEquals(new Weapon(5, 0.5, 500,
                "Test name", "Test description"), validWeapon);
        assertNotEquals(new Weapon(5, 0.25, 250,
                "Test name", "Test description"), validWeapon);
        assertNotEquals(new Weapon(5, 0.25, 500,
                "Different name", "Test description"), validWeapon);
        assertNotEquals(new Weapon(5, 0.25, 500,
                "Test name", "Different description"), validWeapon);

        assertEquals(new Weapon(5, 0.25, 500,
                "Test name", "Test description"), validWeapon);
    }

}
