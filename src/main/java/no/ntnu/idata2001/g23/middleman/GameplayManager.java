package no.ntnu.idata2001.g23.middleman;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import no.ntnu.idata2001.g23.middleman.events.ChangePassageEvent;
import no.ntnu.idata2001.g23.middleman.events.EnemyAttackEvent;
import no.ntnu.idata2001.g23.middleman.events.EnemyDeathEvent;
import no.ntnu.idata2001.g23.middleman.events.EquipWeaponEvent;
import no.ntnu.idata2001.g23.middleman.events.GameUpdateEvent;
import no.ntnu.idata2001.g23.middleman.events.NewGameEvent;
import no.ntnu.idata2001.g23.middleman.events.PlayerAttackEvent;
import no.ntnu.idata2001.g23.middleman.events.PlayerDeathEvent;
import no.ntnu.idata2001.g23.middleman.events.UseItemEvent;
import no.ntnu.idata2001.g23.model.Game;
import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.actions.HealthAction;
import no.ntnu.idata2001.g23.model.entities.Entity;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.entities.enemies.Enemy;
import no.ntnu.idata2001.g23.model.fileparsing.CorruptFileException;
import no.ntnu.idata2001.g23.model.fileparsing.EnemyLoader;
import no.ntnu.idata2001.g23.model.fileparsing.GameFileCollection;
import no.ntnu.idata2001.g23.model.fileparsing.GoalLoader;
import no.ntnu.idata2001.g23.model.fileparsing.ItemLoader;
import no.ntnu.idata2001.g23.model.fileparsing.PlayerLoader;
import no.ntnu.idata2001.g23.model.fileparsing.SpritePathsLoader;
import no.ntnu.idata2001.g23.model.fileparsing.StoryLoader;
import no.ntnu.idata2001.g23.model.goals.Goal;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.UsableItem;
import no.ntnu.idata2001.g23.model.items.Weapon;
import no.ntnu.idata2001.g23.model.misc.Provider;
import no.ntnu.idata2001.g23.model.story.Link;
import no.ntnu.idata2001.g23.model.story.Passage;
import no.ntnu.idata2001.g23.model.story.Story;

/**
 * A middle manager between the view & the model that keeps track of game progress,
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

    public void addUpdateListener(GameUpdateListener listener) {
        listeners.add(listener);
    }

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
     * Makes a new {@link Game} object from the set game files, player name & difficulty.
     *
     * @return The newly made game
     * @throws CorruptFileException If the game cannot be made due to one or more corrupt files
     */
    private Game makeNewGame() throws CorruptFileException {
        //Parse items, create itemProvider
        Path itemsPath = gameFiles.getPath(".items");
        Provider<Item> itemProvider = itemsPath != null
                ? ItemLoader.loadItems(itemsPath)
                : null;

        //Parse enemies, create enemyProvider, using itemProvider
        Path enemiesPath = gameFiles.getPath(".enemies");
        Provider<Enemy> enemyProvider = enemiesPath != null
                ? new EnemyLoader(itemProvider).loadEnemies(enemiesPath)
                : null;

        //parse & create story, using itemProvider
        Path storyPath = gameFiles.getPathRequired(".paths",
                CorruptFileException.Type.INFO_MISSING_STORY);
        Story story = new StoryLoader(itemProvider).loadStory(storyPath);

        //parse & create player, using itemProvider, name & difficulty
        Path playerPath = gameFiles.getPathRequired(".player",
                CorruptFileException.Type.INFO_MISSING_PLAYER);
        Player player = new PlayerLoader(itemProvider, playerName,
                difficulty).loadPlayer(playerPath);

        //parse & create goals, using itemProvider & difficulty
        Path goalsPath = gameFiles.getPathRequired(".goals",
                CorruptFileException.Type.INFO_MISSING_GOALS);
        List<Goal> goals = new GoalLoader(itemProvider, difficulty).loadGoals(goalsPath);

        //Create game, using story & goals
        Game loadedGame = new Game(player, story, goals);

        //TODO: Remove this, make enemies part of .paths
        for (String enemyName : new String[]{"Test Enemy", "Test Enemy"}) {
            loadedGame.getStory().getPassages().forEach(passage ->
                    passage.getEnemies().add(enemyProvider.provide(enemyName)));
        }
        return loadedGame;
    }

    /**
     * Makes the set game & starts it from the beginning.
     *
     * @throws IllegalStateException If no game has been set
     * @throws CorruptFileException If the game cannot be made due to one or more corrupt files
     * @see #setGame(GameFileCollection, String, String)
     */
    public void startGame() throws CorruptFileException {
        if (gameFiles == null
                || playerName == null || playerName.isBlank()
                || difficulty == null || difficulty.isBlank()
        ) {
            throw new IllegalStateException("No game has been set");
        }
        this.game = makeNewGame();
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
        this.currentPassage = game.go(link);
        notifyListeners(new ChangePassageEvent(oldPassage, currentPassage));
    }

    /**
     * Uses an item in the player's inventory.
     *
     * @param item The item to use
     */
    public void useItem(UsableItem item) {
        Player player = game.getPlayer();
        player.useItem(item);
        notifyListeners(new UseItemEvent(item, player));
    }

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

    private void executeEnemyAttackAction(Enemy attacker, Action action, List<Entity> targets) {
        targets.forEach(entity -> {
            if (entity.getHealth() > 0) {
                action.execute(entity);
                if (entity.getHealth() <= 0) {
                    if (entity instanceof Enemy e) {
                        enemyDeath(e, attacker);
                    } else if (entity instanceof Player player) {
                        notifyListeners(new PlayerDeathEvent(player, attacker));
                    }
                }
            }
        });
    }

    /**
     * Executes an attack phase, and kills any entity that would die during it.
     *
     * @param target The target for the player's attack
     */
    public void attack(Enemy target) {
        Player player = game.getPlayer();
        List<Enemy> enemies = currentPassage.getEnemies();
        Action attack = new HealthAction(-player.getEquippedWeapon().getBaseDamage());
        attack.execute(target);
        if (target.getHealth() <= 0) {
            enemyDeath(target, player);
            enemies.remove(target);
        }
        notifyListeners(new PlayerAttackEvent(
                player, attack, target, enemies));

        //After the player attacks, every enemy goes for an attack
        for (Enemy enemy : new ArrayList<>(enemies)) {
            if (enemy.getHealth() > 0) {
                List<Entity> possibleTargets = new ArrayList<>();
                possibleTargets.add(game.getPlayer());
                possibleTargets.addAll(enemies);

                Map<Action, List<Entity>> actionMap = enemy.act(possibleTargets);
                for (Map.Entry<Action, List<Entity>> actionEntry : actionMap.entrySet()) {
                    Action enemyAttack = actionEntry.getKey();
                    List<Entity> targets = actionEntry.getValue();
                    executeEnemyAttackAction(enemy, enemyAttack, targets);
                    enemies.removeIf(e -> e.getHealth() == 0);
                    notifyListeners(new EnemyAttackEvent(
                            enemy, enemyAttack, targets, enemies));
                }
            }
        }
    }
}
