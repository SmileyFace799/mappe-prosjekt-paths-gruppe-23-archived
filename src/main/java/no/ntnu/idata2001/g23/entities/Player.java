package no.ntnu.idata2001.g23.entities;

import java.util.ArrayList;
import java.util.List;
import no.ntnu.idata2001.g23.exceptions.BlankStringException;
import no.ntnu.idata2001.g23.exceptions.NegativeNumberException;
import no.ntnu.idata2001.g23.items.Item;

/**
 * Represents a player with different attributes which can be affected in a story.
 */
public class Player {
    private final String name;
    private final int health;
    private final int score;
    private final int gold;
    private final ArrayList<Item> inventory = new ArrayList<>();

    /**
     * Creates a player.
     *
     * @param name   The name of the player
     * @param health How much health the player has
     * @param score  The player's score
     * @param gold   How much gold the player has
     */
    public Player(String name, int health, int score, int gold) {
        if (name == null || name.isBlank()) {
            throw new BlankStringException("Name cannot be null or blank");
        }
        if (health > 0) {
            throw new NegativeNumberException("Health cannot be less than 0");
        }
        this.name = name.trim();
        this.health = health;
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
     * Increases the player's health.
     *
     * @param health increases players health
     */
    public void addHealth(int health) {
        //working on it
    }

    /**
     * Shows how much health the player has.
     *
     * @return players health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Adds points to the players score.
     *
     * @param points increases players score.
     */
    public void addScore(int points) {
        //working on it
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
    public void addGold(int gold) {
        //working on it
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
     * Adds an item to the inventory.
     *
     * @param item Adds item to the inventory
     */
    public void addToInventory(String item) {
        //working on it
    }

    /**
     * Returns list of items in the players inventory.
     *
     * @return list of items in inventory
     */
    public List<Item> getInventory() {
        return inventory;
    }
}
