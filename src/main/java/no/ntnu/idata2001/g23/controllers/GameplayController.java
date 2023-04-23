package no.ntnu.idata2001.g23.controllers;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import no.ntnu.idata2001.g23.middleman.GameplayManager;
import no.ntnu.idata2001.g23.model.itemhandling.FullInventoryException;
import no.ntnu.idata2001.g23.model.itemhandling.Inventory;
import no.ntnu.idata2001.g23.model.itemhandling.ItemFactory;
import no.ntnu.idata2001.g23.model.story.Link;
import no.ntnu.idata2001.g23.model.story.Passage;
import no.ntnu.idata2001.g23.view.DebugScrollPane;
import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.screens.GameplayScreen;

/**
 * Controller for the gameplay screen, where gameplay happens.
 */
public class GameplayController extends GenericController {
    private static final int HORIZONTAL_SPACING = 100;
    private static final int VERTICAL_SPACING = 30;
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

    private void setTopContent(Node node) {
        node.getStyleClass().add(GameplayScreen.Css.TOP_CONTENT);
        screen.getTopPrompt().setCenter(node);
    }

    private void setLeftPrompt(Node node) {
        node.getStyleClass().addAll(GameplayScreen.Css.PROMPT, GameplayScreen.Css.LEFT_PROMPT);
        screen.getContentPane().setLeft(node);
    }

    private void setBottomPrompt(Node node) {
        node.getStyleClass().add(GameplayScreen.Css.PROMPT);
        screen.getContentPane().setBottom(node);
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
     * Sets the screen to show its default contents.
     */
    public void showDefault() {
        final Passage currentPassage = gameplayManager.getCurrentPassage();

        //TOP
        VBox topContent = new VBox(VERTICAL_SPACING);

        Label title = new Label(currentPassage.getTitle());
        title.getStyleClass().add(GameplayScreen.Css.HEADER);
        topContent.getChildren().add(title);

        topContent.getChildren().add(new Label(currentPassage.getContent()));

        setTopContent(topContent);

        //LEFT
        ScrollPane leftScroll = new DebugScrollPane();

        VBox leftContent = new VBox(VERTICAL_SPACING);
        leftScroll.setContent(leftContent);

        Label moveText = new Label("Move:");
        moveText.getStyleClass().add(GameplayScreen.Css.HEADER);
        leftContent.getChildren().add(moveText);

        for (Link link : currentPassage.getLinks()) {
            Button linkButton = new Button(link.getText());
            linkButton.getStyleClass().add(GameplayScreen.Css.EMPHASIZED_BUTTON);
            linkButton.setOnAction(ae -> movePassage(link));
            leftContent.getChildren().add(linkButton);
        }

        setLeftPrompt(leftScroll);

        //BOTTOM
        HBox bottomContent = new HBox(HORIZONTAL_SPACING);
        bottomContent.getStyleClass().add(GameplayScreen.Css.PROMPT);

        Button fightButton = new Button("Fight");
        fightButton.getStyleClass().add(GameplayScreen.Css.EMPHASIZED_BUTTON);
        //TODO: Make this button work
        fightButton.setDisable(true);
        bottomContent.getChildren().add(fightButton);

        Button itemButton = new Button("Inventory");
        itemButton.getStyleClass().add(GameplayScreen.Css.EMPHASIZED_BUTTON);
        itemButton.setOnAction(ae -> showInventoryLeft());
        bottomContent.getChildren().add(itemButton);

        setBottomPrompt(bottomContent);
    }

    /**
     * Shows yhe player's inventory on the left side of the screen.
     */
    public void showInventoryLeft() {
        Inventory inventory = GameplayManager.getInstance().getGame().getPlayer().getInventory();
        try {
            inventory.addItem(ItemFactory.makeItem("Test item"));
            inventory.addItem(ItemFactory.makeItem("Another test item"));
            inventory.addItem(ItemFactory.makeItem("Large gold nugget"));
        } catch (FullInventoryException fie) {
            fie.printStackTrace();
        }

        screen
                .getInventoryContent()
                .setItems(FXCollections
                        .observableArrayList(GameplayManager
                                .getInstance()
                                .getGame()
                                .getPlayer()
                                .getInventory()
                                .getContents()));

        setLeftPrompt(screen.getInventoryPrompt());
    }

    /**
     * Shows the game's menu at in the bottom prompt.
     */
    public void showPauseModal() {
        addModal(screen.getPauseModal());
    }

    public void movePassage(Link link) {
        gameplayManager.movePassage(link);
        showDefault();
    }
}
