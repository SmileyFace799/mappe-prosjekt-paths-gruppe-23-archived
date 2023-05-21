package no.ntnu.idata2001.g23.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import no.ntnu.idata2001.g23.middleman.GameUpdateListener;
import no.ntnu.idata2001.g23.middleman.GameplayManager;
import no.ntnu.idata2001.g23.middleman.events.AllGoalsFulfilledEvent;
import no.ntnu.idata2001.g23.middleman.events.ChangePassageEvent;
import no.ntnu.idata2001.g23.middleman.events.EnemyAttackEvent;
import no.ntnu.idata2001.g23.middleman.events.EnemyDeathEvent;
import no.ntnu.idata2001.g23.middleman.events.GameUpdateEvent;
import no.ntnu.idata2001.g23.middleman.events.NewGameEvent;
import no.ntnu.idata2001.g23.middleman.events.PlayerAttackEvent;
import no.ntnu.idata2001.g23.middleman.events.PlayerDeathEvent;
import no.ntnu.idata2001.g23.middleman.events.UseItemEvent;
import no.ntnu.idata2001.g23.model.entities.Entity;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.entities.enemies.Enemy;
import no.ntnu.idata2001.g23.model.fileparsing.CorruptFileException;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.UsableItem;
import no.ntnu.idata2001.g23.model.items.Weapon;
import no.ntnu.idata2001.g23.model.misc.Inventory;
import no.ntnu.idata2001.g23.model.story.Passage;
import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.misc.GlobalCss;
import no.ntnu.idata2001.g23.view.screens.GameOverScreen;
import no.ntnu.idata2001.g23.view.screens.GameplayScreen;
import no.ntnu.idata2001.g23.view.screens.VictoryScreen;
import no.ntnu.idata2001.g23.view.textures.ImageLoader;

/**
 * Controller for the gameplay screen, where gameplay happens.
 */
public class GameplayController extends GenericController implements GameUpdateListener {
    private final GameplayManager gameplayManager;
    private final GameplayScreen screen;

    private Map<String, Image> sprites;

    /**
     * Controller for the gameplay screen.
     *
     * @param screen      The screen this is a controller for
     * @param application THe main application
     */
    public GameplayController(GameplayScreen screen, DungeonApp application) {
        super(application);
        this.screen = screen;
        this.gameplayManager = GameplayManager.getInstance();
        GameplayManager.getInstance().addUpdateListener(this);
    }

    private void addModal(Node node) {
        Pane overlay = new FlowPane();
        overlay.getStyleClass().add(GameplayScreen.Css.OVERLAY);
        overlay.getChildren().add(node);
        screen.getRootPane().getChildren().add(overlay);
    }

    /**
     * Removes the top-level modal content.
     */
    public void removeTopModal() {
        int size = screen.getRootPane().getChildren().size();
        if (size == 0 || screen.getRootPane().getChildren().get(size - 1)
                .equals(screen.getContentPane())) {
            throw new IllegalStateException("This screen doesn't have any active modals");
        }
        screen.getRootPane().getChildren().remove(size - 1);
    }

    /**
     * Removes all modal content.
     */
    public void removeAllModal() {
        screen.getRootPane().getChildren().remove(
                screen.getRootPane().getChildren().indexOf(screen.getContentPane()) + 1,
                screen.getRootPane().getChildren().size()
        );
    }

    /**
     * Shows the game's pause menu as a modal window.
     */
    public void showPauseModal() {
        addModal(screen.getPauseModal());
    }

    /**
     * Prompts the player to confirm if they really wanna restart.
     */
    public void showConfirmRestartModal() {
        addModal(screen.getConfirmRestartModal());
    }

    /**
     * Shows a modal window with enemy stats & interaction options.
     *
     * @param enemy The enemy to show a modal window for
     */
    public void showEnemyModal(Enemy enemy) {
        VBox enemyModal = new VBox();
        enemyModal.getStyleClass().add(GameplayScreen.Css.PROMPT);

        Label enemyHeader = new Label(enemy.getName());
        enemyHeader.getStyleClass().add(GlobalCss.HEADER);
        enemyModal.getChildren().add(enemyHeader);

        enemyModal.getChildren().add(ImageLoader.getImageView(sprites.get(enemy.getName()),
                0, 800, true));

        enemyModal.getChildren().add(new Label(enemy.getDetails()));

        Button attackButton = new Button("Attack");
        attackButton.setOnAction(ae -> {
            GameplayManager.getInstance().attack(enemy);
            removeTopModal();
        });
        enemyModal.getChildren().add(attackButton);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(ae -> removeTopModal());
        enemyModal.getChildren().add(closeButton);

        addModal(enemyModal);
    }

