package no.ntnu.idata2001.g23.model.fileparsing;

import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.actions.GoldAction;
import no.ntnu.idata2001.g23.model.actions.HealthAction;
import no.ntnu.idata2001.g23.model.actions.InventoryAction;
import no.ntnu.idata2001.g23.model.actions.ScoreAction;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.MiscItem;
import no.ntnu.idata2001.g23.model.misc.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ActionParserTest {
    private Provider<Item> itemProvider;

    @BeforeEach
    void before() {
        itemProvider = new Provider<>();
        itemProvider.addProvidable("Test item", () ->
                new MiscItem(500, "Test Item", "Test description"));
    }

    /**
     * Asserts that a {@link CorruptFileException} is thrown,
     * and asserts that the exception thrown is of the specified type.
     *
     * @param actionString The action string to parse
     * @param type The {@link CorruptFileException.Type} to check for
     */
    private void assertCfeType(String actionString, CorruptFileException.Type type) {
        CorruptFileException exception = assertThrows(CorruptFileException.class, () ->
                ActionParser.parseAction(actionString, 0, itemProvider));
        assertEquals(type, exception.getType());
    }

    @Test
    void testParsingOfValidActions() {
        String validGoldActionString = "Gold: 500";
        String validHealthActionString = "Health: 5";
        String validInventoryActionString = "Inventory: Test item";
        String validScoreActionString = "Score: 50";

        Action parsedGoldAction = assertDoesNotThrow(() ->
                ActionParser.parseAction(validGoldActionString, 0, itemProvider));
        Action parsedHealthAction = assertDoesNotThrow(() ->
                ActionParser.parseAction(validHealthActionString, 0, itemProvider));
        Action parsedInventoryAction = assertDoesNotThrow(() ->
                ActionParser.parseAction(validInventoryActionString, 0, itemProvider));
        Action parsedScoreAction = assertDoesNotThrow(() ->
                ActionParser.parseAction(validScoreActionString, 0, itemProvider));

        Action validGoldAction = new GoldAction(500);
        Action validHealthAction = new HealthAction(5);
        Action validInventoryAction =
                new InventoryAction(itemProvider.provide("Test item"));
        Action validScoreAction = new ScoreAction(50);

        assertEquals(validGoldAction, parsedGoldAction);
        assertEquals(validHealthAction, parsedHealthAction);
        assertEquals(validInventoryAction, parsedInventoryAction);
        assertEquals(validScoreAction, parsedScoreAction);
    }

    @Test
    void testParsingFileWithInvalidActionFormat() {
        assertCfeType("Invalid action format",
                CorruptFileException.Type.ACTION_INVALID_FORMAT);
    }

    @Test
    void testParsingFileWithInvalidActionType() {
        assertCfeType("Invalid type: Some value",
                CorruptFileException.Type.ACTION_INVALID_TYPE);
    }

    @Test
    void testParsingFileWithInvalidActionValue() {
        assertCfeType("Gold: Invalid value",
                CorruptFileException.Type.ACTION_INVALID_VALUE);
        assertCfeType("Health: Invalid value",
                CorruptFileException.Type.ACTION_INVALID_VALUE);
        assertCfeType("Inventory: Invalid value",
                CorruptFileException.Type.ACTION_INVALID_VALUE);
        assertCfeType("Score: Invalid value",
                CorruptFileException.Type.ACTION_INVALID_VALUE);
    }
}
