package no.ntnu.idata2001.g23.model.goals;

import java.util.Objects;
import no.ntnu.idata2001.g23.exceptions.unchecked.NegativeOrZeroNumberException;
import no.ntnu.idata2001.g23.model.entities.Player;

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
        GoldGoal goldGoal = (GoldGoal) obj;
        return Objects.equals(minimumGold, goldGoal.minimumGold);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Integer.hashCode(minimumGold);
        return hash;
    }
}
