package no.ntnu.idata2001.g23.model.actions;

import no.ntnu.idata2001.g23.model.entities.Player;

/**
 * Changes the player's health.
 */
public class HealthAction implements Action {
    private final int health;

    public HealthAction(int health) {
        this.health = health;
    }

    @Override
    public void execute(Player player) {
        player.changeHealth(health);
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
        HealthAction healthAction = (HealthAction) obj;
        return health == healthAction.health;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Integer.hashCode(health);
        return hash;
    }
}
