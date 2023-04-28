package no.ntnu.idata2001.g23.model.goals;

import java.util.Objects;
import no.ntnu.idata2001.g23.exceptions.unchecked.NegativeOrZeroNumberException;
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
            throw new NegativeOrZeroNumberException("Minimum health must be greater than 0");
        }
        this.minimumHealth = minimumHealth;
    }

    @Override
    public boolean isFulfilled(Player player) {
        return player.getHealth() >= minimumHealth;
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
        HealthGoal healthGoal = (HealthGoal) obj;
        return Objects.equals(minimumHealth, healthGoal.minimumHealth);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Integer.hashCode(minimumHealth);
        return hash;
    }
}
