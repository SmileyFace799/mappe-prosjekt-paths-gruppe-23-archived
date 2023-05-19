package no.ntnu.idata2001.g23.model.entities.enemies;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.actions.HealthAction;
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

    /**
     * Creates an enemy. This should only be called by {@link EnemyBuilder}.
     *
     * @param name      The name of the enemy
     * @param maxHealth How much max health the enemy has
     * @param score     The amount of score the enemy will give upon death
     * @param gold      How much gold the enemy will give upon death
     */
    private Enemy(
            String name,
            int maxHealth,
            int score,
            int gold,
            double itemDropChance,
            boolean canDropWeapon) {
        super(name, maxHealth, score, gold);
        if (itemDropChance < 0 || itemDropChance > 1) {
            throw new IllegalArgumentException(
                    "double \"itemDropChance\" cannot be less than 0 or greater than 1");
        }
        this.itemDropChance = itemDropChance;
        this.canDropWeapon = canDropWeapon;
    }

    public double getItemDropChance() {
        return itemDropChance;
    }

    public boolean canDropWeapon() {
        return canDropWeapon;
    }

    /**
     * The attacking AI of an enemy.
     *
     * <p>Does the following:
     * <ul>
     *     <li>Checks if the player can be targeted</li>
     *     <li>Checks if there is an equipped weapon</li>
     *     <li>If both previous conditions are true,
     *     deal damage to the player equal to the equipped weapon's base damage</li>
     * </ul></p>
     *
     * @param possibleTargets A collection of all possible targets.
     */
    @Override
    public Map<Action, List<Entity>> act(Collection<Entity> possibleTargets) {
        //LinkedHashMap keeps insertion order for consistent attacks
        Map<Action, List<Entity>> actions = new LinkedHashMap<>();
        Entity target = possibleTargets
                .stream()
                .filter(Player.class::isInstance)
                .findFirst()
                .orElse(null);
        Weapon weapon = getEquippedWeapon();
        if (target != null && weapon != null) {
            actions.put(new HealthAction(-weapon.getBaseDamage()), List.of(target));
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
                && canDropWeapon == enemy.canDropWeapon;
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
        return hash;
    }

    /**
     * Builder pattern, builds an enemy.
     */
    public static class EnemyBuilder {
        //Required
        private final String name;
        private final int maxHealth;

        //Optional
        private int health;
        private int score;
        private int gold;
        private List<Item> items;
        private Weapon weapon;
        private double itemDropChance;
        private boolean canDropWeapon;

        /**
         * Makes a builder for the enemy.
         *
         * @param name      The enemy's name
         * @param maxHealth The enemy's max health
         */
        public EnemyBuilder(String name, int maxHealth) {
            this.name = name;
            this.maxHealth = maxHealth;

            //Default optional values
            health = maxHealth;
            score = 0;
            gold = 0;
            items = List.of();
            weapon = null;
            itemDropChance = 0.5;
            canDropWeapon = true;
        }

        public EnemyBuilder setHealth(int health) {
            this.health = health;
            return this;
        }

        public EnemyBuilder setScore(int score) {
            this.score = score;
            return this;
        }

        public EnemyBuilder setGold(int gold) {
            this.gold = gold;
            return this;
        }

        public EnemyBuilder setStartingItems(List<Item> items) {
            this.items = items;
            return this;
        }

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
                        "double \"itemDropChance\" must be a number between 0 and 1 (inclusive)");
            }
            this.itemDropChance = itemDropChance;
            return this;
        }

        public EnemyBuilder canDropWeapon(boolean dropWeapon) {
            this.canDropWeapon = dropWeapon;
            return this;
        }

        /**
         * Builds the enemy.
         *
         * @return The enemy that was made
         */
        public Enemy build() {
            Enemy enemy = new Enemy(name, maxHealth, score, gold, itemDropChance, canDropWeapon);
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
