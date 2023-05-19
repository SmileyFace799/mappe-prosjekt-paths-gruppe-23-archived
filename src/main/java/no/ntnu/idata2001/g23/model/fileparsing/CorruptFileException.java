package no.ntnu.idata2001.g23.model.fileparsing;

/**
 * Thrown when reading data from a file failed.
 * Has a specific {@link Type Type} that describes exactly what went wrong.
 */
public class CorruptFileException extends Exception {
    //This oddly specific string was used enough where SonarLint started wanted it as a constant
    private static final String MISSING_COLON_SEPARATOR_STRING =
            "Couldn't find the \":\"-separator ";
    private static final String MORE_INFO = "\n\nADDITIONAL INFORMATION:";
    private final CorruptFileException.Type type;

    /**
     * Makes a corrupt file exception.
     *
     * @param type The specific type of this exception
     */
    public CorruptFileException(CorruptFileException.Type type) {
        super(type.getMessage());
        this.type = type;
    }

    /**
     * Makes a corrupt file exception.
     *
     * @param type       The specific type of this exception
     * @param lineNumber The line number where this exception occurred
     */
    public CorruptFileException(CorruptFileException.Type type, int lineNumber) {
        super(type.getMessage()
                + MORE_INFO
                + getLineText(lineNumber));
        this.type = type;
    }

    /**
     * Makes a corrupt file exception.
     *
     * @param type              The specific type of this exception
     * @param lineNumber        The line number where this exception occurred
     * @param invalidParamValue The invalid parameter that caused the exception
     */
    public CorruptFileException(CorruptFileException.Type type,
                                int lineNumber,
                                String invalidParamValue) {
        super(type.getMessage()
                + MORE_INFO
                + getLineText(lineNumber)
                + getInvalidParamText(invalidParamValue));
        this.type = type;
    }

    private static String getLineText(int lineNumber) {
        return "\nLine: " + lineNumber;
    }

    private static String getInvalidParamText(String paramValue) {
        return "\nInvalid parameter: \"" + paramValue + "\"";
    }

    public CorruptFileException.Type getType() {
        return type;
    }

    /**
     * Every type of exception that can occur when some part of a game fails to load.
     * These mainly exist for reliable unit testing of file parsing.
     */
    public enum Type {
        EMPTY_FILE("File is empty"),

        //Info exceptions
        UNKNOWN_INFO("An unknown error occurred while attempting to read the info file"),
        INFO_INVALID_PATH("An invalid path was found in the info file"),
        INFO_MISSING_DIFFICULTIES(
                "A \".difficulties\"-file is required & missing from the game"),
        INFO_MISSING_STORY("A \".paths\"-file is required & missing from the game"),
        INFO_MISSING_PLAYER("A \".player\"-file is required & missing from the game"),
        INFO_MISSING_GOALS("A \".goals\"-file is required & missing from the game"),

        //Difficulty exceptions
        UNKNOWN_DIFFICULTIES(
                "An unknown error occurred while attempting to read the difficulties file"),
        NO_DIFFICULTIES("The \".difficulties\"-file doesn't contain any difficulties"),

        //Story exceptions
        UNKNOWN_STORY("An unknown error occurred while attempting to read the story file"),
        NO_TITLE("Story is missing a title"),
        INVALID_PASSAGE("Expected to find a passage, "
                + "found non-empty line that isn't a passage"),
        NO_PASSAGES("Reached end of file without finding any passages for the story"),
        PASSAGE_NO_NAME("Found passage with no name"),
        PASSAGE_NO_CONTENT("Found passage with no content"),
        LINK_NO_TEXT("Cannot find link text"),
        LINK_NO_REFERENCE("Cannot find link reference"),
        LINK_INVALID_ACTION("Expected to find action, "
                + "found text not enclosed in matching curly brackets"),
        ACTION_INVALID_FORMAT(MISSING_COLON_SEPARATOR_STRING
                + "that separates the action type & value"),
        ACTION_INVALID_TYPE("Cannot recognize action type"),
        ACTION_INVALID_VALUE("Value cannot be assigned to action"),

        //Item exceptions
        UNKNOWN_ITEMS("An unknown error occurred while attempting to read the items file"),
        INVALID_ITEM("Expected to find an item, found non-empty line that isn't an item"),
        ITEM_NO_NAME("Found item with no name"),
        ITEM_NO_TYPE("Found item with no type"),
        ITEM_INVALID_TYPE("Found item with invalid type"),
        ITEM_INVALID_PARAMETER_VALUE("A parameter value for an item is invalid"),

        //Player exceptions
        UNKNOWN_PLAYER("An unknown error occurred while attempting to read the player file"),
        NO_PLAYER("Found difficulty with no player"),
        PLAYER_INVALID_VALUE("A parameter value for the player is invalid"),

        //Goal exceptions
        UNKNOWN_GOALS("An unknown error occurred while attempting to read the goals file"),
        NO_GOALS("Found difficulty with no goals"),
        GOAL_INVALID_FORMAT(MISSING_COLON_SEPARATOR_STRING
                + "that separates the goal type & value"),
        GOAL_INVALID_TYPE("Cannot recognize goal type"),
        GOAL_INVALID_VALUE("Value cannot be assigned to goal"),

        //Enemy exceptions
        UNKNOWN_ENEMIES(
                "An unknown error occurred while attempting to read the enemies file"),
        INVALID_ENEMY("Expected to find an enemy, "
                + "found non-empty line that isn't an enemy"),
        ENEMY_NO_NAME("Found enemy with no name"),
        ENEMY_INVALID_TYPE("Found enemy with invalid type"),
        ENEMY_INVALID_PARAMETER_VALUE("A parameter value for an enemy is invalid"),

        //Sprite exceptions
        UNKNOWN_SPRITES(
                "An unknown error occurred while attempting to read the sprites file"),
        INVALID_SPRITE("An invalid path for a sprite was found"),

        //Map parsing exceptions
        ENTRY_INVALID_FORMAT(MISSING_COLON_SEPARATOR_STRING
                + "that separates the parameter name & value"),
        REQUIRED_KEY_MISSING(
                "List of parameters is missing one or more required parameters"),
        ;
        private final String message;

        Type(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
