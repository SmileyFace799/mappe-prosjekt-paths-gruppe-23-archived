package no.ntnu.idata2001.g23.goals;

import no.ntnu.idata2001.g23.entities.Player;
import no.ntnu.idata2001.g23.exceptions.unchecked.NegativeOrZeroNumberException;

/**
 * Represents a minimum expected amount of gold.
 */
public class GoldGoal implements Goal {
    private final int minimumGold;

    /**
     * Makes a gold goal for the player to achieve.
     *
     * @param minimumGold The minimum amount of gold the player must have to complete this goal.
     */
    public GoldGoal(int minimumGold) {
        if (minimumGold <= 0) {
            throw new NegativeOrZeroNumberException("Minimum gold must be greater than 0");
        }
        this.minimumGold = minimumGold;
    }

    @Override
    public boolean isFulfilled(Player player) {
        return player.getGold() >= minimumGold;
    }
}
