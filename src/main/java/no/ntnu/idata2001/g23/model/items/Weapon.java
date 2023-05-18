package no.ntnu.idata2001.g23.model.items;

/**
 * Any item that can be equipped as a weapon to damage enemies.
 */
public class Weapon extends Item {
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
    public Weapon(int baseDamage, double baseCritChance, int value,
                     String name, String description) {
        super(value, name, description);
        if (baseDamage < 0) {
            throw new IllegalArgumentException("int \"damage\" cannot be negative");
        }
        if (baseCritChance < 0 || baseCritChance > 1) {
            throw new IllegalArgumentException(
                    "double \"baseCritChance\" cannot be less than 0, or greater than 1");
        }
        this.baseDamage = baseDamage;
        this.baseCritChance = baseCritChance;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public double getBaseCritChance() {
        return baseCritChance;
    }

    @Override
    public String toString() {
        return super.toString()
                + "\nBase damage: " + getBaseDamage()
                + "\nBase critical strike chance: " + getBaseCritChance();
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
        return super.equals(weapon)
                && getBaseDamage() == weapon.getBaseDamage()
                && getBaseCritChance() == weapon.getBaseCritChance();
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 31 * hash + Integer.hashCode(getBaseDamage());
        hash = 31 * hash + Double.hashCode(getBaseCritChance());
        return hash;
    }
}
