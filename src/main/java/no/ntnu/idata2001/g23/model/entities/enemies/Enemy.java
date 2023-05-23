package no.ntnu.idata2001.g23.model.entities.enemies;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.actions.GoldAction;
import no.ntnu.idata2001.g23.model.actions.HealthAction;
import no.ntnu.idata2001.g23.model.actions.InventoryAction;
import no.ntnu.idata2001.g23.model.actions.ScoreAction;
import no.ntnu.idata2001.g23.model.entities.Actor;
import no.ntnu.idata2001.g23.model.entities.Entity;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.Weapon;

/**
 * An entity that is hostile towards the player.
 */
public class Enemy extends Entity implements Actor {
    private final double itemDropChance;
    private final boolean canDropWeapon;
    private final double escapeChance;

    /**
     * Creates an enemy. This should only be called by {@link EnemyBuilder}.
     *
     * @param name           The name of the enemy
     * @param health         How much health the enemy has
     * @param score          The amount of score the enemy will give upon death
     * @param gold           How much gold the enemy will give upon death
     * @param itemDropChance The chance for the enemy to drop any of it's inventory items
     * @param canDropWeapon  If the enemy can drop their equipped weapon
     * @param escapeChance   The chance of the player successfully escaping this enemy
     */
    protected Enemy(
            String name,
            int health,
            int score,
            int gold,
            double itemDropChance,
            boolean canDropWeapon,
            double escapeChance
    ) {
        super(name, health, score, gold);
        if (itemDropChance < 0 || itemDropChance > 1) {
            throw new IllegalArgumentException(
                    "double \"itemDropChance\" cannot be less than 0 or greater than 1");
        }
        if (escapeChance < 0 || escapeChance > 1) {
            throw new IllegalArgumentException(
                    "double \"escapeChance\" cannot be less than 0 or greater than 1");
        }
        this.itemDropChance = itemDropChance;
        this.canDropWeapon = canDropWeapon;
        this.escapeChance = escapeChance;
    }

    /**
     * Makes an enemy drop all their loot.
     *
     * @return A list of actions that will give away the enemy's dropped loot
     */
    public List<Action> dropLoot() {
        List<Action> dropActions = new ArrayList<>();
        dropActions.add(new GoldAction(getGold()));
        dropActions.add(new ScoreAction(getScore()));
        Weapon equippedWeapon = getEquippedWeapon();
        if (equippedWeapon != null && !canDropWeapon) {
            //Removes weapon so enemy can't drop it
            getInventory().removeItem(equippedWeapon);
        }
        getInventory().getContents().forEach(item -> {
            if (itemDropChance > Math.random()) {
                dropActions.add(new InventoryAction(item));
            }
        });

        //Enemy loses all their loot after dropping it
        changeGold(-getGold());
        changeScore(-getScore());
        equipWeapon(null);
        getInventory().clearItems();

        return dropActions;
    }

    public double getEscapeChance() {
        return escapeChance;
    }

    /**
     * Gets every detail about the enemy that the player should know.
     *
     * @return A string of enemy details
     */
    public String getDetails() {
        Weapon equippedWeapon = getEquippedWeapon();
        return String.format("%nHealth: %s/%s", getHealth(), getMaxHealth())
                + "\nWeapon: " + (equippedWeapon != null ? equippedWeapon.getName() : "None");
    }

    /**
     * The attacking AI of an enemy.
     *
     * <p>Does the following:</p>
     * <ul>
     *     <li>Checks if the player can be targeted</li>
     *     <li>Checks if there is an equipped weapon</li>
     *     <li>If both previous conditions are true,
     *     deal damage to the player equal to the equipped weapon's base damage</li>
     * </ul>
     *
     * @param possibleTargets A collection of all possible targets.
     * @return A map of the enemy's actions, and the targets of them
     */
    @Override
    public Map<Action, List<Entity>> act(List<Entity> possibleTargets) {
        //LinkedHashMap keeps insertion order for consistent attacks
        Map<Action, List<Entity>> actions = new LinkedHashMap<>();
        Entity target = possibleTargets
                .stream()
                .filter(Player.class::isInstance)
                .findFirst()
                .orElse(null);
        Weapon weapon = getEquippedWeapon();
        if (target != null && weapon != null) {
            actions.put(new HealthAction(-weapon.getDamage()), List.of(target));
        }
        return actions;
    }

