package no.ntnu.idata2001.g23.model.fileparsing;

/**
 * Thrown when reading data from a file failed.
 * Has a specific {@link CorruptFileException.Type} that describes exactly what went wrong.
 */
public class CorruptFileException extends Exception {
    private final CorruptFileException.Type type;

    private static final String MORE_INFO = "\n\nADDITIONAL INFORMATION:";

    private static String getLineText(int lineNumber) {
        return "\nLine: " + lineNumber;
    }

    private static String getInvalidParamText(String paramValue) {
        return "\nInvalid parameter: \"" + paramValue + "\"";
    }

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
     * @param type The specific type of this exception
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
     * @param type The specific type of this exception
     * @param lineNumber The line number where this exception occurred
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

    public CorruptFileException.Type getType() {
        return type;
    }

    public enum Type {
        UNKNOWN("An unknown error occurred while attempting to read the file"),
        EMPTY_FILE("File is empty"),

        //Info exceptions
        UNKNOWN_INFO("An unknown error occurred while attempting to read the info file"),
        INFO_INVALID_PATH("An invalid path was found"),
        NO_STORY("No story path was found"),

        //Story exceptions
        NO_TITLE("Story is missing a title"),
        INVALID_GOAL_OR_PASSAGE("Expected to find a goal or passage, "
                + "found non-empty line that isn't a goal or passage"),
        NO_GOALS("Reached end of file without finding any goals for the story"),
        DIFFICULTY_NO_NAME("Found difficulty with no name"),
        DIFFICULTY_NO_GOALS("Found difficulty with no goals"),
        GOAL_INVALID_FORMAT("Couldn't find the \":\"-separator "
                + "that separates the goal type & value"),
        GOAL_INVALID_TYPE("Cannot recognize goal type"),
        GOAL_INVALID_VALUE("Value cannot be assigned to goal"),
        NO_PASSAGES("Reached end of file without finding any passages for the story"),
        PASSAGE_NO_NAME("Found passage with no name"),
        PASSAGE_NO_CONTENT("Found passage with no content"),
        LINK_NO_TEXT("Cannot find link text"),
        LINK_NO_REFERENCE("Cannot find link reference"),
        LINK_INVALID_ACTION("Expected to find action, "
                + "found text not enclosed in matching curly brackets"),
        ACTION_INVALID_FORMAT("Couldn't find the \":\"-separator "
                + "that separates the action type & value"),
        ACTION_INVALID_TYPE("Cannot recognize action type"),
        ACTION_INVALID_VALUE("Value cannot be assigned to action"),

        //Item exceptions
        INVALID_ITEM("Expected to find an item, found non-empty line that isn't an item"),
        ITEM_NO_NAME("Found item with no name"),
        ITEM_NO_TYPE("Found item with no type"),
        ITEM_INVALID_TYPE("Found item with invalid type"),
        ITEM_INVALID_PARAMETER_VALUE("A parameter value for an item is invalid"),
        ITEM_INVALID_FORMAT("Couldn't find the \":\"-separator "
                + "that separates the item parameter name & value"),
        ITEM_MISSING_PARAMETERS("Item is missing required parameters"),
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
