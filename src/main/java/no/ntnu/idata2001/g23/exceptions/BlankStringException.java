package no.ntnu.idata2001.g23.exceptions;

public class BlankStringException extends RuntimeException {
    public BlankStringException(String nameOfBlankString) {
        super("\"" + nameOfBlankString + "\" string is either null of blank");
    }
}
