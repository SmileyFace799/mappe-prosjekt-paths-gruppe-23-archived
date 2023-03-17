package no.ntnu.idata2001.g23.actions;

import no.ntnu.idata2001.g23.entities.Player;

/**
 * Alters the players score.
 */
public class ScoreAction implements Action {
    private final int points;

    public ScoreAction(int points) {
        this.points = points;
    }

    @Override
    public void execute(Player player) {
        player.addScore(points);
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
        ScoreAction scoreAction = (ScoreAction) obj;
        return points == scoreAction.points;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Integer.hashCode(points);
        return hash;
    }
}
