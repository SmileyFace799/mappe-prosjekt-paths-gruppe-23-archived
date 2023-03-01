package no.ntnu.idata2001.g23.goals;

import no.ntnu.idata2001.g23.entities.Player;

/**
 * A generic goal that checks if a player fulfills something.
 */
public interface Goal {
    boolean isFulfilled(Player player);
}
