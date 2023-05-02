package no.ntnu.idata2001.g23.model.fileparsing;

import no.ntnu.idata2001.g23.exceptions.unchecked.ElementNotFoundException;
import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.actions.GoldAction;
import no.ntnu.idata2001.g23.model.actions.HealthAction;
import no.ntnu.idata2001.g23.model.actions.InventoryAction;
import no.ntnu.idata2001.g23.model.actions.ScoreAction;
import no.ntnu.idata2001.g23.model.itemhandling.ItemProvider;

/**
 * Utility class for parsing actions.
 */
public class ActionParser {
    private ActionParser() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Parses an action from a text string.
     *
     * @param actionData   The action data to parse
     * @param lineNumber   The line number of the action to parse,
     *                     in the larger set of data it is present in.
     *                     Used to generate a more detailed error message
     *                     if the action data can't be parsed
     * @param itemProvider The {@link ItemProvider} to use when
     *                     generating the item for an {@link InventoryAction}
     * @return The parsed action
     */
    public static Action parseAction(String actionData, int lineNumber, ItemProvider itemProvider)
            throws CorruptFileException {
        String[] splitActionData = actionData.split(":", 2);
        if (splitActionData.length < 2) {
            throw new CorruptFileException(CorruptFileException.Type.ACTION_INVALID_FORMAT,
                    lineNumber, actionData);
        }
        Action returnAction;
        String actionType = splitActionData[0].trim();
        String actionValue = splitActionData[1].trim();
        try {
            switch (actionType) {
                case "Gold" -> returnAction = new GoldAction(Integer.parseInt(actionValue));
                case "Health" -> returnAction = new HealthAction(Integer.parseInt(actionValue));
                case "Inventory" -> returnAction =
                        new InventoryAction(itemProvider.provideItem(actionValue));
                case "Score" -> returnAction = new ScoreAction(Integer.parseInt(actionValue));
                default -> throw new CorruptFileException(
                        CorruptFileException.Type.ACTION_INVALID_TYPE, lineNumber, actionType);
            }
        } catch (NumberFormatException | ElementNotFoundException e) {
            throw new CorruptFileException(CorruptFileException.Type.ACTION_INVALID_VALUE,
                    lineNumber, actionValue);
        }
        return returnAction;
    }
}
