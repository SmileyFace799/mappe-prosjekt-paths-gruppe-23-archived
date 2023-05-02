package no.ntnu.idata2001.g23.model.items;

/**
 * An item without any special features.
 */
public class MiscItem extends Item {
    /**
     * Makes a miscellaneous item, with no special properties.
     *
     * @param value       The base value of the item, used to calculate sell price.
     *                    If the value is 0, it will not be sellable.
     * @param name The item's name.
     * @param description The item's description.
     */
    public MiscItem(int value, String name, String description) {
        super(value, name, description);
    }

    @Override
    public String getCategory() {
        return "Miscellaneous";
    }

    @Override
    public String toString() {
        return "Value: " + getValue()
                + "\nSellable: " + isSellable()
                + "\nName: " + getName()
                + "\nDescription: " + getDescription()
                + "\nCategory: " + getCategory();
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
        MiscItem miscItem = (MiscItem) obj;
        return getValue() == miscItem.getValue()
                && getName().equals(miscItem.getName())
                && getDescription().equals(miscItem.getDescription());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + getValue();
        hash = 31 * hash + getName().hashCode();
        hash = 31 * hash + getDescription().hashCode();
        return hash;
    }
}
