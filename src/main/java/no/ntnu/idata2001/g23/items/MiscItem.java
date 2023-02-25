package no.ntnu.idata2001.g23.items;

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
     * @param stackAmount The item's initial stack amount.
     */
    public MiscItem(int value, String name, String description, int stackAmount) {
        super(value, name, description, stackAmount);
    }

    public MiscItem(int value, String name, String description) {
        this(value, name, description, 1);
    }

    public MiscItem(MiscItem original) {
        this(original.value, original.name, original.description, original.getCurrentStackSize());
    }

    @Override
    public String getCategory() {
        return "Miscellaneous";
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }

    @Override
    public String toString() {
        return "Value: " + value
                + "\nSellable: " + isSellable()
                + "\nName: " + name
                + "\nDescription: " + description
                + "\nCategory: " + getCategory()
                + "\nCurrent stack size: " + stackSize
                + "\nMax stack size: " + getMaxStackSize()
                + "\nStackable: " + isStackable();
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
        return value == miscItem.getValue()
                && name.equals(miscItem.getName())
                && description.equals(miscItem.getDescription())
                && stackSize == miscItem.getCurrentStackSize();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + value;
        hash = 31 * hash + name.hashCode();
        hash = 31 * hash + description.hashCode();
        hash = 31 * hash + stackSize;
        return hash;
    }
}
