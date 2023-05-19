package no.ntnu.idata2001.g23.model.actions;

import no.ntnu.idata2001.g23.model.entities.Entity;

/**
 * Changes the player's health.
 */
public class HealthAction implements Action {
    private final int health;

    public HealthAction(int health) {
        this.health = health;
    }

    @Override
    public void execute(Entity entity) {
        entity.changeHealth(health);
    }

    /**
     * Test for content equality between two objects.
     *
     * @param obj The object to compare to this one
     * @return True if the argument object is a health action with matching parameters
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
        HealthAction healthAction = (HealthAction) obj;
        return health == healthAction.health;
    }

    /**
     * Compute a hashCode using the rules found in "Effective java" by Joshua Bloch.
     *
     * @return A hashCode for the health action, using all its parameters
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Integer.hashCode(health);
        return hash;
    }
}
