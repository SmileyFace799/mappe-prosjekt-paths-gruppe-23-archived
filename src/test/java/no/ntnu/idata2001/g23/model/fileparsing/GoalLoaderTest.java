package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.List;
import no.ntnu.idata2001.g23.model.goals.Goal;
import no.ntnu.idata2001.g23.model.goals.GoldGoal;
import no.ntnu.idata2001.g23.model.goals.HealthGoal;
import no.ntnu.idata2001.g23.model.goals.InventoryGoal;
import no.ntnu.idata2001.g23.model.goals.ScoreGoal;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.MiscItem;
import no.ntnu.idata2001.g23.model.misc.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GoalLoaderTest {
    private Provider<Item> itemProvider;
    private String difficulty;
    private GoalLoader goalLoader;

    @BeforeEach
    void before() {
        itemProvider = new Provider<>();
        itemProvider.addProvidable("Test Item", () ->
                new MiscItem(500, "Test Item", "Test description"));
        itemProvider.addProvidable("Other Item", () ->
                new MiscItem(500, "Other Item", "Other description"));
        difficulty = "Test";
        goalLoader = new GoalLoader(itemProvider, difficulty);
    }

    /**
     * Asserts that a {@link CorruptFileException} is thrown,
     * and asserts that the exception thrown is of the specified type.
     *
     * @param goalsString The goals string to parse
     * @param type        The {@link CorruptFileException.Type} to check for
     */
    private void assertCfeType(
            String goalsString, CorruptFileException.Type type) {
        CorruptFileException exception = assertThrows(CorruptFileException.class, () -> goalLoader
                .parseGoals(new LineNumberReader(new StringReader(goalsString))));
        assertEquals(type, exception.getType());
    }

    @Test
    void testLoadingOfValidGoals() {
        String validGoalsString = String.format("""
                #Easy
                Gold: 5000
                Health: 50
                Inventory: Test Item
                Score: 500
                                        
                #%s
                Gold: 10000
                Health: 100
                Inventory: Other Item
                Score: 1000
                """, difficulty);

        List<Goal> parsedGoals = assertDoesNotThrow(() -> goalLoader
                .parseGoals(new LineNumberReader(new StringReader(validGoalsString))));

        List<Goal> validGoals = List.of(
                new GoldGoal(10000),
                new HealthGoal(100),
                new InventoryGoal(itemProvider.provide("Other Item")),
                new ScoreGoal(1000)
        );

        assertEquals(validGoals, parsedGoals);
    }

    @Test
    void testLoadingOfGoalsWithNoGoals() {
        assertCfeType("", CorruptFileException.Type.NO_GOALS);
        assertCfeType(String.format("#%s", difficulty), CorruptFileException.Type.NO_GOALS);
        assertCfeType(String.format("""
                #Other difficulty
                Gold: 5000
                                        
                #%s
                """, difficulty), CorruptFileException.Type.NO_GOALS);

        //Shouldn't throw, should only care about the goalLoader's difficulty, and nothing else
        assertDoesNotThrow(() -> goalLoader.parseGoals(new LineNumberReader(new StringReader(
                String.format("""
                        #Other difficulty
                        
                        #%s
                        Gold: 10000
                        """, difficulty)))));
    }

    @Test
    void testLoadingOfGoalsWithInvalidFormat() {
        assertCfeType(String.format("""
                #%s
                Invalid format
                """, difficulty), CorruptFileException.Type.GOAL_INVALID_FORMAT);
    }

    @Test
    void testLoadingOfGoalsWithInvalidType() {
        assertCfeType(String.format("""
                #%s
                Invalid Type: Value
                """, difficulty), CorruptFileException.Type.GOAL_INVALID_TYPE);
    }

    @Test
    void testLoadingOfGoalsWithInvalidValue() {
        assertCfeType(String.format("""
                #%s
                Gold: -1
                """, difficulty), CorruptFileException.Type.GOAL_INVALID_VALUE);

        assertCfeType(String.format("""
                #%s
                Health: Value
                """, difficulty), CorruptFileException.Type.GOAL_INVALID_VALUE);

        assertCfeType(String.format("""
                #%s
                Inventory: 5
                """, difficulty), CorruptFileException.Type.GOAL_INVALID_VALUE);

        assertCfeType(String.format("""
                #%s
                Score: 0.05
                """, difficulty), CorruptFileException.Type.GOAL_INVALID_VALUE);
    }
}
