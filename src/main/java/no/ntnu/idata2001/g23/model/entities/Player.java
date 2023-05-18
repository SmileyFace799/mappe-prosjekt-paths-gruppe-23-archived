package no.ntnu.idata2001.g23.model.entities;

import java.util.List;
import java.util.Objects;
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
        private final int health;

        //Optional
        private int score;
        private int gold;
        private List<Item> items;
        private Weapon weapon;

        /**
         * Makes a builder for the player.
         *
         * @param name   The player's name
         * @param health The player's health
         */
        public PlayerBuilder(String name, int health) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("String \"name\" cannot be null or blank");
            }
            if (health < 0) {
                throw new IllegalArgumentException("int \"health\" must be 0 or greater");
            }
            this.name = name;
            this.health = health;

            //Default optional values
            score = 0;
            gold = 0;
            items = List.of();
            weapon = null;
        }

        /**
         * Sets the player's score.
         *
         * @param score The player's new score
         * @return The builder
         * @throws IllegalArgumentException If the player's new score is less than 0
         */
        public PlayerBuilder setScore(int score) {
            if (score < 0) {
                throw new IllegalArgumentException("int \"score\" must be 0 or greater");
            }
            this.score = score;
            return this;
        }

        /**
         * Sets the player's gold.
         *
         * @param gold The player's new gold
         * @return The builder
         * @throws IllegalArgumentException If the player's new gold is less than 0
         */
        public PlayerBuilder setGold(int gold) {
            if (gold < 0) {
                throw new IllegalArgumentException("int \"gold\" must be 0 or greater");
            }
            this.gold = gold;
            return this;
        }

        /**
         * Sets the player's starting items.
         *
         * @param items The player's new list of starting items
         * @return The builder
         * @throws IllegalArgumentException If the list of new items contains {@code null}
         */
        public PlayerBuilder setStartingItems(List<Item> items) {
            if (items != null && items.stream().anyMatch(Objects::isNull)) {
                throw new IllegalArgumentException("List \"items\" cannot contain null");
            }
            this.items = items;
            return this;
        }

        /**
         * Sets the player's equipped weapon.
         *
         * @param weapon The player's new equipped weapon
         * @return The builder
         */
        public PlayerBuilder setEquippedWeapon(Weapon weapon) {
            this.weapon = weapon;
            return this;
        }

        /**
         * Builds the player.
         *
         * @return The player that was made
         */
        public Player build() {
            Player player = new Player(name, health, score, gold);
            for (Item item : items) {
                player.getInventory().addItem(item);
            }
            if (weapon != null) {
                player.getInventory().addItem(weapon);
                player.equipWeapon(weapon);
            }
            return player;
        }
    }
}