package no.ntnu.idata2001.g23.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import no.ntnu.idata2001.g23.middleman.GameUpdateListener;
import no.ntnu.idata2001.g23.middleman.GameplayManager;
import no.ntnu.idata2001.g23.middleman.events.ChangePassageEvent;
import no.ntnu.idata2001.g23.middleman.events.EnemyAttackEvent;
import no.ntnu.idata2001.g23.middleman.events.GameUpdateEvent;
import no.ntnu.idata2001.g23.middleman.events.InventoryUpdateEvent;
import no.ntnu.idata2001.g23.middleman.events.NewGameEvent;
import no.ntnu.idata2001.g23.middleman.events.PlayerAttackEvent;
import no.ntnu.idata2001.g23.model.entities.Entity;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.entities.enemies.Enemy;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.UsableItem;
import no.ntnu.idata2001.g23.model.items.Weapon;
import no.ntnu.idata2001.g23.model.misc.Inventory;
import no.ntnu.idata2001.g23.model.story.Passage;
import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.misc.GlobalCss;
import no.ntnu.idata2001.g23.view.screens.GameplayScreen;
import no.ntnu.idata2001.g23.view.textures.ImageLoader;

/**
 * Controller for the gameplay screen, where gameplay happens.
 */
public class GameplayController extends GenericController implements GameUpdateListener {
    private final GameplayManager gameplayManager;
    private final GameplayScreen screen;
    private final List<String> actionHistory;

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
        this.actionHistory = new ArrayList<>();
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

    public void clearActionHistory() {
        actionHistory.clear();
        screen.getHistoryContent().getChildren().clear();
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
            updateInventoryLists(player.getInventory());
            updatePlayerStats(player);
        } else if (event instanceof ChangePassageEvent changePassageEvent) {
            Passage currentPassage = changePassageEvent.currentPassage();
            updateCurrentPassage(currentPassage);
            updateEnemies(currentPassage.getEnemies());
            logAction("Moved to " + currentPassage.getTitle());
        } else if (event instanceof InventoryUpdateEvent inventoryUpdateEvent) {
            updateInventoryLists(inventoryUpdateEvent.inventory());
        } else if (event instanceof EnemyAttackEvent enemyAttackEvent) {
            List<Entity> targets = enemyAttackEvent.targets();
            targets.stream()
                    .filter(Player.class::isInstance)
                    .findFirst()
                    .ifPresent(playerEntity -> updatePlayerStats((Player) playerEntity));
            updateEnemies(enemyAttackEvent.remainingEnemies());
        } else if (event instanceof PlayerAttackEvent playerAttackEvent) {
            updateEnemies(playerAttackEvent.remainingEnemies());
        }
    }

    /**
     * Logs an action in the history pane.
     *
     * @param logMessage THe message for the action to log
     */
    public void logAction(String logMessage) {
        if (actionHistory.size() >= 15) {
            actionHistory.remove(0);
        }
        actionHistory.add(logMessage);
        VBox historyContent = screen.getHistoryContent();
        historyContent.getChildren().clear();
        actionHistory.forEach(message -> historyContent.getChildren().add(new Label(message)));
    }

    /**
     * Updates the passage info shown on screen.
     *
     * @param newPassage The new passage with the updated text
     */
    public void updateCurrentPassage(Passage newPassage) {
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
    public void updateInventoryLists(Inventory inventory) {
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
    public void updatePlayerStats(Player player) {
        screen.getNameLabel().setText(player.getName());
        screen.getHpLabel().setText(String.format(
                "%s/%s", player.getHealth(), player.getMaxHealth()));
        screen.getGoldLabel().setText(Integer.toString(player.getGold()));
        screen.getScoreLabel().setText(Integer.toString(player.getScore()));
    }

    /**
     * Updates the enemies showed on the screen & all their stats.
     *
     * @param enemies The enemies to be showed on the screen
     */
    public void updateEnemies(List<Enemy> enemies) {
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
}
