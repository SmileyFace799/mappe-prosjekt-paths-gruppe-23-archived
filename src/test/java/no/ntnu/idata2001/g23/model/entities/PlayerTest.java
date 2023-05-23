package no.ntnu.idata2001.g23.model.entities;

import java.util.ArrayList;
import java.util.List;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Item item;
    private Weapon weapon;
    private Player.PlayerBuilder validPlayerBuilder;

    @BeforeEach
    void before() {
        item = new Item("Test item", "Test description");
        weapon = new Weapon(5, "Test weapon", "Test description");
        validPlayerBuilder = new Player.PlayerBuilder("Test player", 10)
                .setGold(20)
                .setScore(30)
                .setStartingItems(List.of(item))
                .setEquippedWeapon(weapon);
    }

    @Test
    void testCreationOfPlayerWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () ->
                new Player.PlayerBuilder(null, 20));
        assertThrows(IllegalArgumentException.class, () ->
                new Player.PlayerBuilder("  ", 20));
    }

    @Test
    void testCreationOfPlayerWithInvalidHealth() {
        assertThrows(IllegalArgumentException.class, () ->
                new Player.PlayerBuilder("Test player", -1));
    }

    @Test
    void testCreationOfPlayerWithInvalidGold() {
        assertThrows(IllegalArgumentException.class, () ->
                validPlayerBuilder.setGold(-1));
    }

    @Test
    void testCreationOfPlayerWithInvalidScore() {
        assertThrows(IllegalArgumentException.class, () ->
                validPlayerBuilder.setScore(-1));
    }

    @Test
    void testCreationOfPlayerWithInvalidStartingItems() {
        List<Item> nullItemList = new ArrayList<>();
        nullItemList.add(null);
        assertThrows(IllegalArgumentException.class, () ->
                validPlayerBuilder.setStartingItems(nullItemList));
    }

    @Test
    void testCreationOfPlayerWithValidParameters() {
        Player validPlayer = validPlayerBuilder.build();
        assertEquals("Test player", validPlayer.getName());
        assertEquals(20, validPlayer.getGold());
        assertEquals(10, validPlayer.getHealth());
        assertEquals(10, validPlayer.getMaxHealth());
        assertEquals(30, validPlayer.getScore());
        assertEquals(weapon, validPlayer.getEquippedWeapon());
        assertTrue(validPlayer.getInventory().hasItem(item));
        assertTrue(validPlayer.getInventory().hasItem(weapon));
    }
}
