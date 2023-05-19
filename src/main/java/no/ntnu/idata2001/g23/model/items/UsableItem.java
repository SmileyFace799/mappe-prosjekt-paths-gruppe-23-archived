package no.ntnu.idata2001.g23.model.items;

import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.entities.Entity;

/**
 * An item that can be used & consumed upon use.
 */
public class UsableItem extends Item {
    private final Action onUse;

    /**
     * Makes a usable item.
     *
     * @param value       The base value of the item, used to calculate sell price.
     *                    If the value is 0, it will not be sellable.
     * @param name        The item's name.
     * @param description The item's description.
     */
    public UsableItem(int value, String name, String description, Action onUse) {
        super(value, name, description);
        if (onUse == null) {
            throw new IllegalArgumentException("Action \"onUse\" cannot be null");
        }
        this.onUse = onUse;
    }

    public void use(Entity entity) {
        onUse.execute(entity);
    }

    @Override
    public String toString() {
        return super.toString()
                + "\nOn use: " + onUse.toString();
    }

    /**
     * Test for content equality between two objects.
     *
     * @param obj The object to compare to this one
     * @return True if the argument object is a usable item with matching parameters
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
        UsableItem usableItem = (UsableItem) obj;
        return super.equals(usableItem)
                && onUse.equals(usableItem.onUse);
    }

    /**
     * Compute a hashCode using the rules found in "Effective java" by Joshua Bloch.
     *
     * @return A hashCode for the usable item, using all its parameters
     */
    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 31 * hash + onUse.hashCode();
        return hash;
    }
}
