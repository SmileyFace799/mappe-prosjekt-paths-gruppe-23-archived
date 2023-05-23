package no.ntnu.idata2001.g23.model.goals;

import no.ntnu.idata2001.g23.model.entities.Player;

/**
 * Represents a goal value or wanted result related to the player's state.
 * Makes it possible to check if the player has achieved an expected result.
 */
public interface Goal {
    /**
     * Checks if the player fulfills a goal.
     *
     * @param player The player to check if fulfills the goal
     * @return True if the player fulfills the goal
     */
    boolean isFulfilled(Player player);

    /**
     * A descriptive text string that explains the requirements for this goal.
     *
     * @return A string containing the goal's requirements
     */
    String getDescriptiveText();
}