package no.ntnu.idata2001.g23.model.entities;

import java.util.List;
import no.ntnu.idata2001.g23.model.itemhandling.FullInventoryException;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.Weapon;

/**
 * Represents a player with different attributes which can be affected in a story.
 */
public class Player extends Entity {
    /**
     * Creates a player. This should only be called by {@link PlayerBuilder}.
     *
     * @param name      The name of the player
     * @param maxHealth How much max health the player has
     * @param score     The player's score
     * @param gold      How much gold the player has
     */
    private Player(String name, int maxHealth, int score, int gold) {
        super(name, maxHealth, score, gold);
    }

    /**
     * Builder pattern, builds a player.
     */
    public static class PlayerBuilder {
        //Required
        private final String name;
        private final int maxHealth;

        //Optional
        private int health;
        private int score;
        private int gold;
        private List<Item> items;
        private Weapon weapon;

        /**
         * Makes a builder for the player.
         *
         * @param name      The player's name
         * @param maxHealth The player's max health
         */
        public PlayerBuilder(String name, int maxHealth) {
            this.name = name;
            this.maxHealth = maxHealth;

            //Default optional values
            health = maxHealth;
            score = 0;
            gold = 0;
            items = List.of();
            weapon = null;
        }

        public PlayerBuilder setHealth(int health) {
            this.health = health;
            return this;
        }

        public PlayerBuilder setScore(int score) {
            this.score = score;
            return this;
        }

        public PlayerBuilder setGold(int gold) {
            this.gold = gold;
            return this;
        }

        public PlayerBuilder setStartingItems(List<Item> items) {
            this.items = items;
            return this;
        }

        public PlayerBuilder setEquippedWeapon(Weapon weapon) {
            this.weapon = weapon;
            return this;
        }

        /**
         * Builds the player.
         *
         * @return The player object that was made
         * @throws FullInventoryException If the player is built with more starting items
         *                                than the inventory size allows
         */
        public Player build() {
            Player player = new Player(name, maxHealth, score, gold);
            player.setHealth(health);
            for (Item item : items) {
                player.getInventory().addItem(item);
            }
            player.getInventory().addItem(weapon);
            player.equipWeapon(weapon);
            return player;
        }
    }
}