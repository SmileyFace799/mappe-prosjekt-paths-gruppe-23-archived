package no.ntnu.idata2001.g23.model.items;

import no.ntnu.idata2001.g23.exceptions.unchecked.NullValueException;
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
            throw new NullValueException("Action \"onUse\" cannot be null");
        }
        this.onUse = onUse;
    }

    @Override
    public String getCategory() {
        return "Usable item";
    }

    public void use(Entity entity) {
        onUse.execute(entity);
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
        UsableItem usableItem = (UsableItem) obj;
        return getValue() == usableItem.getValue()
                && getName().equals(usableItem.getName())
                && getDescription().equals(usableItem.getDescription())
                && onUse.equals(usableItem.onUse);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Integer.hashCode(getValue());
        hash = 31 * hash + getName().hashCode();
        hash = 31 * hash + getDescription().hashCode();
        hash = 31 * hash + onUse.hashCode();
        return hash;
    }
}