    /**
     * Test for content equality between two objects.
     *
     * @param obj The object to compare to this one
     * @return True if the argument object is ae enemy action with matching parameters
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

        Enemy enemy = (Enemy) obj;
        return super.equals(obj)
                && itemDropChance == enemy.itemDropChance
                && canDropWeapon == enemy.canDropWeapon
                && escapeChance == enemy.escapeChance;
    }

    /**
     * Compute a hashCode using the rules found in "Effective java" by Joshua Bloch.
     *
     * @return A hashCode for the enemy, using all its parameters
     */
    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 31 * hash + Double.hashCode(itemDropChance);
        hash = 31 * hash + Boolean.hashCode(canDropWeapon);
        hash = 31 * hash + Double.hashCode(escapeChance);
        return hash;
    }

    /**
     * Builder pattern, builds an enemy.
     */
    public static class EnemyBuilder {
        //Required
        private final String name;
        private final int health;

        //Optional
        private int score;
        private int gold;
        private List<Item> items;
        private Weapon weapon;
        private double itemDropChance;
        private boolean canDropWeapon;
        private double escapeChance;

        /**
         * Makes a builder for the enemy.
         *
         * @param name   The enemy's name
         * @param health The enemy's health
         */
        public EnemyBuilder(String name, int health) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("String \"name\" cannot be null or blank");
            }
            if (health < 0) {
                throw new IllegalArgumentException("int \"health\" must be 0 or greater");
            }
            this.name = name;
            this.health = health;

            //Default optional values
            this.score = 0;
            this.gold = 0;
            this.items = List.of();
            this.weapon = null;
            this.itemDropChance = 0.5;
            this.canDropWeapon = true;
            this.escapeChance = 0.5;
        }

        /**
         * Sets the score the enemy will give upon being killed.
         *
         * @param score The enemy's new score
         * @return The builder
         * @throws IllegalArgumentException If the enemy's new score is less than 0
         */
        public EnemyBuilder setScore(int score) {
            if (score < 0) {
                throw new IllegalArgumentException("int \"score\" must be 0 or greater");
            }
            this.score = score;
            return this;
        }

        /**
         * Sets the gold the enemy will give upon being killed.
         *
         * @param gold The enemy's new gold
         * @return The builder
         * @throws IllegalArgumentException If the enemy's new gold is less than 0
         */
        public EnemyBuilder setGold(int gold) {
            if (gold < 0) {
                throw new IllegalArgumentException("int \"gold\" must be 0 or greater");
            }
            this.gold = gold;
            return this;
        }

        /**
         * Sets the enemy's starting items.
         *
         * @param items The enemy's new list of starting items
         * @return The builder
         * @throws IllegalArgumentException If the list of new items contains {@code null}
         */
        public EnemyBuilder setStartingItems(List<Item> items) {
            if (items != null && items.stream().anyMatch(Objects::isNull)) {
                throw new IllegalArgumentException("List \"items\" cannot contain null");
            }
            this.items = items;
            return this;
        }

        /**
         * Sets the enemy's equipped weapon.
         * Will also be added to the enemy's list of items upon building.
         *
         * @param weapon The weapon to set as equipped
         * @return The builder
         */
        public EnemyBuilder setEquippedWeapon(Weapon weapon) {
            this.weapon = weapon;
            return this;
        }

        /**
         * Sets the chance of each item being dropped upon death.
         *
         * @param itemDropChance The new item drop chance
         * @return The builder
         */
        public EnemyBuilder setItemDropChance(double itemDropChance) {
            if (itemDropChance < 0 || itemDropChance > 1) {
                throw new IllegalArgumentException(
                        "double \"itemDropChance\" cannot be less than 0 or greater than 1");
            }
            this.itemDropChance = itemDropChance;
            return this;
        }

        /**
         * Sets if the enemy can drop its weapon upon death.
         * The weapon will still have to pass the item drop chance to successfully drop.
         *
         * @param dropWeapon If the enemy's weapon can be dropped upon death
         * @return The builder
         */
        public EnemyBuilder canDropWeapon(boolean dropWeapon) {
            this.canDropWeapon = dropWeapon;
            return this;
        }

        /**
         * Sets the chance of the player successfully escaping this enemy, if they try to move away.
         *
         * @param escapeChance The new escape chance
         * @return The builder
         */
        public EnemyBuilder setEscapeChance(double escapeChance) {
            if (escapeChance < 0 || escapeChance > 1) {
                throw new IllegalArgumentException(
                        "double \"escapeChance\" cannot be less than 0 or greater than 1");
            }
            this.escapeChance = escapeChance;
            return this;
        }

        /**
         * Builds the enemy.
         *
         * @return The enemy that was made
         */
        public Enemy build() {
            Enemy enemy = new Enemy(
                    name,
                    health,
                    score,
                    gold,
                    itemDropChance,
                    canDropWeapon,
                    escapeChance);
            enemy.setHealth(health);
            for (Item item : items) {
                enemy.getInventory().addItem(item);
            }
            if (weapon != null) {
                enemy.getInventory().addItem(weapon);
                enemy.equipWeapon(weapon);
            }
            return enemy;
        }
    }
}
