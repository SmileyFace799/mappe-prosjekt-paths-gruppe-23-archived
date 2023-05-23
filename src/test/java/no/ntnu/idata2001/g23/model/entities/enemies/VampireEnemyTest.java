package no.ntnu.idata2001.g23.model.entities.enemies;

import java.util.ArrayList;
import java.util.List;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VampireEnemyTest {
    private Item item;
    private Weapon weapon;
    private VampireEnemy.VampireEnemyBuilder validVampireEnemyBuilder;

    @BeforeEach
    void before() {
        item = new Item("Test item", "Test description");
        weapon = new Weapon(5, "Test weapon", "Test description");
        validVampireEnemyBuilder =
                new VampireEnemy.VampireEnemyBuilder("Test VampireEnemy", 10)
                        .setGold(20)
                        .setScore(30)
                        .setStartingItems(List.of(item))
                        .setEquippedWeapon(weapon)
                        .setItemDropChance(1)
                        .canDropWeapon(false);
    }

    @Test
    void testCreationOfVampireEnemyWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () ->
                new VampireEnemy.VampireEnemyBuilder(null, 20));
        assertThrows(IllegalArgumentException.class, () ->
                new VampireEnemy.VampireEnemyBuilder("  ", 20));
    }

    @Test
    void testCreationOfVampireEnemyWithInvalidHealth() {
        assertThrows(IllegalArgumentException.class, () ->
                new VampireEnemy.VampireEnemyBuilder("Test VampireEnemy", -1));
    }

    @Test
    void testCreationOfVampireEnemyWithInvalidGold() {
        assertThrows(IllegalArgumentException.class, () ->
                validVampireEnemyBuilder.setGold(-1));
    }

    @Test
    void testCreationOfVampireEnemyWithInvalidScore() {
        assertThrows(IllegalArgumentException.class, () ->
                validVampireEnemyBuilder.setScore(-1));
    }

    @Test
    void testCreationOfVampireEnemyWithInvalidStartingItems() {
        List<Item> nullItemList = new ArrayList<>();
        nullItemList.add(null);
        assertThrows(IllegalArgumentException.class, () ->
                validVampireEnemyBuilder.setStartingItems(nullItemList));
    }

    @Test
    void testCreationOfVampireEnemyWithInvalidItemDropChance() {
        assertThrows(IllegalArgumentException.class, () ->
                validVampireEnemyBuilder.setItemDropChance(-0.1));
        assertThrows(IllegalArgumentException.class, () ->
                validVampireEnemyBuilder.setItemDropChance(1.1));
    }

    @Test
    void testCreationOfVampireEnemyWithValidParameters() {
        VampireEnemy validVampireEnemy = validVampireEnemyBuilder.build();
        assertEquals("Test VampireEnemy", validVampireEnemy.getName());
        assertEquals(20, validVampireEnemy.getGold());
        assertEquals(10, validVampireEnemy.getHealth());
        assertEquals(10, validVampireEnemy.getMaxHealth());
        assertEquals(30, validVampireEnemy.getScore());
        assertEquals(weapon, validVampireEnemy.getEquippedWeapon());
        assertTrue(validVampireEnemy.getInventory().hasItem(item));
        assertTrue(validVampireEnemy.getInventory().hasItem(weapon));
        Player player = new Player.PlayerBuilder("Test player", 20).build();
        validVampireEnemy.dropLoot().forEach(action -> action.execute(player));
        assertTrue(player.getInventory().hasItem(item));
        assertFalse(player.getInventory().hasItem(weapon));
    }
}
