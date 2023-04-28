package no.ntnu.idata2001.g23.model;

import java.util.List;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.goals.Goal;
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

    /**
     * Makes a new game.
     *
     * @param player The game's player.
     * @param story The game's story.
     * @param difficulty The game's difficulty.
     */
    public Game(Player player, Story story, String difficulty) {
        this.player = player;
        this.story = story;
        this.goals = story.getGoals(difficulty);
    }

    public Player getPlayer() {
        return player;
    }

    public Story getStory() {
        return story;
    }

    public Passage begin() {
        return story.getOpeningPassage();
    }

    public Passage go(Link link) {
        return story.getPassage(link);
    }
}
