package no.ntnu.idata2001.g23.entities;

import no.ntnu.idata2001.g23.exceptions.ElementNotFoundException;
import no.ntnu.idata2001.g23.exceptions.NegativeOrZeroNumberException;
import no.ntnu.idata2001.g23.itemhandling.Inventory;
import no.ntnu.idata2001.g23.items.Item;
import no.ntnu.idata2001.g23.items.weapons.Weapon;

/**
 * A generic entity.
 */
public abstract class GenericEntity {
    protected final Inventory<Item> inventory;
    protected int health;
    protected int maxHealth;
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
     * Changes an entity's health.
     * If the resulting entity health exceeds the entity's max health,
     * the health will be set to the max health.
     * If the resulting entity health is less than 0,
     * the health will be set to 0.
     *
     * @param health The health to add or remove. Positive amounts add health,
     *               negative amounts remove health.
     */
    public void changeHealth(int health) {
        this.health = Math.max(Math.min(this.health + health, maxHealth), 0);
    }


    /**
     * Adds an item to the inventory.
     *
     * @param index Inventory slot to put item at
     * @param item Adds item to the inventory
     */
    public void addToInventory(int index, Item item) {
        getInventory().changeItem(index, item);
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
