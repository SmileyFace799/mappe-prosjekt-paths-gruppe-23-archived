package no.ntnu.idata2001.g23.model.actions;

import no.ntnu.idata2001.g23.model.entities.Entity;

/**
 * Alters the players score.
 */
public class ScoreAction implements Action {
    private final int points;

    public ScoreAction(int points) {
        this.points = points;
    }

    @Override
    public void execute(Entity entity) {
        entity.changeScore(points);
    }

    @Override
    public String getDetails() {
        return (points < 0 ? "-" : "+") + Math.abs(points) + " Points";
    }

    /**
     * Test for content equality between two objects.
     *
     * @param obj The object to compare to this one
     * @return True if the argument object is a score action with matching parameters
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
        ScoreAction scoreAction = (ScoreAction) obj;
        return points == scoreAction.points;
    }

    /**
     * Compute a hashCode using the rules found in "Effective java" by Joshua Bloch.
     *
     * @return A hashCode for the score action, using all its parameters
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Integer.hashCode(points);
        return hash;
    }
}
