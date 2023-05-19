package no.ntnu.idata2001.g23.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import no.ntnu.idata2001.g23.middleman.GameUpdateListener;
import no.ntnu.idata2001.g23.middleman.GameplayManager;
import no.ntnu.idata2001.g23.middleman.events.ChangePassageEvent;
import no.ntnu.idata2001.g23.middleman.events.GameUpdateEvent;
import no.ntnu.idata2001.g23.middleman.events.InventoryUpdateEvent;
import no.ntnu.idata2001.g23.middleman.events.NewGameEvent;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.UsableItem;
import no.ntnu.idata2001.g23.model.items.Weapon;
import no.ntnu.idata2001.g23.model.misc.Inventory;
import no.ntnu.idata2001.g23.model.story.Passage;
import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.screens.GameplayScreen;
import no.ntnu.idata2001.g23.view.textures.ImageLoader;

/**
 * Controller for the gameplay screen, where gameplay happens.
 */
public class GameplayController extends GenericController implements GameUpdateListener {
    private final GameplayManager gameplayManager;
    private final GameplayScreen screen;
    private final List<String> actionHistory;

    private Map<String, String> spritePaths;

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
            spritePaths = newGameEvent.spritePaths();
            if (spritePaths == null) {
                spritePaths = new HashMap<>();
            }
            updateCurrentPassage(newGameEvent.startPassage());
            updateInventoryLists(newGameEvent.game().getPlayer().getInventory());
            updatePlayerStats(newGameEvent.game().getPlayer());
        } else if (event instanceof ChangePassageEvent changePassageEvent) {
            Passage currentPassage = changePassageEvent.currentPassage();
            updateCurrentPassage(currentPassage);
            logAction("Moved to " + currentPassage.getTitle());
        } else if (event instanceof InventoryUpdateEvent inventoryUpdateEvent) {
            updateInventoryLists(inventoryUpdateEvent.inventory());
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

        newPassage.getEnemies().forEach(enemy -> {
            VBox enemyBox = new VBox();
            enemyBox.setStyle("-fx-alignment: center");
            screen.getEnemyContent().getChildren().add(enemyBox);

            enemyBox.getChildren().add(ImageLoader.getImageView(
                    ImageLoader.getImage(spritePaths.get(enemy
                            .getName().trim().toLowerCase().replace(" ", ""))),
                    0, 400, true
            ));

            enemyBox.getChildren().add(new Label(enemy.getName()));

            enemyBox.getChildren().add(new Label(String.format(
                    "HP: %s/%s", enemy.getHealth(), enemy.getMaxHealth()
            )));
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
        screen.getHpLabel().setText(player.getHealth() + "/" + player.getMaxHealth());
        screen.getGoldLabel().setText(Integer.toString(player.getGold()));
        screen.getScoreLabel().setText(Integer.toString(player.getScore()));
    }
}
