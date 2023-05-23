package no.ntnu.idata2001.g23.model.items;

import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.entities.Entity;

/**
 * An item that can be used and consumed upon use.
 */
public class UsableItem extends Item {
    private final Action onUse;

    /**
     * Makes a usable item.
     *
     * @param name        The item's name.
     * @param description The item's description.
     * @param onUse       The action to execute when the item is used
     */
    public UsableItem(String name, String description, Action onUse) {
        super(name, description);
        if (onUse == null) {
            throw new IllegalArgumentException("Action \"onUse\" cannot be null");
        }
        this.onUse = onUse;
    }

    /**
     * Gets the onUse field.
     *
     * @return The onUse field
     */
    public Action getUseAction() {
        return onUse;
    }

    /**
     * Uses the item on an entity.
     *
     * @param entity The entity to use the item on
     */
    public void use(Entity entity) {
        onUse.execute(entity);
    }

    @Override
    public String getDetails() {
        return super.getDetails()
                + "\nOn use: " + onUse.getDescriptiveText();
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
