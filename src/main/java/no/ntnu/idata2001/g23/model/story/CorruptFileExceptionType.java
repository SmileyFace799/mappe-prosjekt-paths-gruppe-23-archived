package no.ntnu.idata2001.g23.model.story;

/**
 * Types of {@link CorruptFileException} that specifies the exact cause.
 */
public enum CorruptFileExceptionType {
    UNKNOWN("An unknown error occurred while attempting to read the file"),
    EMPTY_FILE("File is empty"),
    NO_TITLE("Story is missing a title"),
    INVALID_GOAL_OR_PASSAGE("Expected to find a goal or passage,"
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
    ACTION_INVALID_VALUE("Value cannot be assigned to action");

    private final String message;

    CorruptFileExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
