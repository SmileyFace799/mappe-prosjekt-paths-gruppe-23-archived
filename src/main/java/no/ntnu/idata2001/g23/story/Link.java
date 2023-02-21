package no.ntnu.idata2001.g23.story;

import no.ntnu.idata2001.g23.exceptions.BlankStringException;

/**
 * Connects passages between the different parts of a story.
 */
public class Link {
    // The text
    private final String text;
    // Where the link leads to
    private final String reference;
    // Actions that affects the players attributes
    //TODO: Add action methods after we have finished oppgave 7.
    //private final ArrayList<Action> actions = new ArrayList<>();

    /**
     * Creates a link.
     *
     * @param text      The text indicates a choice or action in the story.
     * @param reference The reference identifies a passage (a part of the story).
     */
    // @param actions Actions are objects that can affect the attributes of a player.
    public Link(String text, String reference) {
        if (text == null || text.isBlank()) {
            throw new BlankStringException("Text cannot be null or blank");
        }
        if (reference == null || reference.isBlank()) {
            throw new BlankStringException("Reference cannot be null or blank");
        }
        this.text = text.trim();
        this.reference = reference.trim();
    }

    /**
     * Returns the text.
     *
     * @return The text.
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the reference.
     *
     * @return The reference.
     */
    public String getReference() {
        return reference;
    }
    //public void addAction(actions Action) throws duplicateActionException{}
    //public String getActions() {return getActions;}
    //public void setGetActions(String getActions) {this.getActions = getActions;}

    /**
     * Returns a multi-line string containing the text, reference and actions.
     *
     * @return A multi-line string containing the text, reference and actions.
     */
    public String toString() {
        return text + "\n" + reference + "\n"; //+ actions;
    }

    /**
     * Test for content equality between two objects.
     *
     * @param other The object to compare to this one.
     * @return True if the argument object is a set of links with matching attributes.
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Link otherLink) {
            return text.equals(otherLink.getText())
                    && reference.equals(otherLink.getReference());
        } else {
            return false;
        }
    }

    /**
     * Compute a hashCode using the rules found in "Effective java" by Joshua Bloch.
     *
     * @return A hashCode for Links.
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + text.hashCode();
        result = 37 * result + reference.hashCode();
        return result;
    }
}
