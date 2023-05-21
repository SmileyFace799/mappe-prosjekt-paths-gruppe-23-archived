package no.ntnu.idata2001.g23.model.actions;

import no.ntnu.idata2001.g23.model.entities.Entity;

/**
 * Represents a future change in the player's state. Changes the player's state along the way.
 */
public interface Action {
    void execute(Entity entity);

    /**
     * A detailed string that describes what the action will do.
     *
     * @return Details about what the action will do
     */
    String getDescriptiveText();

    /**
     * A string of text that says what the action did upon execution.
     *
     * @return What the action did upon execution
     */
    String getExecutedText();
}
