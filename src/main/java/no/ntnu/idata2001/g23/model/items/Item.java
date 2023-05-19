package no.ntnu.idata2001.g23.model.items;

/**
 * Any item that can be stored in some inventory.
 */
public class Item {
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
    public Item(int value, String name, String description) {
        if (value < 0) {
            throw new IllegalArgumentException("int \"value\" cannot be negative");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("String \"name\" cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("String \"description\" cannot be null or blank");
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

    @Override
    public String toString() {
        return "Value: " + getValue()
                + "\nSellable: " + isSellable()
                + "\nName: " + getName()
                + "\nDescription: " + getDescription();
    }

    /**
     * Test for content equality between two objects.
     *
     * @param obj The object to compare to this one
     * @return True if the argument object is a item with matching parameters
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
        Item item = (Item) obj;
        return value == item.value
                && name.equals(item.name)
                && description.equals(item.description);
    }

    /**
     * Compute a hashCode using the rules found in "Effective java" by Joshua Bloch.
     *
     * @return A hashCode for the item, using all its parameters
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Integer.hashCode(value);
        hash = 31 * hash + name.hashCode();
        hash = 31 * hash + description.hashCode();
        return hash;
    }
}
