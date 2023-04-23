package no.ntnu.idata2001.g23.middleman;

import java.util.Collection;
import java.util.HashSet;
import no.ntnu.idata2001.g23.exceptions.unchecked.ElementNotFoundException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NullValueException;
import no.ntnu.idata2001.g23.middleman.events.GameUpdateEvent;
import no.ntnu.idata2001.g23.model.Game;
import no.ntnu.idata2001.g23.model.story.Link;
import no.ntnu.idata2001.g23.model.story.Passage;

/**
 * A middle manager between the view & the model that keeps track of game progress,
 * and contains methods for the view & its controllers to progress & interact with the game.
 */
public class GameplayManager {
    private static GameplayManager instance;

    /**
     * Singleton.
     *
     * @return Singleton instance
     */
    public static GameplayManager getInstance() {
        if (instance == null) {
            instance = new GameplayManager();
        }
        return instance;
    }

    private final Collection<GameUpdateListener> listeners;
    private Game game;
    private Passage currentPassage;

    private GameplayManager() {
        listeners = new HashSet<>();
    }

    public void addUpdateListener(GameUpdateListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(GameUpdateEvent event) {
        listeners.forEach(listener -> listener.onUpdate(event));
    }

    public Game getGame() {
        return game;
    }

    public Passage getCurrentPassage() {
        return currentPassage;
    }

    /**
     * Starts a game from the beginning.
     *
     * @param game The game to start
     */
    public void startGame(Game game) {
        startGame(game, null);
    }

    /**
     * Starts a game from a specified passage.
     *
     * @param game The game to start
     * @param startPassage The starting passage. The story must contain this passage.
     *                     If this is {@code null}, the story will start from the beginning
     */
    public void startGame(Game game, Passage startPassage) {
        this.game = game;
        if (startPassage != null) {
            if (!game.getStory().getPassages().contains(startPassage)) {
                throw new ElementNotFoundException("The story does not contain the start passage");
            }
            currentPassage = startPassage;
        } else {
            this.currentPassage = game.begin();
        }
        //TODO: Make this work, or remove it
        notifyListeners(null);
    }

    /**
     * Updates the current passage to the passage associated with the provided link.
     *
     * @param link The link associated with the passage to move to
     * @throws NullValueException If {@code link} is {@code null}
     */
    public void movePassage(Link link) {
        if (link == null) {
            throw new NullValueException("\"link\" cannot be null");
        }
        this.currentPassage = game.go(link);
    }
}
