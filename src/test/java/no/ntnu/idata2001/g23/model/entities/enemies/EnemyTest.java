package no.ntnu.idata2001.g23.model.entities.enemies;

import java.util.ArrayList;
import java.util.List;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnemyTest {
    private Item item;
    private Weapon weapon;
    private Enemy.EnemyBuilder validEnemyBuilder;

    @BeforeEach
    void before() {
        item = new Item("Test item", "Test description");
        weapon = new Weapon(5, "Test weapon", "Test description");
        validEnemyBuilder = new Enemy.EnemyBuilder("Test Enemy", 10)
                .setGold(20)
                .setScore(30)
                .setStartingItems(List.of(item))
                .setEquippedWeapon(weapon)
                .setItemDropChance(1)
                .canDropWeapon(false);
    }

    @Test
    void testCreationOfEnemyWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () ->
                new Enemy.EnemyBuilder(null, 20));
        assertThrows(IllegalArgumentException.class, () ->
                new Enemy.EnemyBuilder("  ", 20));
    }

    @Test
    void testCreationOfEnemyWithInvalidHealth() {
        assertThrows(IllegalArgumentException.class, () ->
                new Enemy.EnemyBuilder("Test Enemy", -1));
    }

    @Test
    void testCreationOfEnemyWithInvalidGold() {
        assertThrows(IllegalArgumentException.class, () ->
                validEnemyBuilder.setGold(-1));
    }

    @Test
    void testCreationOfEnemyWithInvalidScore() {
        assertThrows(IllegalArgumentException.class, () ->
                validEnemyBuilder.setScore(-1));
    }

    @Test
    void testCreationOfEnemyWithInvalidStartingItems() {
        List<Item> nullItemList = new ArrayList<>();
        nullItemList.add(null);
        assertThrows(IllegalArgumentException.class, () ->
                validEnemyBuilder.setStartingItems(nullItemList));
    }

    @Test
    void testCreationOfEnemyWithInvalidItemDropChance() {
        assertThrows(IllegalArgumentException.class, () ->
                validEnemyBuilder.setItemDropChance(-0.1));
        assertThrows(IllegalArgumentException.class, () ->
                validEnemyBuilder.setItemDropChance(1.1));
    }

    @Test
    void testCreationOfEnemyWithValidParameters() {
        Enemy validEnemy = validEnemyBuilder.build();
        assertEquals("Test Enemy", validEnemy.getName());
        assertEquals(20, validEnemy.getGold());
        assertEquals(10, validEnemy.getHealth());
        assertEquals(10, validEnemy.getMaxHealth());
        assertEquals(30, validEnemy.getScore());
        assertEquals(weapon, validEnemy.getEquippedWeapon());
        assertTrue(validEnemy.getInventory().hasItem(item));
        assertTrue(validEnemy.getInventory().hasItem(weapon));
        Player player = new Player.PlayerBuilder("Test player", 20).build();
        validEnemy.dropLoot().forEach(action -> action.execute(player));
        assertTrue(player.getInventory().hasItem(item));
        assertFalse(player.getInventory().hasItem(weapon));
    }
}
