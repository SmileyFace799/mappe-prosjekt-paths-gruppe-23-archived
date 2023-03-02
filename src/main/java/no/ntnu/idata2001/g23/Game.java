package no.ntnu.idata2001.g23;

import java.util.List;
import no.ntnu.idata2001.g23.entities.Player;
import no.ntnu.idata2001.g23.goals.Goal;
import no.ntnu.idata2001.g23.story.Link;
import no.ntnu.idata2001.g23.story.Passage;
import no.ntnu.idata2001.g23.story.Story;

/**
 * A game that can be player, with everything needed to play it.
 */
public class Game {
    Player player;
    Story story;
    List<Goal> goals;

    /**
     * Makes a new game.
     *
     * @param player The game's player.
     * @param story The game's story.
     * @param goals The goals of the game.
     */
    public Game(Player player, Story story, List<Goal> goals) {
        this.player = player;
        this.story = story;
        this.goals = goals;
    }

    public Player getPlayer() {
        return player;
    }

    public Story getStory() {
        return story;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public Passage begin() {
        return story.getOpeningPassage();
    }

    public Passage go(Link link) {
        return story.getPassage(link);
    }
}
