package no.ntnu.idata2001.g23.model.entities;

import no.ntnu.idata2001.g23.exceptions.unchecked.BlankStringException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NegativeNumberException;

/**
 * Represents a player with different attributes which can be affected in a story.
 */
public class Player extends GenericEntity {
    private final String name; //required
    private int score; //optional
    private int gold; //optional

    /**
     * Creates a player. This should only be called by {@link PlayerBuilder}.
     *
     * @param name      The name of the player
     * @param maxHealth How much max health the player has
     * @param score     The player's score
     * @param gold      How much gold the player has
     */
    private Player(String name, int maxHealth, int score, int gold) {
        super(maxHealth, 16);
        if (name == null || name.isBlank()) {
            throw new BlankStringException("Name cannot be null or blank");
        }
        this.name = name;
        this.score = score;
        this.gold = gold;
    }

    public String toString() {
        return String.format("%s (Health: %d, Score: %d, Gold: %d)", name, health, score, gold);
    }

    // no setters, only getters - to provide immutability

    /**
     * Returns the players name.
     *
     * @return The players name
     */
    public String getName() {
        return name;
    }

    /**
     * Adds points to the players score.
     *
     * @param points increases players score.
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Shows the players current score.
     *
     * @return players score
     */
    public int getScore() {
        return score;
    }

    /**
     * Increases players gold.
     *
     * @param gold increases players gold.
     */
    public void changeGold(int gold) {
        if (this.gold + gold < 0) {
            throw new NegativeNumberException("Resulting amount of gold cannot be less than 0");
        }
        this.gold += gold;
    }

    /**
     * Shows amount of gold the player has.
     *
     * @return amount of gold
     */
    public int getGold() {
        return gold;
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

        /**
         * Makes a builder for the player.
         *
         * @param name The player's name
         * @param maxHealth The player's max health
         */
        public PlayerBuilder(String name, int maxHealth) {
            this.name = name;
            this.maxHealth = maxHealth;

            //Set default optional values here
            health = maxHealth;
            score = 0;
            gold = 0;
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

        /**
         * Builds the player.
         *
         * @return The player object that was made
         */
        public Player build() {
            Player player = new Player(name, maxHealth, score, gold);
            player.setHealth(health);

            return player;
        }
    }
}