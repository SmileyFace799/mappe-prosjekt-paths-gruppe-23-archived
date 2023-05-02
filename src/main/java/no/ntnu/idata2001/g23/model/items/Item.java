package no.ntnu.idata2001.g23.model.items;

import no.ntnu.idata2001.g23.exceptions.unchecked.BlankStringException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NegativeNumberException;

/**
 * Any item that can be stored in some inventory.
 */
public abstract class Item {
    private final int value;
    private final String name;
    private final String description;

    /**
     * Makes a basic item.
     *
     * @param value       The base value of the item, used to calculate sell price.
     *                    If the value is 0, it will not be sellable.
     * @param name        The item's name.
     * @param description The item's description.
     */
    protected Item(int value, String name, String description) {
        if (value < 0) {
            throw new NegativeNumberException("int \"value\" cannot be negative");
        }
        if (name == null || name.isBlank()) {
            throw new BlankStringException("String \"name\" cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new BlankStringException("String \"description\" cannot be null or blank");
        }
        this.value = value;
        this.name = name;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public boolean isSellable() {
        return value != 0;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    //TODO: Possibly remove this, obsolete?
    public abstract String getCategory();

    @Override
    public String toString() {
        return "Value: " + value
                + "\nSellable: " + isSellable()
                + "\nName: " + name
                + "\nDescription: " + description
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
        Item item = (Item) obj;
        return value == item.getValue()
                && name.equals(item.getName())
                && description.equals(item.getDescription());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + value;
        hash = 31 * hash + name.hashCode();
        hash = 31 * hash + description.hashCode();
        return hash;
    }
}
