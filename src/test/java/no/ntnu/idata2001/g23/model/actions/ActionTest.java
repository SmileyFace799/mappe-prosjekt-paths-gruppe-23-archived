package no.ntnu.idata2001.g23.model.actions;

import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.items.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ActionTest {
    private Player player;

    @BeforeEach
    void before() {
        player = new Player.PlayerBuilder("Test player", 10)
                .setGold(20)
                .setScore(30)
                .build();
    }

    @Test
    void testGoldActionExecute() {
        GoldAction validGoldAction = new GoldAction(-3);
        validGoldAction.execute(player);
        assertEquals(17, player.getGold());
    }

    @Test
    void testHealthActionExecute() {
        HealthAction validHealthAction = new HealthAction(-4);
        validHealthAction.execute(player);
        assertEquals(6, player.getHealth());
    }

    @Test
    void testInventoryActionExecute() {
        Item item = new Item("Test item", "Test description");
        InventoryAction validInventoryAction = new InventoryAction(item);
        validInventoryAction.execute(player);
        assertTrue(player.getInventory().hasItem(item));
    }

    @Test
    void testScoreActionExecute() {
        ScoreAction validScoreAction = new ScoreAction(-5);
        validScoreAction.execute(player);
        assertEquals(25, player.getScore());
    }

    @Test
    void testCreationOfInventoryActionWithNoItem() {
        assertThrows(IllegalArgumentException.class, () -> new InventoryAction(null));
    }

    //TODO: Test equals
}
