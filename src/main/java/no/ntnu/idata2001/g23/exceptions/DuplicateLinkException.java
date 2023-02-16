package no.ntnu.idata2001.g23.exceptions;

public class DuplicateLinkException extends RuntimeException {
    public DuplicateLinkException(String errorMessage) {
        super(errorMessage);
    }
}