    /**
     * Shows the main action prompt with all the player's actions.
     */
    public void showActionPrompt() {
        screen.getContentPane().setLeft(screen.getActionPrompt());
    }

    /**
     * Sets the player's move options on the left side of the screen.
     */
    public void showMovePrompt() {
        screen.getContentPane().setLeft(screen.getMovePrompt());
    }

    /**
     * Shows the player's inventory on the left side of the screen.
     */
    public void showInventoryPrompt() {
        screen.getContentPane().setLeft(screen.getInventoryPrompt());
    }

    public void showViewItemsPrompt() {
        screen.getViewItemsView().getSelectionModel().clearSelection();
        screen.getContentPane().setLeft(screen.getViewItemsPrompt());
    }

    public void showUseItemPrompt() {
        screen.getUseItemView().getSelectionModel().clearSelection();
        screen.getContentPane().setLeft(screen.getUseItemPrompt());
    }

    public void showEquipWeaponPrompt() {
        screen.getEquipWeaponView().getSelectionModel().clearSelection();
        screen.getContentPane().setLeft(screen.getEquipWeaponPrompt());
    }

    public void showGoalsPrompt() {
        screen.getContentPane().setLeft(screen.getGoalsPrompt());
    }

    /**
     * Restarts the game.
     */
    public void restartGame() {
        try {
            GameplayManager.getInstance().startGame();
        } catch (CorruptFileException cfe) {
            throw new IllegalStateException("Game could not restart (This should never happen)");
        }
        removeAllModal();
    }

    /**
     * Uses the selected item in the use item view.
     */
    public void useSelectedItem() {
        UsableItem item = screen.getUseItemView().getSelectionModel().getSelectedItem();
        if (item != null) {
            gameplayManager.useItem(item);
        }
    }

    /**
     * Equips the selected weapon in the equip weapon view.
     */
    public void equipSelectedWeapon() {
        Weapon selectedWeapon = screen.getEquipWeaponView().getSelectionModel().getSelectedItem();
        if (selectedWeapon != null) {
            gameplayManager.equipWeapon(selectedWeapon);
        }
        screen.getEquipWeaponView().getSelectionModel().clearSelection();
    }

