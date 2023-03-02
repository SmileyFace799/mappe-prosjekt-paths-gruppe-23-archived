package no.ntnu.idata2001.g23.actions;

import no.ntnu.idata2001.g23.entities.Player;

/**
 * Represents a future change in the player's state. Changes the player's state along the way.
 */
public interface Action {
    void execute(Player player);
}
