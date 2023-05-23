package no.ntnu.idata2001.g23.model.entities.enemies;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.actions.HealthAction;
import no.ntnu.idata2001.g23.model.entities.Entity;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.Weapon;

/**
 * An enemy that heals as it does damage.
 */
public class VampireEnemy extends Enemy {

    /**
     * Creates a vampire enemy. This should only be called by {@link VampireEnemyBuilder}.
     *
     * @param name           The name of the enemy
     * @param health         How much health the enemy has
     * @param score          The amount of score the enemy will give upon death
     * @param gold           How much gold the enemy will give upon death
     * @param itemDropChance The chance for the enemy to drop any of it's inventory items
     * @param canDropWeapon  If the enemy can drop their equipped weapon
     * @param escapeChance   The chance of the player successfully escaping this enemy
     */
    protected VampireEnemy(
            String name,
            int health,
            int score,
            int gold,
            double itemDropChance,
            boolean canDropWeapon,
            double escapeChance
    ) {
        super(name, health, score, gold, itemDropChance, canDropWeapon, escapeChance);
    }

    @Override
    public String getDetails() {
        return super.getDetails() + "\nLife-steals for half of equipped weapon's damage";
    }

    /**
     * Does the same as a regular enemy,
     * but also heals itself for half the equipped weapon's base damage.
     *
     * @param possibleTargets A collection of all possible targets.
     * @return A map of the enemy's actions, and the targets of them
     */
    @Override
    public Map<Action, List<Entity>> act(List<Entity> possibleTargets) {
        Map<Action, List<Entity>> actions = super.act(possibleTargets);
        Weapon equippedWeapon = getEquippedWeapon();
        if (equippedWeapon != null) {
            actions.put(new HealthAction(equippedWeapon.getDamage() / 2), List.of(this));
        }
        return actions;
    }

    /**
     * Builder pattern, builds an enemy.
     */
    public static class VampireEnemyBuilder {
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
         * @param health The enemy's max health
         */
        public VampireEnemyBuilder(String name, int health) {
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
        public VampireEnemyBuilder setScore(int score) {
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
        public VampireEnemyBuilder setGold(int gold) {
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
        public VampireEnemyBuilder setStartingItems(List<Item> items) {
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
        public VampireEnemyBuilder setEquippedWeapon(Weapon weapon) {
            this.weapon = weapon;
            return this;
        }

        /**
         * Sets the chance of each item being dropped upon death.
         *
         * @param itemDropChance The new item drop chance
         * @return The builder
         */
        public VampireEnemyBuilder setItemDropChance(double itemDropChance) {
            if (itemDropChance < 0 || itemDropChance > 1) {
                throw new IllegalArgumentException(
                        "double \"itemDropChance\" must be a number between 0 and 1 (inclusive)");
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
        public VampireEnemyBuilder canDropWeapon(boolean dropWeapon) {
            this.canDropWeapon = dropWeapon;
            return this;
        }

        /**
         * Sets the chance of the player successfully escaping this enemy, if they try to move away.
         *
         * @param escapeChance The new escape chance
         * @return The builder
         */
        public VampireEnemyBuilder setEscapeChance(double escapeChance) {
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
        public VampireEnemy build() {
            VampireEnemy vampireEnemy = new VampireEnemy(
                    name, health, score, gold, itemDropChance, canDropWeapon, escapeChance);
            for (Item item : items) {
                vampireEnemy.getInventory().addItem(item);
            }
            if (weapon != null) {
                vampireEnemy.getInventory().addItem(weapon);
                vampireEnemy.equipWeapon(weapon);
            }
            return vampireEnemy;
        }
    }
}
