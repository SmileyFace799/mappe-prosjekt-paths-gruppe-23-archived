package no.ntnu.idata2001.g23.model.actions;

import no.ntnu.idata2001.g23.model.entities.Entity;

/**
 * Changes the amount of gold the entity has.
 */
public class GoldAction implements Action {
    private final int gold;

    /**
     * Makes a gold action.
     *
     * @param gold The amount of gold to add or remove
     */
    public GoldAction(int gold) {
        this.gold = gold;
    }

    @Override
    public void execute(Entity entity) {
        entity.changeGold(gold);
    }

    @Override
    public String getDescriptiveText() {
        String message;
        if (gold > 0) {
            message = String.format("Gain %s gold", Math.abs(gold));
        } else if (gold < 0) {
            message = String.format("Lose %s gold", Math.abs(gold));
        } else {
            message = null;
        }
        return message;
    }

    @Override
    public String getExecutedText() {
        String message;
        if (gold > 0) {
            message = String.format("gained %s gold", Math.abs(gold));
        } else if (gold < 0) {
            message = String.format("lost %s gold", Math.abs(gold));
        } else {
            message = null;
        }
        return message;
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
