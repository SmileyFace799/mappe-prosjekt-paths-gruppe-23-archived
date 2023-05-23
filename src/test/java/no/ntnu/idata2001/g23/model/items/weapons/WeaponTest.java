package no.ntnu.idata2001.g23.model.items.weapons;

import no.ntnu.idata2001.g23.model.items.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {
    private Weapon validWeapon;

    @BeforeEach
    void before() {
        validWeapon = new Weapon(5, "Test name", "Test description");
    }

    @Test
    void testCreationOfWeaponWithInvalidBaseDamage() {
        assertThrows(IllegalArgumentException.class, () -> new Weapon(
                -5, "Test name", "Test description"));
    }

    @Test
    void testCreationOfWeaponWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> new Weapon(
                5, null, "Test description"));
        assertThrows(IllegalArgumentException.class, () -> new Weapon(
                5, "  ", "Test description"));
    }

    @Test
    void testCreationOfWeaponWithInvalidDescription() {
        assertThrows(IllegalArgumentException.class, () -> new Weapon(
                5, "Test name", null));
        assertThrows(IllegalArgumentException.class, () -> new Weapon(
                5, "Test name", "  "));
    }

    @Test
    void testCreationOfWeaponWithValidParameters() {
        assertEquals(5, validWeapon.getDamage());
        assertEquals("Test name", validWeapon.getName());
        assertEquals("Test description", validWeapon.getDescription());
    }

    @Test
    void testWeaponEquals() {
        assertEquals(validWeapon, validWeapon);
        assertNotEquals(null, validWeapon);
        assertNotEquals(new Object(), validWeapon);

        assertNotEquals(new Weapon(10,
                "Test name", "Test description"), validWeapon);
        assertNotEquals(new Weapon(5,
                "Different name", "Test description"), validWeapon);
        assertNotEquals(new Weapon(5,
                "Test name", "Different description"), validWeapon);

        assertEquals(new Weapon(5,
                "Test name", "Test description"), validWeapon);
    }

}
