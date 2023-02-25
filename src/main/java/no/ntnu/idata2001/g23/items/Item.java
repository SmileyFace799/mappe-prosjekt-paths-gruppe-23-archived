package no.ntnu.idata2001.g23.items;

import no.ntnu.idata2001.g23.exceptions.BlankStringException;
import no.ntnu.idata2001.g23.exceptions.NegativeNumberException;
import no.ntnu.idata2001.g23.exceptions.NumberOutOfRangeException;

/**
 * Any item that can be stored in some inventory.
 */
public abstract class Item {
    protected final int value;
    protected final String name;
    protected final String description;
    protected int stackSize;

    /**
     * Makes a basic item.
     *
     * @param value       The base value of the item, used to calculate sell price.
     *                    If the value is 0, it will not be sellable.
     * @param name        The item's name.
     * @param description The item's description.
     * @param stackAmount The initial amount of stacked items.
     */
    protected Item(int value, String name, String description, int stackAmount) {
        if (value < 0) {
            throw new NegativeNumberException("int \"value\" cannot be negative");
        }
        if (name == null || name.isBlank()) {
            throw new BlankStringException("String \"name\" cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new BlankStringException("String \"description\" cannot be null or blank");
        }
        if (stackAmount <= 0 || stackAmount > getMaxStackSize()) {
            throw new NumberOutOfRangeException("int \"stackAmount\" must be between"
                    + "1 & the item's max stack size");
        }
        this.value = value;
        this.name = name;
        this.description = description;
        this.stackSize = stackAmount;
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

    public abstract String getCategory();

    public int getCurrentStackSize() {
        return stackSize;
    }

    public abstract int getMaxStackSize();

    public boolean isStackable() {
        return getMaxStackSize() > 1;
    }

    public int getStackValue() {
        return value * stackSize;
    }

    /**
     * Increases the size of the stack. If the stack cannot hold all the items added,
     * the remaining quantity of items is returned. If the stack can hold all the items added,
     * 0 is returned instead.
     *
     * @param amount The amount to increase the stack size by,
     *               must be between 1 and the max stack size.
     * @return The remaining quantity of items that didn't fit in the stack. 0 if all items fit.
     */
    public int increaseStack(int amount) {
        if (amount < 1 || amount > getMaxStackSize()) {
            throw new NumberOutOfRangeException(
                    "int \"amount\" must be between 0 and the max stack size");
        }
        int amountLeft = Math.max(stackSize + amount - getMaxStackSize(), 0);
        stackSize += amount - amountLeft;
        return amountLeft;
    }

    /**
     * Decreases the size of the stack.
     *
     * @param amount The amount to decrease the stack size by,
     *               must be between 0, and 1 less than the stack size.
     */
    public void decreaseStack(int amount) {
        if (amount < 1 || amount > stackSize - 1) {
            throw new NumberOutOfRangeException(
                    "int \"amount\" must be between 0 and 1 less than the stack size");
        }
        stackSize -= amount;
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
        Item item = (Item) obj;
        return value == item.getValue()
                && name.equals(item.getName())
                && description.equals(item.getDescription())
                && stackSize == item.getCurrentStackSize();
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
