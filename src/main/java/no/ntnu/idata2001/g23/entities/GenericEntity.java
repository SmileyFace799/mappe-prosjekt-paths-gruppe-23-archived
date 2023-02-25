package no.ntnu.idata2001.g23.entities;

import no.ntnu.idata2001.g23.exceptions.ElementNotFoundException;
import no.ntnu.idata2001.g23.exceptions.NegativeNumberException;
import no.ntnu.idata2001.g23.exceptions.NegativeOrZeroNumberException;
import no.ntnu.idata2001.g23.itemhandling.Inventory;
import no.ntnu.idata2001.g23.items.Item;
import no.ntnu.idata2001.g23.items.weapons.Weapon;

/**
 * A generic entity.
 */
public abstract class GenericEntity implements Actor {
    protected int health;
    protected int maxHealth;
    protected final Inventory<Item> inventory;
    protected Weapon equippedWeapon;

    protected GenericEntity(int maxHealth, int inventorySize) {
        if (maxHealth <= 0) {
            throw new NegativeOrZeroNumberException("Max health must be greater than 0");
        }
        this.maxHealth = maxHealth;
        this.inventory = new Inventory<>(inventorySize);
        this.equippedWeapon = null;
    }

    public int getHealth() {
        return health;
    }

    public boolean isDead() {
        return health == 0;
    }

    public Inventory<Item> getInventory() {
        return inventory;
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    /**
     * Adds health to an entity.
     *
     * @param health The health to add, cannot be negative.
     *               If the resulting entity health exceeds the entity's max health,
     *               the health will be set to the max health.
     */
    public void addHealth(int health) {
        if (health < 0) {
            throw new NegativeNumberException("Added health cannot be negative");
        }
        this.health = Math.min(this.health + health, maxHealth);
    }

    /**
     * Removes health from an entity.
     *
     * @param health The health to remove, cannot be negative.
     *               If the resulting entity health is less than 0,
     *               the health will be set to 0.
     */
    public void removeHealth(int health) {
        if (health < 0) {
            throw new NegativeNumberException("Removed health cannot be negative");
        }
        this.health = Math.max(this.health - health, 0);
    }

    /**
     * Assigns a weapon for the player to use during combat.
     *
     * @param inventoryIndex The index of the weapon in the inventory.
     */
    public void equipWeapon(int inventoryIndex) {
        if (inventory.getItem(inventoryIndex) instanceof Weapon weapon) {
            equippedWeapon = weapon;
        } else {
            throw new ElementNotFoundException("No weapon found at the provided index");
        }
    }
}
