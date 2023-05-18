package no.ntnu.idata2001.g23.controllers;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import no.ntnu.idata2001.g23.middleman.GameplayManager;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.UsableItem;
import no.ntnu.idata2001.g23.model.misc.Inventory;
import no.ntnu.idata2001.g23.model.story.Link;
import no.ntnu.idata2001.g23.model.story.Passage;
import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.screens.GameplayScreen;

/**
 * Controller for the gameplay screen, where gameplay happens.
 */
public class GameplayController extends GenericController {
    private final GameplayManager gameplayManager;
    private final GameplayScreen screen;
    private final List<String> actionHistory;

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
     * Shows the main action prompt with all of the player's actions.
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

    /**
     * Uses the selected item if it is usable.
     */
    public void useSelectedItem() {
        if (screen.getInventoryContent().getSelectionModel().getSelectedItem()
                instanceof UsableItem usableItem) {
            gameplayManager.useItem(usableItem);
            showActionPrompt();
        }
    }

    public void clearActionHistory() {
        actionHistory.clear();
        screen.getHistoryContent().getChildren().clear();
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
        for (Link link : newPassage.getLinks()) {
            Button linkButton = new Button(link.getText());
            linkButton.setOnAction(ae -> {
                gameplayManager.movePassage(link);
                showActionPrompt();
            });
            moveOptions.getChildren().add(linkButton);
        }
    }

    /**
     * Update the inventory list view.
     *
     * @param inventory The inventory of items to show
     */
    public void updateInventoryList(Inventory inventory) {
        ListView<Item> inventoryContent = screen.getInventoryContent();
        inventoryContent.getItems().clear();
        inventoryContent.getItems().addAll(inventory.getContents());
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
