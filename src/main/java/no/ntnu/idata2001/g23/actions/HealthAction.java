package no.ntnu.idata2001.g23.actions;

import no.ntnu.idata2001.g23.entities.Player;

/**
 * Changes the player's health.
 */
public class HealthAction implements Action {
    private int health;

    HealthAction(int health) {
        this.health = health;
    }
    @Override
    public void execute(Player player) {
        player.changeHealth(health);
    }
}
