package no.ntnu.idata2001.g23.goals;

import no.ntnu.idata2001.g23.entities.Player;
import no.ntnu.idata2001.g23.exceptions.NegativeOrZeroNumberException;

/**
 * Represents a minimum expected score.
 */
public class ScoreGoal implements Goal {
    int minimumPoints;

    private ScoreGoal(int minimumPoints) {
        if (minimumPoints <= 0) {
            throw new NegativeOrZeroNumberException("Score cannot be less than or 0.");
        }
        this.minimumPoints = minimumPoints;
    }

    @Override
    public boolean isFulfilled(Player player) {
        //TODO: Add isFulfilled ScoreGoal
    }
}
