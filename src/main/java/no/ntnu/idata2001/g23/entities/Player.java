package no.ntnu.idata2001.g23.entities;

import no.ntnu.idata2001.g23.exceptions.unchecked.BlankStringException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NegativeNumberException;

/**
 * Represents a player with different attributes which can be affected in a story.
 */
public class Player extends GenericEntity {
    private final String name;
    private int score;
    private int gold;

    /**
     * Creates a player.
     *
     * @param name   The name of the player
     * @param health How much health the player has
     * @param score  The player's score
     * @param gold   How much gold the player has
     */
    public Player(String name, int health, int score, int gold) {
        super(health, 16);
        if (name == null || name.isBlank()) {
            throw new BlankStringException("Name cannot be null or blank");
        }
        this.name = name.trim();
        this.score = score;
        this.gold = gold;
    }

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
            throw new NegativeNumberException("Gold cannot be less than 0.");
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
}