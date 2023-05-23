package no.ntnu.idata2001.g23.model.items;

/**
 * Any item that can be equipped as a weapon to damage enemies.
 */
public class Weapon extends Item {
    protected final int damage;

    /**
     * Makes a basic weapon.
     *
     * @param damage     The weapon's damage.
     * @param name           The weapon's name.
     * @param description    The weapon's description.
     */
    public Weapon(int damage,
                     String name, String description) {
        super(name, description);
        if (damage < 0) {
            throw new IllegalArgumentException("int \"damage\" cannot be negative");
        }
        this.damage = damage;
    }

    /**
     * Gets the damage field.
     *
     * @return The damage field
     */
    public int getDamage() {
        return damage;
    }

    @Override
    public String getDetails() {
        return super.getDetails()
                + "\nDamage: " + getDamage();
    }

    /**
     * Test for content equality between two objects.
     *
     * @param obj The object to compare to this one
     * @return True if the argument object is a weapon with matching parameters
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
        Weapon weapon = (Weapon) obj;
        return super.equals(weapon)
                && damage == weapon.damage;
    }

    /**
     * Compute a hashCode using the rules found in "Effective java" by Joshua Bloch.
     *
     * @return A hashCode for the weapon, using all its parameters
     */
    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 31 * hash + Integer.hashCode(damage);
        return hash;
    }
}
