package no.ntnu.idata2001.g23.goals;

import no.ntnu.idata2001.g23.entities.Player;

/**
 * Represents a goal value or wanted result related to the player's state.
 * Makes it possible to check if the player has achieved an expected result.
 */
public interface Goal {
    boolean isFulfilled(Player player);
}