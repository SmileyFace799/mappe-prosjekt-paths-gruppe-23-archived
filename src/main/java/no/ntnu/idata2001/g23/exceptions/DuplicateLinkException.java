package no.ntnu.idata2001.g23.exceptions;

public class DuplicateLinkException extends Exception{
    public DuplicateLinkException(String nameOfLink) {
        super("\"" + nameOfLink + "\" is already exists");
    }
}
