package no.ntnu.idata2001.g23.model.entities;

import no.ntnu.idata2001.g23.model.items.UsableItem;
import no.ntnu.idata2001.g23.model.items.Weapon;
import no.ntnu.idata2001.g23.model.misc.Inventory;

/**
 * An entity with different parameters that can be affected throughout a story.
 */
public abstract class Entity {
    private final String name;
    private final int maxHealth;
    private int health;
    private int score;
    private int gold;
    private final Inventory inventory;
    private Weapon equippedWeapon;

    /**
     * Creates an entity.
     *
     * @param name      The name of the entity
     * @param maxHealth How much max health the entity has
     * @param score     The entity's score
     * @param gold      How much gold the entity has
     */
    protected Entity(String name, int maxHealth, int score, int gold) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.score = score;
        this.gold = gold;
        this.inventory = new Inventory();
    }

    /**
     * Returns the entity's name.
     *
     * @return The entity's name
     */
    public String getName() {
        return name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    /**
     * Shows the entity's current score.
     *
     * @return entity's score
     */
    public int getScore() {
        return score;
    }

    /**
     * Shows amount of gold the entiy has.
     *
     * @return amount of gold
     */
    public int getGold() {
        return gold;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    /**
     * Sets an entity's health.
     *
     * <p>If the set entity health exceeds the entity's max health,
     * the health will be set to the max health.
     * If the set entity health is less than 0,
     * the health will be set to 0.</p>
     *
     * @param health The value to set the entity's health at
     */
    public void setHealth(int health) {
        this.health = Math.max(Math.min(health, maxHealth), 0);
    }

    /**
     * Changes an entity's health.
     *
     * <p>If the resulting entity health exceeds the entity's max health,
     * the health will be set to the max health.
     * If the resulting entity health is less than 0,
     * the health will be set to 0.</p>
     *
     * @param health The health to add or remove. Positive amounts add health,
     *               negative amounts remove health
     */
    public void changeHealth(int health) {
        this.health = Math.max(Math.min(this.health + health, maxHealth), 0);
    }

    /**
     * Adds or removes points to the entity's score.
     *
     * @param points The amount of points to add or remove.
     *               Positive numbers add score, negative numbers remove score
     */
    public void changeScore(int points) {
        if (this.score + score < 0) {
            throw new IllegalArgumentException("Resulting amount of score cannot be less than 0");
        }
        this.score += points;
    }

    /**
     * Increases entity's gold.
     *
     * @param gold increases entity's gold.
     */
    public void changeGold(int gold) {
        if (this.gold + gold < 0) {
            throw new IllegalArgumentException("Resulting amount of gold cannot be less than 0");
        }
        this.gold += gold;
    }

    /**
     * Uses an item in the entity's inventory. Using the item also consumes it.
     *
     * @param item The item to use. Must exist in the entity's inventory
     * @throws IllegalArgumentException If the specified item is not in the entity's inventory
     * @see UsableItem
     */
    public void useItem(UsableItem item) {
        if (getInventory().hasItem(item)) {
            item.use(this);
            getInventory().removeItem(item);
        } else {
            throw new IllegalArgumentException("\"item\" is not in the entity's inventory");
        }
    }

    /**
     * Assigns a weapon for the entity to use during combat.
     *
     * @param weapon The weapon to equip. If it's not in the entity's inventory, it will be added
     */
    public void equipWeapon(Weapon weapon) {
        if (weapon != null && !inventory.hasItem(weapon)) {
            inventory.addItem(weapon);
        }
        equippedWeapon = weapon;
    }

    /**
     * Test for content equality between two objects.
     *
     * @param obj The object to compare to this one
     * @return True if the argument object is an entity with matching parameters
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

        Entity entity = (Entity) obj;
        return name.equals(entity.name)
                && maxHealth == entity.maxHealth
                && health == entity.health
                && score == entity.score
                && gold == entity.gold
                && inventory.equals(entity.inventory)
                && equippedWeapon.equals(entity.equippedWeapon);
    }

    /**
     * Compute a hashCode using the rules found in "Effective java" by Joshua Bloch.
     *
     * @return A hashCode for the entity, using all its parameters
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + name.hashCode();
        hash = 31 * hash + Integer.hashCode(maxHealth);
        hash = 31 * hash + Integer.hashCode(health);
        hash = 31 * hash + Integer.hashCode(score);
        hash = 31 * hash + Integer.hashCode(gold);
        hash = 31 * hash + inventory.hashCode();
        hash = 31 * hash + equippedWeapon.hashCode();
        return hash;
    }
}