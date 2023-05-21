package no.ntnu.idata2001.g23.model.goals;

import java.util.Objects;
import no.ntnu.idata2001.g23.model.entities.Player;

/**
 * Represents a minimum expected health value.
 */
public class HealthGoal implements Goal {
    private final int minimumHealth;

    /**
     * Makes a health goal for the player to achieve.
     *
     * @param minimumHealth The minimum amount of health the player must have to complete this goal.
     */
    public HealthGoal(int minimumHealth) {
        if (minimumHealth <= 0) {
            throw new IllegalArgumentException("Minimum health must be greater than 0");
        }
        this.minimumHealth = minimumHealth;
    }

    @Override
    public boolean isFulfilled(Player player) {
        return player.getHealth() >= minimumHealth;
    }

    @Override
    public String getDescriptiveText() {
        return String.format("Get at least %s health", minimumHealth);
    }

    /**
     * Test for content equality between two objects.
     *
     * @param obj The object to compare to this one
     * @return True if the argument object is a health goal with matching parameters
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
        HealthGoal healthGoal = (HealthGoal) obj;
        return Objects.equals(minimumHealth, healthGoal.minimumHealth);
    }

    /**
     * Compute a hashCode using the rules found in "Effective java" by Joshua Bloch.
     *
     * @return A hashCode for the health goal, using all its parameters
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Integer.hashCode(minimumHealth);
        return hash;
    }
}
