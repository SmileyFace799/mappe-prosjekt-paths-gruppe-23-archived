package no.ntnu.idata2001.g23.intermediary;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import no.ntnu.idata2001.g23.intermediary.events.AllGoalsFulfilledEvent;
import no.ntnu.idata2001.g23.intermediary.events.ChangePassageEvent;
import no.ntnu.idata2001.g23.intermediary.events.ChangePassageFailedEvent;
import no.ntnu.idata2001.g23.intermediary.events.EnemyAttackEvent;
import no.ntnu.idata2001.g23.intermediary.events.EnemyDeathEvent;
import no.ntnu.idata2001.g23.intermediary.events.EquipWeaponEvent;
import no.ntnu.idata2001.g23.intermediary.events.GameUpdateEvent;
import no.ntnu.idata2001.g23.intermediary.events.NewGameEvent;
import no.ntnu.idata2001.g23.intermediary.events.PlayerAttackEvent;
import no.ntnu.idata2001.g23.intermediary.events.PlayerDeathEvent;
import no.ntnu.idata2001.g23.intermediary.events.UseItemEvent;
import no.ntnu.idata2001.g23.model.Game;
import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.actions.HealthAction;
import no.ntnu.idata2001.g23.model.entities.Entity;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.entities.enemies.Enemy;
import no.ntnu.idata2001.g23.model.fileparsing.CorruptFileException;
import no.ntnu.idata2001.g23.model.fileparsing.GameFileCollection;
import no.ntnu.idata2001.g23.model.fileparsing.SpritePathsLoader;
import no.ntnu.idata2001.g23.model.goals.Goal;
import no.ntnu.idata2001.g23.model.items.UsableItem;
import no.ntnu.idata2001.g23.model.items.Weapon;
import no.ntnu.idata2001.g23.model.story.Link;
import no.ntnu.idata2001.g23.model.story.Passage;

/**
 * A middle manager between the view and the model that keeps track of game progress,
 * and contains methods for the controllers to interact with the game.
 * Also makes use of observer pattern to notify the view about any changes.
 */
public class GameplayManager {
    private static GameplayManager instance;
    private final Collection<GameUpdateListener> listeners;
    private GameFileCollection gameFiles;
    private String playerName;
    private String difficulty;

    private Game game;
    private Passage currentPassage;

    private GameplayManager() {
        listeners = new HashSet<>();
    }

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

