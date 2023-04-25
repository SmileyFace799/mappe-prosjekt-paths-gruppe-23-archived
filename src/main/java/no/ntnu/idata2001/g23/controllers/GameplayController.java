package no.ntnu.idata2001.g23.controllers;

import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import no.ntnu.idata2001.g23.middleman.GameplayManager;
import no.ntnu.idata2001.g23.model.story.Link;
import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.screens.GameplayScreen;

/**
 * Controller for the gameplay screen, where gameplay happens.
 */
public class GameplayController extends GenericController {
    private final GameplayManager gameplayManager;
    private final GameplayScreen screen;

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
    }

    private void addModal(Node node) {
        Pane overlay = new FlowPane();
        overlay.getStyleClass().add(GameplayScreen.Css.OVERLAY);
        overlay.getChildren().add(node);
        screen.getRoot().getChildren().add(overlay);
    }

    /**
     * Removes the top-level modal content.
     */
    public void removeTopModal() {
        int size = screen.getRoot().getChildren().size();
        if (size == 0 || screen.getRoot().getChildren().get(size - 1)
                .equals(screen.getContentPane())) {
            throw new IllegalStateException("This screen doesn't have any active modals");
        }
        screen.getRoot().getChildren().remove(size - 1);
    }

    /**
     * Removes all modal content.
     */
    public void removeAllModal() {
        screen.getRoot().getChildren().remove(
                screen.getRoot().getChildren().indexOf(screen.getContentPane()) + 1,
                screen.getRoot().getChildren().size()
        );
    }

    /**
     * Shows the game's menu at in the bottom prompt.
     */
    public void showPauseModal() {
        addModal(screen.getPauseModal());
    }

    /**
     * Sets the screen to show its default contents.
     */
    public void showMovePrompt() {
        screen.getContentPane().setLeft(screen.getMovePrompt());
    }

    /**
     * Shows yhe player's inventory on the left side of the screen.
     */
    public void showInventoryPrompt() {
        screen.getContentPane().setLeft(screen.getInventoryPrompt());
    }

    public void movePassage(Link link) {
        gameplayManager.movePassage(link);
    }
}
