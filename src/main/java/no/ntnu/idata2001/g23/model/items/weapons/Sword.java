package no.ntnu.idata2001.g23.model.items.weapons;

/**
 * A melee weapon that can attack close-range targets, typically for great damage.
 */
public class Sword extends Weapon {
    /**
     * Makes a sword.
     *
     * @param baseDamage     The sword's base damage.
     * @param baseCritChance The chance for the sword to critically strike its target.
     * @param value          The base value of the sword, used to calculate sell price.
     *                       If the value is 0, it will not be sellable.
     * @param name           The sword's name.
     * @param description    The sword's description.
     */
    public Sword(int baseDamage, double baseCritChance, int value,
                 String name, String description) {
        super(baseDamage, baseCritChance, value, name, description);
    }

    @Override
    public String getCategory() {
        return "Weapon -> Sword";
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