    /**
     * Adds a listener that can listen for updates.
     *
     * @param listener The listener to add
     */
    public void addUpdateListener(GameUpdateListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies every listener about an event.
     *
     * @param event The event to notify all listeners of
     */
    private void notifyListeners(GameUpdateEvent event) {
        listeners.forEach(listener -> listener.onUpdate(event));
    }

    /**
     * Sets a game that can be played.
     *
     * @param gameFiles  The game's files
     * @param playerName The player's name
     * @param difficulty The game's difficulty
     * @see #startGame()
     */
    public void setGame(GameFileCollection gameFiles, String playerName, String difficulty) {
        this.gameFiles = gameFiles;
        this.playerName = playerName;
        this.difficulty = difficulty;
    }

    /**
     * Makes the set game and starts it from the beginning.
     *
     * @throws IllegalStateException If no game has been set
     * @throws CorruptFileException  If the game cannot be made due to one or more corrupt files
     * @see #setGame(GameFileCollection, String, String)
     */
    public void startGame() throws CorruptFileException {
        if (gameFiles == null
                || playerName == null || playerName.isBlank()
                || difficulty == null || difficulty.isBlank()
        ) {
            throw new IllegalStateException("No game has been set");
        }
        this.game = gameFiles.makeNewGame(playerName, difficulty);
        this.currentPassage = game.begin();
        Path spritesFilePath = gameFiles.getPath(".sprites");
        Map<String, String> spritePaths = spritesFilePath != null
                ? new SpritePathsLoader(gameFiles.getGamePath())
                .loadSpritePaths(spritesFilePath)
                : null;
        notifyListeners(new NewGameEvent(game, currentPassage, spritePaths));
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

        Passage oldPassage = currentPassage;
        Passage newPassage = game.go(link);
        Optional<Enemy> blockingEnemy = oldPassage
                .getEnemies()
                .stream()
                .filter(enemy -> enemy.getEscapeChance() < Math.random())
                .findFirst();

        if (blockingEnemy.isEmpty()) {
            Player player = game.getPlayer();
            List<Action> actions = link.getActions();
            actions.forEach(action -> action.execute(player));
            this.currentPassage = game.go(link);
            ChangePassageEvent changePassageEvent =
                    new ChangePassageEvent(oldPassage, currentPassage, actions);
            notifyListeners(changePassageEvent);
            if (player.getHealth() <= 0) {
                notifyListeners(new PlayerDeathEvent(changePassageEvent));
            }
        } else {
            notifyListeners(new ChangePassageFailedEvent(
                    oldPassage, newPassage, blockingEnemy.get()));
            enemiesAttack();
        }
    }

    /**
     * Uses an item in the player's inventory.
     *
     * @param item The item to use
     */
    public void useItem(UsableItem item) {
        Player player = game.getPlayer();
        player.useItem(item);
        UseItemEvent useItemEvent = new UseItemEvent(item, player);
        notifyListeners(useItemEvent);
        if (player.getHealth() <= 0) {
            notifyListeners(new PlayerDeathEvent(useItemEvent));
        }
    }

    /**
     * Equips the player wit a specified weapons.
     *
     * @param weapon The weapon to equip
     */
    public void equipWeapon(Weapon weapon) {
        game.getPlayer().equipWeapon(weapon);
        notifyListeners(new EquipWeaponEvent(game.getPlayer().getEquippedWeapon()));
    }

    /**
     * Drops the loot of any enemy who has died, and gives it to the enemy's killer.
     *
     * @param deadEnemy The enemy that died
     * @param killedBy  The entity that killed the enemy who died
     */
    private void enemyDeath(Enemy deadEnemy, Entity killedBy) {
        List<Action> droppedLoot = deadEnemy.dropLoot();
        if (killedBy != null) {
            droppedLoot.forEach(action -> action.execute(killedBy));
        }
        notifyListeners(new EnemyDeathEvent(deadEnemy, killedBy, droppedLoot));
    }

    /**
     * Makes every enemy attack.
     */
    private void enemiesAttack() {
        List<Enemy> enemies = currentPassage.getEnemies();
        for (Enemy enemy : new ArrayList<>(enemies)) {
            if (enemy.getHealth() > 0) {
                List<Entity> possibleTargets = new ArrayList<>();
                possibleTargets.add(game.getPlayer());
                possibleTargets.addAll(enemies);

                Map<Action, List<Entity>> actionMap = enemy.act(possibleTargets);
                for (Map.Entry<Action, List<Entity>> actionEntry : actionMap.entrySet()) {
                    Action enemyAttack = actionEntry.getKey();
                    List<Entity> targets = actionEntry.getValue();
                    targets.forEach(enemyAttack::execute);
                    EnemyAttackEvent enemyAttackEvent = new EnemyAttackEvent(
                            enemy, enemyAttack, targets,
                            enemies.stream().filter(e -> e.getHealth() > 0).toList());
                    notifyListeners(enemyAttackEvent);
                    targets.stream().filter(entity -> entity.getHealth() <= 0).forEach(dead -> {
                        if (dead instanceof Enemy e) {
                            enemyDeath(e, enemy);
                        } else if (dead instanceof Player) {
                            notifyListeners(new PlayerDeathEvent(enemyAttackEvent));
                        }
                    });
                }
            }
        }
    }

    /**
     * Executes an attack phase, and kills any entity that would die during it.
     *
     * @param target The target for the player's attack
     */
    public void attack(Enemy target) {
        Player player = game.getPlayer();
        List<Enemy> enemies = currentPassage.getEnemies();
        Action attack = new HealthAction(-player.getEquippedWeapon().getDamage());
        attack.execute(target);
        if (target.getHealth() <= 0) {
            enemyDeath(target, player);
            enemies.remove(target);
        }
        notifyListeners(new PlayerAttackEvent(attack, target, enemies));

        //After the player attacks, every enemy goes for an attack
        enemiesAttack();
    }

    /**
     * Checks all the game's goals to see if the player fulfills them or not.
     *
     * @return A map of every goal, and if it's fulfilled or not
     */
    public Map<Goal, Boolean> checkGoals() {
        Player player = game.getPlayer();
        Map<Goal, Boolean> goalMap = game
                .getGoals()
                .stream()
                .collect(Collectors.toMap(
                        goal -> goal,
                        goal -> goal.isFulfilled(player)
                ));
        if (goalMap.values().stream().allMatch(isFulfilled -> isFulfilled)) {
            notifyListeners(new AllGoalsFulfilledEvent(game.getGoals()));
        }
        return goalMap;
    }
}
