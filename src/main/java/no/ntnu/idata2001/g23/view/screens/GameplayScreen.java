package no.ntnu.idata2001.g23.view.screens;

import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import no.ntnu.idata2001.g23.controllers.GameplayController;
import no.ntnu.idata2001.g23.middleman.GameUpdateListener;
import no.ntnu.idata2001.g23.middleman.GameplayManager;
import no.ntnu.idata2001.g23.middleman.events.ChangePassageEvent;
import no.ntnu.idata2001.g23.middleman.events.GameUpdateEvent;
import no.ntnu.idata2001.g23.middleman.events.NewGameEvent;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.view.DebugScrollPane;
import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.textures.TxLoader;

/**
 * The gameplay screen, where the game is played.
 */
public class GameplayScreen extends GenericScreen implements GameUpdateListener {
    private final GameplayController controller;
    protected static final int HORIZONTAL_SPACING = 100;
    protected static final int VERTICAL_SPACING = 30;

    private BorderPane contentPane;

    private VBox pauseModal;

    private BorderPane topPrompt;
    private Label passageTitle;
    private Label passageText;

    private ScrollPane movePrompt;
    private VBox moveOptions;

    private VBox inventoryPrompt;
    private ListView<Item> inventoryContent;

    private HBox actionPrompt;

    /**
     * Makes the gameplay screen.
     *
     * @param application The application instance to give to the controller.
     */
    public GameplayScreen(DungeonApp application) {
        super("gameplay.css");
        controller = new GameplayController(this, application);
        GameplayManager.getInstance().addUpdateListener(this);
    }

    @Override
    public StackPane getRoot() {
        return (StackPane) super.getRoot();
    }

    public BorderPane getContentPane() {
        return contentPane;
    }

    public VBox getPauseModal() {
        return pauseModal;
    }

    public BorderPane getTopPrompt() {
        return topPrompt;
    }

    public Label getPassageTitle() {
        return passageTitle;
    }

    public Label getPassageText() {
        return passageText;
    }

    public ScrollPane getMovePrompt() {
        return movePrompt;
    }

    public VBox getMoveOptions() {
        return moveOptions;
    }

    public VBox getInventoryPrompt() {
        return inventoryPrompt;
    }

    public ListView<Item> getInventoryContent() {
        return inventoryContent;
    }

    private void initializePauseModal() {
        pauseModal = new VBox(VERTICAL_SPACING);
        pauseModal.getStyleClass().add(Css.PROMPT);

        Label menuText = new Label("Game Paused");
        menuText.getStyleClass().add(Css.HEADER);
        pauseModal.getChildren().add(menuText);

        Button resumeButton = new Button("Resume");
        resumeButton.getStyleClass().add(Css.EMPHASIZED_BUTTON);
        resumeButton.setOnAction(ae -> controller.removeTopModal());
        pauseModal.getChildren().add(resumeButton);

        Button saveButton = new Button("Save game");
        //TODO: Save game features
        saveButton.setDisable(true);
        pauseModal.getChildren().add(saveButton);

        Button mainMenuButton = new Button("Main menu");
        mainMenuButton.setOnAction(ae -> controller.changeScreen(MainMenuScreen.class));
        pauseModal.getChildren().add(mainMenuButton);
    }

    private void initializeTopPrompt() {
        topPrompt = new BorderPane();
        topPrompt.getStyleClass().addAll(Css.PROMPT, Css.TOP_PROMPT);

        VBox passageContent = new VBox(VERTICAL_SPACING);
        passageContent.getStyleClass().add(Css.TOP_CONTENT);
        topPrompt.setCenter(passageContent);

        passageTitle = new Label();
        passageTitle.getStyleClass().add(Css.HEADER);
        passageContent.getChildren().add(passageTitle);

        passageText = new Label();
        passageContent.getChildren().add(passageText);

        Button menuButton = new Button();
        menuButton.setGraphic(TxLoader.getIcon("menuIcon.png", 100));
        menuButton.setOnAction(ae -> controller.showPauseModal());
        topPrompt.setRight(menuButton);
    }

    private void initializeMovePrompt() {
        movePrompt = new DebugScrollPane();
        movePrompt.getStyleClass().addAll(Css.PROMPT, Css.LEFT_PROMPT);

        VBox moveContent = new VBox(VERTICAL_SPACING);
        movePrompt.setContent(moveContent);

        Label moveText = new Label("Move:");
        moveText.getStyleClass().add(Css.HEADER);
        moveContent.getChildren().add(moveText);

        moveOptions = new VBox(VERTICAL_SPACING);
        moveContent.getChildren().add(moveOptions);
    }

    private void initializeInventoryPrompt() {
        inventoryPrompt = new VBox(VERTICAL_SPACING);
        inventoryPrompt.getStyleClass().addAll(Css.PROMPT, Css.LEFT_PROMPT);

        Label inventoryHeader = new Label("Inventory");
        inventoryHeader.getStyleClass().add(Css.HEADER);
        inventoryPrompt.getChildren().add(inventoryHeader);

        inventoryContent = new ListView<>();
        inventoryContent.setPlaceholder(new Label("(No items)"));
        inventoryContent.setItems(FXCollections.observableArrayList());
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

    private void initializeActionPrompt() {
        actionPrompt = new HBox(HORIZONTAL_SPACING);
        actionPrompt.getStyleClass().add(Css.PROMPT);

        Button moveButton = new Button("Move");
        moveButton.getStyleClass().add(Css.HEADER);
        moveButton.setOnAction(ae -> controller.showMovePrompt());
        actionPrompt.getChildren().add(moveButton);

        Button fightButton = new Button("Fight");
        fightButton.getStyleClass().add(Css.EMPHASIZED_BUTTON);
        //TODO: Make this button work
        fightButton.setDisable(true);
        actionPrompt.getChildren().add(fightButton);

        Button inventoryButton = new Button("Inventory");
        inventoryButton.getStyleClass().add(Css.EMPHASIZED_BUTTON);
        inventoryButton.setOnAction(ae -> controller.showInventoryPrompt());
        actionPrompt.getChildren().add(inventoryButton);
    }

    @Override
    protected void initializeNodes() {
        initializeTopPrompt();
        initializePauseModal();
        initializeMovePrompt();
        initializeInventoryPrompt();
        initializeActionPrompt();
    }

    @Override
    protected Pane makeRoot() {
        contentPane = new BorderPane();
        contentPane.setTop(topPrompt);
        contentPane.setLeft(movePrompt);
        contentPane.setBottom(actionPrompt);

        StackPane root = new StackPane();
        root.getChildren().add(contentPane);
        return root;
    }

    @Override
    public void setDefaultState() {
        controller.removeAllModal();
        controller.showMovePrompt();
    }

    @Override
    public void onUpdate(GameUpdateEvent event) {
        if (event instanceof NewGameEvent newGameEvent) {
            controller.updatePassageText(newGameEvent.startPassage());
            controller.updateInventoryList(newGameEvent.game().getPlayer().getInventory());
        } else if (event instanceof ChangePassageEvent changePassageEvent) {
            controller.updatePassageText(changePassageEvent.currentPassage());
        }
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
