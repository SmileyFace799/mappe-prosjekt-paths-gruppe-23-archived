package no.ntnu.idata2001.g23.goals;

import no.ntnu.idata2001.g23.entities.Player;

/**
 * Represents a minimum expected health value.
 */
public class HealthGoal implements Goal {
    private final int minimumHealth;

    public HealthGoal(int minimumHealth) {
        this.minimumHealth = minimumHealth;
    }

    @Override
    public boolean isFulfilled(Player player) {
        //TODO: add isFulfilled HealthGoal
        return true;
    }
}
