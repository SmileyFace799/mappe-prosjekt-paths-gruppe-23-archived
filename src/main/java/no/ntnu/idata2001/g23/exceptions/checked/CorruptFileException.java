package no.ntnu.idata2001.g23.exceptions.checked;

/**
 * Thrown when reading data from a file failed.
 */
public class CorruptFileException extends Exception {
    public CorruptFileException(String message) {
        super(message);
    }
}
