package no.ntnu.idata2001.g23.model.story;

import java.util.ArrayList;
import java.util.List;
import no.ntnu.idata2001.g23.model.actions.Action;

/**
 * Connects passages between the different parts of a story.
 */

public class Link {
    // The text
    private final String text;
    // Where the link leads to
    private final String reference;
    // Actions that affect the player
    private final List<Action> actions;

    /**
     * Creates an instance of a link.
     *
     * @param text      The text indicates a choice or action in the story.
     * @param reference The reference identifies a passage (a part of the story).
     */
    public Link(String text, String reference) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text cannot be null or blank");
        }
        if (reference == null || reference.isBlank()) {
            throw new IllegalArgumentException("Reference cannot be null or blank");
        }

        this.text = text.trim();
        this.reference = reference.trim();
        this.actions = new ArrayList<>();
    }

    /**
     * Returns the text, which indicates a choice or an action.
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

    /**
     * Adds an action to the link.
     *
     * @param action The action to add
     * @throws IllegalArgumentException If the action is null
     */
    public void addAction(Action action) {
        if (action == null) {
            throw new IllegalArgumentException("\"action\" cannot be null");
        }
        this.actions.add(action);
    }

    /**
     * Get actions.
     *
     * @return The actions.
     */
    public List<Action> getActions() {
        return actions;
    }

    /**
     * Returns a multi-line string containing the text, reference and actions.
     *
     * @return A multi-line string containing the text, reference and actions.
     */
    public String toString() {
        return getText() + "\n" + getReference() + "\n" + getActions() + "\n";
    }

    /**
     * Test for content equality between two objects.
     *
     * @param obj The object to compare to this one.
     * @return True if the argument object is a link with matching reference.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Link link = (Link) obj;
        return getReference().equals(link.getReference());
    }

    /**
     * Compute a hashCode using the rules found in "Effective java" by Joshua Bloch.
     *
     * @return A hashCode for the link, using its reference
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + getReference().hashCode();
        return result;
    }
}
