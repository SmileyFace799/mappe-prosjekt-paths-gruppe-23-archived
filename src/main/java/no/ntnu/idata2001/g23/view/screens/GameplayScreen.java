package no.ntnu.idata2001.g23.view.screens;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import no.ntnu.idata2001.g23.controllers.GameplayController;
import no.ntnu.idata2001.g23.middleman.GameplayManager;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.textures.TxLoader;

/**
 * The gameplay screen, where the game is played.
 */
public class GameplayScreen extends GenericScreen {
    private final GameplayController controller;

    private static final int HORIZONTAL_SPACING = 100;
    private static final int VERTICAL_SPACING = 30;

    //Initially invisible nodes, for later use
    private final VBox pauseModal;
    private final VBox inventoryPrompt;
    private final ListView<Item> inventoryContent;

    //Immediately visible nodes
    private BorderPane contentPane;
    private BorderPane topPrompt;

    /**
     * Makes the gameplay screen.
     *
     * @param application The application instance to give to the controller.
     */
    public GameplayScreen(DungeonApp application) {
        super("gameplay.css");
        controller = new GameplayController(this, application);

        //PAUSE MODAL
        pauseModal = new VBox(VERTICAL_SPACING);
        pauseModal.getStyleClass().add(Css.PROMPT);

        Label menuText = new Label("Game Paused");
        menuText.getStyleClass().add(Css.HEADER);
        pauseModal.getChildren().add(menuText);

        Button resumeButton = new Button("Resume");
        resumeButton.getStyleClass().add(GameplayScreen.Css.EMPHASIZED_BUTTON);
        resumeButton.setOnAction(ae -> controller.removeTopModal());
        pauseModal.getChildren().add(resumeButton);

        Button saveButton = new Button("Save game");
        //TODO: Save game features
        saveButton.setDisable(true);
        pauseModal.getChildren().add(saveButton);

        Button mainMenuButton = new Button("Main menu");
        mainMenuButton.setOnAction(ae -> controller.changeScreen(MainMenuScreen.class));
        pauseModal.getChildren().add(mainMenuButton);

        //INVENTORY PROMPT
        inventoryPrompt = new VBox(VERTICAL_SPACING);
        inventoryPrompt.getStyleClass().addAll(Css.PROMPT, Css.LEFT_PROMPT);

        Label inventoryHeader = new Label("Inventory");
        inventoryHeader.getStyleClass().add(Css.HEADER);
        inventoryPrompt.getChildren().add(inventoryHeader);

        inventoryContent = new ListView<>();
        inventoryContent.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        inventoryPrompt.getChildren().add(inventoryContent);

        Button useButton = new Button("Use selected item");
        //TODO: Make this button work
        useButton.setDisable(true);
        inventoryPrompt.getChildren().add(useButton);
    }

    @Override
    public StackPane getRoot() {
        return (StackPane) super.getRoot();
    }

    public VBox getPauseModal() {
        return pauseModal;
    }

    public VBox getInventoryPrompt() {
        return inventoryPrompt;
    }

    public ListView<Item> getInventoryContent() {
        return inventoryContent;
    }

    public BorderPane getContentPane() {
        return contentPane;
    }

    public BorderPane getTopPrompt() {
        return topPrompt;
    }

    @Override
    public void setDefaultState() {
        if (GameplayManager.getInstance().getGame() != null) {
            controller.removeAllModal();
            controller.showDefault();
        } else { //This should never trigger, only exists as a fail-safe
            VBox errorVbox = new VBox(20);
            errorVbox.getStyleClass().add("menu");
            errorVbox.getChildren().add(new Label(
                    "Oops, there's no game loaded, spaghetti code moment"));
            Button backButton = new Button("Main menu");
            backButton.setOnAction(ae -> controller.changeScreen(MainMenuScreen.class));
            errorVbox.getChildren().add(backButton);
            getRoot().getChildren().add(errorVbox);
        }
    }

    @Override
    protected Pane makeRoot() {
        StackPane root = new StackPane();

        contentPane = new BorderPane();
        root.getChildren().add(contentPane);

        topPrompt = new BorderPane();
        topPrompt.getStyleClass().addAll(Css.PROMPT, Css.TOP_PROMPT);
        contentPane.setTop(topPrompt);

        Button menuButton = new Button();
        menuButton.setGraphic(TxLoader.getIcon("menuIcon.png", 100));
        menuButton.setOnAction(ae -> controller.showPauseModal());
        topPrompt.setRight(menuButton);

        return root;
    }

    /**
     * Contains custom {@code .css} style classes used by the gameplay screen.
     */
    public static class Css {
        private Css() {
            throw new IllegalStateException("Utility class");
        }

        public static final String OVERLAY = "overlay";

        public static final String PROMPT = "prompt";
        public static final String TOP_PROMPT = "top-prompt";
        public static final String LEFT_PROMPT = "left-prompt";

        public static final String TOP_CONTENT = "top-content";

        public static final String HEADER = "header";
        public static final String EMPHASIZED_BUTTON = "emphasized-button";
    }
}
