package no.ntnu.idata2001.g23.model.items;

/**
 * Any item that can be stored in some inventory.
 */
public class Item {
    private final String name;
    private final String description;

    /**
     * Makes a basic item.
     *
     * @param name        The item's name
     * @param description The item's description
     */
    public Item(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("String \"name\" cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("String \"description\" cannot be null or blank");
        }
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the name field.
     *
     * @return The name field
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description field.
     *
     * @return The description field
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets all the item's details in a multiline string.
     *
     * @return The item's details
     */
    public String getDetails() {
        return "Name: " + getName()
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
        return name.equals(item.name)
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
        hash = 31 * hash + name.hashCode();
        hash = 31 * hash + description.hashCode();
        return hash;
    }
}
