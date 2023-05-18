package no.ntnu.idata2001.g23.middleman;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import no.ntnu.idata2001.g23.middleman.events.AttackEvent;
import no.ntnu.idata2001.g23.middleman.events.ChangePassageEvent;
import no.ntnu.idata2001.g23.middleman.events.GameUpdateEvent;
import no.ntnu.idata2001.g23.middleman.events.InventoryUpdateEvent;
import no.ntnu.idata2001.g23.middleman.events.NewGameEvent;
import no.ntnu.idata2001.g23.model.Game;
import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.entities.Entity;
import no.ntnu.idata2001.g23.model.entities.enemies.Enemy;
import no.ntnu.idata2001.g23.model.items.UsableItem;
import no.ntnu.idata2001.g23.model.story.Link;
import no.ntnu.idata2001.g23.model.story.Passage;

/**
 * A middle manager between the view & the model that keeps track of game progress,
 * and contains methods for the controllers to interact with the game.
 * Also makes use of observer pattern to notify the view about any changes.
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
                throw new IllegalArgumentException("The story does not contain the start passage");
            }
            currentPassage = startPassage;
        } else {
            this.currentPassage = game.begin();
        }
        notifyListeners(new NewGameEvent(game, currentPassage));
    }

    /**
     * Updates the current passage to the passage associated with the provided link.
     *
     * @param link The link associated with the passage to move to
     * @throws IllegalArgumentException If {@code link} is {@code null}
     */
    public void movePassage(Link link) {
        if (link == null) {
            throw new IllegalArgumentException("\"link\" cannot be null");
        }
        this.currentPassage = game.go(link);
        notifyListeners(new ChangePassageEvent(currentPassage));
    }

    /**
     * Uses an item in the player's inventory.
     *
     * @param item The item to use
     */
    public void useItem(UsableItem item) {
        game.getPlayer().useItem(item);
        notifyListeners(new InventoryUpdateEvent(game.getPlayer().getInventory()));
    }

    /**
     * Makes an enemy attack.
     *
     * @param attacker The enemy that's attacking
     * @param possibleTargets A collection of possible targets for the enemy to attack
     */
    public void attack(Enemy attacker, Collection<Entity> possibleTargets) {
        Map<Action, List<Entity>> actionMap = attacker.act(possibleTargets);
        for (Map.Entry<Action, List<Entity>> actionEntry : actionMap.entrySet()) {
            Action action = actionEntry.getKey();
            List<Entity> targets = actionEntry.getValue();
            targets.forEach(action::execute);
            notifyListeners(new AttackEvent(attacker, action, targets));
        }
    }
}
