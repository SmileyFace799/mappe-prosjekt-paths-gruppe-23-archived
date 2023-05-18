package no.ntnu.idata2001.g23.model.goals;

import java.util.Objects;
import no.ntnu.idata2001.g23.model.entities.Player;

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
            throw new IllegalArgumentException("Minimum score must be greater than 0");
        }
        this.minimumPoints = minimumPoints;
    }

    @Override
    public boolean isFulfilled(Player player) {
        return player.getScore() >= minimumPoints;
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
        ScoreGoal scoreGoal = (ScoreGoal) obj;
        return Objects.equals(minimumPoints, scoreGoal.minimumPoints);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Integer.hashCode(minimumPoints);
        return hash;
    }
}
