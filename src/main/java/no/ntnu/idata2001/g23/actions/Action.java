package no.ntnu.idata2001.g23.actions;

import no.ntnu.idata2001.g23.entities.Player;

/**
 * An action that happens when passing from one passage to another.
 */
public interface Action {
    void execute(Player player);
}
