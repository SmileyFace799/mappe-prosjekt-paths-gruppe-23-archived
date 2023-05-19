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

    /**
     * Test for content equality between two objects.
     *
     * @param obj The object to compare to this one
     * @return True if the argument object is a score goal with matching parameters
     */
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

    /**
     * Compute a hashCode using the rules found in "Effective java" by Joshua Bloch.
     *
     * @return A hashCode for the score goal, using all its parameters
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Integer.hashCode(minimumPoints);
        return hash;
    }
}
