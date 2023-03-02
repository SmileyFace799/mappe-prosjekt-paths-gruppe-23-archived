package no.ntnu.idata2001.g23.goals;

import no.ntnu.idata2001.g23.entities.Player;
import no.ntnu.idata2001.g23.exceptions.unchecked.NegativeNumberException;

/**
 * Represents a minimum expected amount of gold.
 */
public class GoldGoal implements Goal {
    private final int minimumGold;

    public GoldGoal(int minimumGold) {
        if (minimumGold < 0) {
            throw new NegativeNumberException("Minimum gold cannot be negative numbers.");
        }
        this.minimumGold = minimumGold;
    }

    @Override
    public boolean isFulfilled(Player player) {
        //TODO: add isFulfilled GoldGoal.
        return true;
    }
}
