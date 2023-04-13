package no.ntnu.idata2001.g23.model.goals;

import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.exceptions.unchecked.NegativeOrZeroNumberException;

/**
 * Represents a minimum expected score.
 */
public class ScoreGoal implements Goal {
    int minimumPoints;

    /**
     * Makes a score goal for the player to achieve.
     *
     * @param minimumPoints The minimum amount of points the player must have to complete this goal.
     */
    public ScoreGoal(int minimumPoints) {
        if (minimumPoints <= 0) {
            throw new NegativeOrZeroNumberException("Minimum score must be greater than 0");
        }
        this.minimumPoints = minimumPoints;
    }

    @Override
    public boolean isFulfilled(Player player) {
        return player.getScore() >= minimumPoints;
    }
}
