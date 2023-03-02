package no.ntnu.idata2001.g23.items.weapons;

import no.ntnu.idata2001.g23.exceptions.unchecked.NegativeNumberException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NumberOutOfRangeException;
import no.ntnu.idata2001.g23.items.Item;

/**
 * Any item that can be equipped as a weapon to damage enemies.
 */
public abstract class Weapon extends Item {
    protected final int baseDamage;
    protected final double baseCritChance;

    /**
     * Makes a basic weapon.
     *
     * @param baseDamage     The weapon's base damage.
     * @param baseCritChance The chance for the weapon to critically strike its target.
     * @param value          The base value of the weapon, used to calculate sell price.
     *                       If the value is 0, it will not be sellable.
     * @param name           The weapon's name.
     * @param description    The weapon's description.
     */
    protected Weapon(int baseDamage, double baseCritChance, int value,
                     String name, String description) {
        super(value, name, description, 1);
        if (baseDamage < 0) {
            throw new NegativeNumberException("int \"damage\" cannot be negative");
        }
        if (baseCritChance < 0 || baseCritChance > 1) {
            throw new NumberOutOfRangeException(
                    "double \"baseCritChance\" cannot be outside the range \"0 <= x <= 1\"");
        }
        this.baseDamage = baseDamage;
        this.baseCritChance = baseCritChance;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    @Override
    public final int getMaxStackSize() {
        return 1;
    }

    public double getBaseCritChance() {
        return baseCritChance;
    }

    @Override
    public String toString() {
        return "Value: " + value
                + "\nSellable: " + isSellable()
                + "\nName: " + name
                + "\nDescription: " + description
                + "\nCategory: " + getCategory()
                + "\nBase damage: " + baseDamage
                + "\nBase critical strike chance: " + baseCritChance
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
        Weapon weapon = (Weapon) obj;
        return value == weapon.getValue()
                && name.equals(weapon.getName())
                && description.equals(weapon.getDescription())
                && baseDamage == weapon.getBaseDamage()
                && baseCritChance == weapon.getBaseCritChance();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + value;
        hash = 31 * hash + name.hashCode();
        hash = 31 * hash + description.hashCode();
        hash = 31 * hash + baseDamage;
        hash = 31 * hash + Double.hashCode(baseCritChance);
        return hash;
    }
}