    @Override
    public void onUpdate(GameUpdateEvent event) {
        if (event instanceof NewGameEvent newGameEvent) {
            Map<String, String> spritePaths = newGameEvent.spritePaths();
            if (spritePaths != null) {
                sprites = spritePaths
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> ImageLoader.getImage(entry.getValue())
                        ));
            } else {
                sprites = new HashMap<>();
            }
            Passage startPassage = newGameEvent.startPassage();
            updateCurrentPassage(startPassage);
            updateEnemies(startPassage.getEnemies());
            Player player = newGameEvent.game().getPlayer();
            updatePlayerStats(player);
            screen.getHistoryContent().getChildren().clear();
        } else if (event instanceof ChangePassageEvent changePassageEvent) {
            Passage currentPassage = changePassageEvent.newPassage();
            updateCurrentPassage(currentPassage);
            updateEnemies(currentPassage.getEnemies());
        } else if (event instanceof UseItemEvent useItemEvent) {
            Entity entity = useItemEvent.entity();
            if (entity instanceof Player player) {
                updatePlayerStats(player);
            }
        } else if (event instanceof EnemyAttackEvent enemyAttackEvent) {
            List<Entity> targets = enemyAttackEvent.targets();
            targets.stream()
                    .filter(Player.class::isInstance)
                    .findFirst()
                    .ifPresent(playerEntity -> updatePlayerStats((Player) playerEntity));
            updateEnemies(enemyAttackEvent.remainingEnemies());
        } else if (event instanceof PlayerAttackEvent playerAttackEvent) {
            updateEnemies(playerAttackEvent.remainingEnemies());
        } else if (event instanceof EnemyDeathEvent enemyDeathEvent
                && (enemyDeathEvent.killer() instanceof Player player)) {
            updatePlayerStats(player);
        } else if (event instanceof PlayerDeathEvent) {
            changeScreen(GameOverScreen.class);
        } else if (event instanceof AllGoalsFulfilledEvent) {
            changeScreen(VictoryScreen.class);
        }

        logEvent(event);
    }

    /**
     * Logs an event in the history prompt.
     *
     * @param event The event to log
     */
    private void logEvent(GameUpdateEvent event) {
        String logMessage = event.getDescriptiveText();
        if (logMessage != null && !logMessage.isBlank()) {
            VBox historyContent = screen.getHistoryContent();
            if (!historyContent.getChildren().isEmpty()) {
                //Goes up to 16 messages, accounting for separators
                if (historyContent.getChildren().size() >= 31) {
                    //Removes event text, and the separator
                    historyContent.getChildren().remove(0, 2);
                }
                historyContent.getChildren().add(new Separator());
            }
            historyContent.getChildren().add(new Label(logMessage));
        }
    }

    /**
     * Updates the passage info shown on screen.
     *
     * @param newPassage The new passage with the updated text
     */
    private void updateCurrentPassage(Passage newPassage) {
        screen.getPassageTitle().setText(newPassage.getTitle());
        screen.getPassageText().setText(newPassage.getContent());

        VBox moveOptions = screen.getMoveOptions();
        moveOptions.getChildren().clear();
        newPassage.getLinks().forEach(link -> {
            Button linkButton = new Button(link.getText());
            linkButton.setOnAction(ae -> {
                gameplayManager.movePassage(link);
                showActionPrompt();
            });
            moveOptions.getChildren().add(linkButton);
        });
    }

    /**
     * Updates any inventory list views.
     *
     * @param inventory The inventory of items to show
     */
    private void updateInventoryLists(Inventory inventory) {
        ListView<Item> viewItemsView = screen.getViewItemsView();
        ListView<UsableItem> useItemView = screen.getUseItemView();
        ListView<Weapon> equipWeaponView = screen.getEquipWeaponView();
        viewItemsView.getItems().clear();
        useItemView.getItems().clear();
        equipWeaponView.getItems().clear();

        inventory.getContents().forEach(item -> {
            viewItemsView.getItems().add(item);
            if (item instanceof UsableItem usableItem) {
                useItemView.getItems().add(usableItem);
            } else if (item instanceof Weapon weapon) {
                equipWeaponView.getItems().add(weapon);
            }
        });
    }

    /**
     * Update the player stats shown on screen.
     *
     * @param player The player with the updated stats to show
     */
    private void updatePlayerStats(Player player) {
        screen.getNameLabel().setText(player.getName());
        screen.getHpLabel().setText(String.format(
                "%s/%s", player.getHealth(), player.getMaxHealth()));
        screen.getGoldLabel().setText(Integer.toString(player.getGold()));
        screen.getScoreLabel().setText(Integer.toString(player.getScore()));
        updateInventoryLists(player.getInventory());
        updateGoals();
    }

    /**
     * Updates the enemies showed on the screen & all their stats.
     *
     * @param enemies The enemies to be showed on the screen
     */
    private void updateEnemies(List<Enemy> enemies) {
        screen.getEnemyContent().getChildren().clear();

        enemies.forEach(enemy -> {
            Button enemyButton = new Button();
            enemyButton.getStyleClass().add(GameplayScreen.Css.ENEMY_BUTTON);
            enemyButton.setOnAction(ae -> showEnemyModal(enemy));
            screen.getEnemyContent().getChildren().add(enemyButton);

            VBox enemyBox = new VBox();
            enemyBox.setStyle("-fx-alignment: center");
            //The group makes the button not super tall
            enemyButton.setGraphic(new Group(enemyBox));

            enemyBox.getChildren().add(ImageLoader.getImageView(
                    sprites.get(enemy.getName()),
                    0, 400, true
            ));

            enemyBox.getChildren().add(new Label(enemy.getName()));

            enemyBox.getChildren().add(new Label(String.format(
                    "HP: %s/%s", enemy.getHealth(), enemy.getMaxHealth()
            )));
        });
    }

    /**
     * Updates the player's list of goals.
     */
    private void updateGoals() {
        VBox goalsBox = screen.getGoalsBox();
        goalsBox.getChildren().clear();
        GameplayManager.getInstance().checkGoals().forEach((goal, isFulfilled) -> {
            Label goalText = new Label(goal.getDescriptiveText());
            if (Boolean.TRUE.equals(isFulfilled)) {
                goalText.setStyle("-fx-text-fill: lime;");
            }
            goalsBox.getChildren().add(goalText);
        });
    }
}
