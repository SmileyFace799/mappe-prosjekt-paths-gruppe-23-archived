package no.ntnu.idata2001.g23.model;

import java.util.List;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.goals.Goal;
import no.ntnu.idata2001.g23.model.itemhandling.ItemProvider;
import no.ntnu.idata2001.g23.model.story.Link;
import no.ntnu.idata2001.g23.model.story.Passage;
import no.ntnu.idata2001.g23.model.story.Story;

/**
 * Connects a player with a story, and contains method for the player to navigate the story.
 */
public class Game {
    private final Player player;
    private final Story story;
    private final List<Goal> goals;
    private final ItemProvider itemProvider;

    /**
     * Makes a new game.
     *
     * @param player The game's player
     * @param story The game's story
     * @param difficulty The game's difficulty
     * @param itemProvider The game's item provider
     */
    private Game(Player player, Story story, String difficulty, ItemProvider itemProvider) {
        this.player = player;
        this.story = story;
        this.goals = story.getGoals(difficulty);
        this.itemProvider = itemProvider;
    }

    public Player getPlayer() {
        return player;
    }

    public Story getStory() {
        return story;
    }

    public ItemProvider getItemProvider() {
        return itemProvider;
    }

    public Passage begin() {
        return story.getOpeningPassage();
    }

    public Passage go(Link link) {
        return story.getPassage(link);
    }

    /**
     * Builder pattern.
     */
    public static class GameBuilder {
        private final Player player;
        private final Story story;
        private final String difficulty;

        private ItemProvider itemProvider = null;

        /**
         * Makes a game builder with all required parameters.
         *
         * @param player The game's player
         * @param story The game's story
         * @param difficulty The game's difficulty
         */
        public GameBuilder(Player player, Story story, String difficulty) {
            this.player = player;
            this.story = story;
            this.difficulty = difficulty;
        }

        public GameBuilder setItemProvider(ItemProvider itemProvider) {
            this.itemProvider = itemProvider;
            return this;
        }

        public Game build() {
            return new Game(player, story, difficulty, itemProvider);
        }
    }
}
