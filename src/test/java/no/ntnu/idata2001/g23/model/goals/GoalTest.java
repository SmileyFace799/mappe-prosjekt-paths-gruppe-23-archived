package no.ntnu.idata2001.g23.model.goals;

import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.items.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GoalTest {
    private Player player;

    @BeforeEach
    void before() {
        player = new Player.PlayerBuilder("Test player", 10)
                .setGold(20)
                .setScore(30)
                .build();
    }

    @Test
    void testGoldGoalExecute() {
        assertTrue(new GoldGoal(20).isFulfilled(player));
        assertFalse(new GoldGoal(21).isFulfilled(player));
    }

    @Test
    void testHealthGoalExecute() {
        assertTrue(new HealthGoal(10).isFulfilled(player));
        assertFalse(new HealthGoal(11).isFulfilled(player));
    }

    @Test
    void testInventoryGoalExecute() {
        Item item = new Item("Test item", "Test description");
        player.getInventory().addItem(item);
        Item otherItem = new Item("Other item", "Other description");
        assertTrue(new InventoryGoal(item).isFulfilled(player));
        assertFalse(new InventoryGoal(otherItem).isFulfilled(player));
    }

    @Test
    void testScoreGoalExecute() {
        assertTrue(new ScoreGoal(30).isFulfilled(player));
        assertFalse(new ScoreGoal(31).isFulfilled(player));
    }

    @Test
    void testCreationOfInventoryGoalWithNoItem() {
        assertThrows(IllegalArgumentException.class, () -> new InventoryGoal(null));
    }

    //TODO: Test equals
}
