package no.ntnu.idata2001.g23.model.story;

/**
 * Thrown when reading data from a file failed.
 * Has a specific {@link CorruptFileExceptionType} that describes exactly what went wrong.
 */
public class CorruptFileException extends Exception {
    private final CorruptFileExceptionType type;

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
    public CorruptFileException(CorruptFileExceptionType type) {
        super(type.getMessage());
        this.type = type;
    }

    /**
     * Makes a corrupt file exception.
     *
     * @param type The specific type of this exception
     * @param lineNumber The line number where this exception occurred
     */
    public CorruptFileException(CorruptFileExceptionType type, int lineNumber) {
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
    public CorruptFileException(CorruptFileExceptionType type,
                                int lineNumber,
                                String invalidParamValue) {
        super(type.getMessage()
                + MORE_INFO
                + getLineText(lineNumber)
                + getInvalidParamText(invalidParamValue));
        this.type = type;
    }

    public CorruptFileExceptionType getType() {
        return type;
    }
}
