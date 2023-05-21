package no.ntnu.idata2001.g23.model.entities.enemies;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
     * Creates an enemy. This should only be called by {@link VampireEnemyBuilder}.
     *
     * @param name           The name of the enemy
     * @param maxHealth      How much max health the enemy has
     * @param score          The amount of score the enemy will give upon death
     * @param gold           How much gold the enemy will give upon death
     * @param itemDropChance The chance for the enemy to drop any of it's inventory items
     * @param canDropWeapon  If the enemy can drop their equipped weapon
     */
    protected VampireEnemy(
            String name,
            int maxHealth,
            int score,
            int gold,
            double itemDropChance,
            boolean canDropWeapon
    ) {
        super(name, maxHealth, score, gold, itemDropChance, canDropWeapon);
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
    public Map<Action, List<Entity>> act(Collection<Entity> possibleTargets) {
        Map<Action, List<Entity>> actions = super.act(possibleTargets);
        Weapon equippedWeapon = getEquippedWeapon();
        if (equippedWeapon != null) {
            actions.put(new HealthAction(equippedWeapon.getBaseDamage() / 2), List.of(this));
        }
        return actions;
    }

    /**
     * Builder pattern, builds an enemy.
     */
    public static class VampireEnemyBuilder {
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
        public VampireEnemyBuilder(String name, int maxHealth) {
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

        public VampireEnemyBuilder setHealth(int health) {
            this.health = health;
            return this;
        }

        public VampireEnemyBuilder setScore(int score) {
            this.score = score;
            return this;
        }

        public VampireEnemyBuilder setGold(int gold) {
            this.gold = gold;
            return this;
        }

        public VampireEnemyBuilder setStartingItems(List<Item> items) {
            this.items = items;
            return this;
        }

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

        public VampireEnemyBuilder canDropWeapon(boolean dropWeapon) {
            this.canDropWeapon = dropWeapon;
            return this;
        }

        /**
         * Builds the enemy.
         *
         * @return The enemy that was made
         */
        public VampireEnemy build() {
            VampireEnemy vampireEnemy = new VampireEnemy(
                    name, maxHealth, score, gold, itemDropChance, canDropWeapon);
            vampireEnemy.setHealth(health);
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
