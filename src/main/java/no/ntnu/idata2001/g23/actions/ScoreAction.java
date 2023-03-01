package no.ntnu.idata2001.g23.actions;

import no.ntnu.idata2001.g23.entities.Player;

/**
 * Alters the players score.
 */
public class ScoreAction implements Action {
    private int points;

    ScoreAction(int points) {
        this.points = points;
    }
    @Override
    public void execute(Player player) {

    }
}
