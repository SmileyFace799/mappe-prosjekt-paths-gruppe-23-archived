package no.ntnu.idata2001.g23.model.actions;

import no.ntnu.idata2001.g23.model.entities.Entity;

/**
 * Changes the amount of gold the player has.
 */
public class GoldAction implements Action {
    private final int gold;

    public GoldAction(int gold) {
        this.gold = gold;
    }

    @Override
    public void execute(Entity entity) {
        entity.changeGold(gold);
    }

    /**
     * Test for content equality between two objects.
     *
     * @param obj The object to compare to this one
     * @return True if the argument object is a gold action with matching parameters
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
        GoldAction goldAction = (GoldAction) obj;
        return gold == goldAction.gold;
    }

    /**
     * Compute a hashCode using the rules found in "Effective java" by Joshua Bloch.
     *
     * @return A hashCode for the gold action, using all its parameters
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Integer.hashCode(gold);
        return hash;
    }
}
