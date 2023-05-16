package no.ntnu.idata2001.g23.model.entities;

import no.ntnu.idata2001.g23.exceptions.unchecked.BlankStringException;
import no.ntnu.idata2001.g23.exceptions.unchecked.ElementNotFoundException;
import no.ntnu.idata2001.g23.model.itemhandling.FullInventoryException;
import no.ntnu.idata2001.g23.model.itemhandling.Inventory;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.UsableItem;
import no.ntnu.idata2001.g23.model.items.Weapon;

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
            throw new BlankStringException("Name cannot be null or blank");
        }
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.score = score;
        this.gold = gold;
        this.inventory = new Inventory(16);
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
     * @throws ElementNotFoundException If the specified item is not in the entity's inventory
     * @see UsableItem
     */
    public void useItem(UsableItem item) {
        if (getInventory().hasItem(item)) {
            item.use(this);
            getInventory().removeItem(item);
        } else {
            throw new ElementNotFoundException("\"item\" is not in the entity's inventory");
        }
    }

    /**
     * Assigns a weapon for the entity to use during combat.
     *
     * @param inventoryIndex The index of the weapon in the inventory
     */
    public void equipWeapon(int inventoryIndex) {
        if (inventory.getItem(inventoryIndex) instanceof Weapon weapon) {
            equippedWeapon = weapon;
        } else {
            throw new ElementNotFoundException("No weapon found at the provided index");
        }
    }

    /**
     * Assigns a weapon for the entity to use during combat.
     *
     * @param weapon The weapon to equip. If it's not in the entity's inventory, it will be added
     * @throws FullInventoryException If the weapon is not in the entity's inventory,
     *                                and there is no space to add it
     */
    public void equipWeapon(Weapon weapon) throws FullInventoryException {
        if (!inventory.hasItem(weapon)) {
            inventory.addItem(weapon);
        }
        equippedWeapon = weapon;
    }

    /**
     * Abstract implementation of builder pattern, so that classes that extend {@link Entity} can
     * also have a builder that extends this.
     */
    protected abstract static class AbstractEntityBuilder<E extends Entity> {
        //Required
        protected final String name;
        protected final int maxHealth;

        //Optional
        protected int health;
        protected int score;
        protected int gold;
        protected Item[] items;

        /**
         * Makes a builder for the entity.
         *
         * @param name      The entity's name
         * @param maxHealth The entity's max health
         */
        protected AbstractEntityBuilder(String name, int maxHealth) {
            this.name = name;
            this.maxHealth = maxHealth;

            //Default optional values
            health = maxHealth;
            score = 0;
            gold = 0;
            items = new Item[0];
        }

        public AbstractEntityBuilder<E> setHealth(int health) {
            this.health = health;
            return this;
        }

        public AbstractEntityBuilder<E> setScore(int score) {
            this.score = score;
            return this;
        }

        public AbstractEntityBuilder<E> setGold(int gold) {
            this.gold = gold;
            return this;
        }

        public AbstractEntityBuilder<E> setStartingItems(Item... items) {
            this.items = items;
            return this;
        }

        protected E build(E entity) {
            entity.setHealth(health);
            for (Item item : items) {
                entity.getInventory().addItem(item);
            }
            return entity;
        }

        /**
         * Builds the entity.
         *
         * @return The entity object that was made
         * @throws FullInventoryException If the entity is built with more starting items
         *                                than the inventory size allows
         */
        protected abstract E build();
    }
}