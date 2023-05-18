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
import no.ntnu.idata2001.g23.middleman.events.InventoryUpdateEvent;
import no.ntnu.idata2001.g23.middleman.events.NewGameEvent;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.UsableItem;
import no.ntnu.idata2001.g23.model.story.Passage;
import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.misc.DebugScrollPane;
import no.ntnu.idata2001.g23.view.misc.GlobalCss;
import no.ntnu.idata2001.g23.view.textures.TxLoader;

/**
 * The gameplay screen, where the game is played.
 */
public class GameplayScreen extends GenericScreen implements GameUpdateListener {
    private final GameplayController controller;
    protected static final int HORIZONTAL_SPACING = 50;
    protected static final int VERTICAL_SPACING = 30;

    private BorderPane contentPane;

    private VBox pauseModal;

    private BorderPane topPrompt;
    private Label passageTitle;
    private Label passageText;

    private VBox actionPrompt;

    private ScrollPane movePrompt;
    private VBox moveOptions;

    private VBox inventoryPrompt;
    private ListView<Item> inventoryContent;

    private ScrollPane historyPrompt;
    private VBox historyContent;

    private HBox statPrompt;
    private Label nameLabel;
    private Label hpLabel;
    private Label goldLabel;
    private Label scoreLabel;


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
    public StackPane getRootPane() {
        return (StackPane) super.getRootPane();
    }

    public BorderPane getContentPane() {
        return contentPane;
    }

    public VBox getPauseModal() {
        return pauseModal;
    }

    public Label getPassageTitle() {
        return passageTitle;
    }

    public Label getPassageText() {
        return passageText;
    }

    public VBox getActionPrompt() {
        return actionPrompt;
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

    public VBox getHistoryContent() {
        return historyContent;
    }

    public Label getNameLabel() {
        return nameLabel;
    }

    public Label getHpLabel() {
        return hpLabel;
    }

    public Label getGoldLabel() {
        return goldLabel;
    }

    public Label getScoreLabel() {
        return scoreLabel;
    }

    private void initializePauseModal() {
        pauseModal = new VBox(VERTICAL_SPACING);
        pauseModal.getStyleClass().add(Css.PROMPT);

        Label menuText = new Label("Game Paused");
        menuText.getStyleClass().add(GlobalCss.HEADER);
        pauseModal.getChildren().add(menuText);

        Button resumeButton = new Button("Resume");
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
        topPrompt.getStyleClass().addAll(Css.TOP_PROMPT);

        VBox passageContent = new VBox(VERTICAL_SPACING);
        passageContent.getStyleClass().add(Css.TOP_CONTENT);
        topPrompt.setCenter(passageContent);

        passageTitle = new Label();
        passageTitle.getStyleClass().add(GlobalCss.HEADER);
        passageContent.getChildren().add(passageTitle);

        passageText = new Label();
        passageContent.getChildren().add(passageText);

        Button menuButton = new Button();
        menuButton.setGraphic(TxLoader.getIcon("menuIcon.png", 100));
        menuButton.setOnAction(ae -> controller.showPauseModal());
        topPrompt.setRight(menuButton);
    }

    private void initializeActionPrompt() {
        actionPrompt = new VBox(VERTICAL_SPACING);
        actionPrompt.getStyleClass().add(Css.LEFT_PROMPT);

        Label actionHeader = new Label("Actions");
        actionHeader.getStyleClass().add(GlobalCss.HEADER);
        actionPrompt.getChildren().add(actionHeader);

        Button moveButton = new Button("Move");
        moveButton.setOnAction(ae -> controller.showMovePrompt());
        actionPrompt.getChildren().add(moveButton);

        Button fightButton = new Button("Fight");
        //TODO: Make this button work
        fightButton.setDisable(true);
        actionPrompt.getChildren().add(fightButton);

        Button inventoryButton = new Button("Inventory");
        inventoryButton.setOnAction(ae -> controller.showInventoryPrompt());
        actionPrompt.getChildren().add(inventoryButton);
    }

    private void initializeMovePrompt() {
        movePrompt = new DebugScrollPane();
        movePrompt.getStyleClass().addAll(Css.LEFT_PROMPT);

        VBox moveContent = new VBox(VERTICAL_SPACING);
        movePrompt.setContent(moveContent);

        HBox titleBox = new HBox(HORIZONTAL_SPACING);
        titleBox.setStyle("-fx-alignment: center-left;");
        moveContent.getChildren().add(titleBox);

        Button backButton = new Button();
        backButton.setGraphic(TxLoader.getIcon("backIcon.png", 100));
        backButton.setOnAction(ae -> controller.showActionPrompt());
        titleBox.getChildren().add(backButton);

        Label moveHeader = new Label("Move");
        moveHeader.getStyleClass().add(GlobalCss.HEADER);
        titleBox.getChildren().add(moveHeader);

        moveOptions = new VBox(VERTICAL_SPACING);
        moveContent.getChildren().add(moveOptions);
    }

    private void initializeInventoryPrompt() {
        inventoryPrompt = new VBox(VERTICAL_SPACING);
        inventoryPrompt.getStyleClass().addAll(Css.LEFT_PROMPT);

        HBox titleBox = new HBox(HORIZONTAL_SPACING);
        titleBox.setStyle("-fx-alignment: center-left");
        inventoryPrompt.getChildren().add(titleBox);

        Button backButton = new Button();
        backButton.setGraphic(TxLoader.getIcon("backIcon.png", 100));
        backButton.setOnAction(ae -> controller.showActionPrompt());
        titleBox.getChildren().add(backButton);

        Label inventoryHeader = new Label("Inventory");
        inventoryHeader.getStyleClass().add(GlobalCss.HEADER);
        titleBox.getChildren().add(inventoryHeader);

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
                    if (item instanceof UsableItem) {
                        setDisable(false);
                        setStyle("-fx-font-style: italic;");
                    } else {
                        setDisable(true);
                        setStyle("-fx-font-style: none;");
                    }
                }
            }
        });
        inventoryPrompt.getChildren().add(inventoryContent);

        Button useButton = new Button("Use selected item");
        useButton.setOnAction(ae -> controller.useSelectedItem());
        inventoryPrompt.getChildren().add(useButton);
    }

    private void initializeHistoryPrompt() {
        historyPrompt = new DebugScrollPane();
        historyPrompt.getStyleClass().add(Css.RIGHT_PROMPT);

        VBox historyContentContainer = new VBox(VERTICAL_SPACING);
        historyPrompt.setContent(historyContentContainer);

        Label historyHeader = new Label("History");
        historyHeader.getStyleClass().add(GlobalCss.HEADER);
        historyContentContainer.getChildren().add(historyHeader);

        historyContent = new VBox(VERTICAL_SPACING);
        historyContent.setPrefWidth(750);
        historyContentContainer.getChildren().add(historyContent);
    }

    private void initializeStatPrompt() {
        statPrompt = new HBox(HORIZONTAL_SPACING * 4D);
        statPrompt.getStyleClass().addAll(Css.PROMPT, GlobalCss.HEADER);

        nameLabel = new Label();
        statPrompt.getChildren().add(nameLabel);

        HBox hpBox = new HBox();
        statPrompt.getChildren().add(hpBox);

        hpBox.getChildren().add(new Label("HP: "));

        hpLabel = new Label();
        hpBox.getChildren().add(hpLabel);

        HBox goldBox = new HBox();
        statPrompt.getChildren().add(goldBox);

        goldBox.getChildren().add(new Label("Gold: "));

        goldLabel = new Label();
        goldBox.getChildren().add(goldLabel);

        HBox scoreBox = new HBox();
        statPrompt.getChildren().add(scoreBox);

        scoreBox.getChildren().add(new Label("Score: "));

        scoreLabel = new Label();
        scoreBox.getChildren().add(scoreLabel);
    }

    @Override
    protected void initializeNodes() {
        initializePauseModal();
        initializeTopPrompt();
        initializeActionPrompt();
        initializeMovePrompt();
        initializeInventoryPrompt();
        initializeHistoryPrompt();
        initializeStatPrompt();
    }

    @Override
    protected Pane makeRoot() {
        contentPane = new BorderPane();
        contentPane.setTop(topPrompt);
        contentPane.setRight(historyPrompt);
        contentPane.setBottom(statPrompt);

        StackPane root = new StackPane();
        root.getChildren().add(contentPane);
        return root;
    }

    @Override
    public void setDefaultState() {
        controller.removeAllModal();
        controller.showActionPrompt();
        controller.clearActionHistory();
    }

    @Override
    public void onUpdate(GameUpdateEvent event) {
        if (event instanceof NewGameEvent newGameEvent) {
            controller.updateCurrentPassage(newGameEvent.startPassage());
            controller.updateInventoryList(newGameEvent.game().getPlayer().getInventory());
            controller.updatePlayerStats(newGameEvent.game().getPlayer());
        } else if (event instanceof ChangePassageEvent changePassageEvent) {
            Passage currentPassage = changePassageEvent.currentPassage();
            controller.updateCurrentPassage(currentPassage);
            controller.logAction("Moved to " + currentPassage.getTitle());
        } else if (event instanceof InventoryUpdateEvent inventoryUpdateEvent) {
            controller.updateInventoryList(inventoryUpdateEvent.inventory());
        }
    }

    /**
     * Contains custom {@code .css} style classes used by the gameplay screen.
     * This class is public so {@link GameplayController} can access it.
     */
    public static class Css {
        private Css() {
            throw new IllegalStateException("Do not instantiate this class pls :)");
        }

        public static final String OVERLAY = "overlay";

        public static final String PROMPT = "prompt";
        public static final String TOP_PROMPT = "top-prompt";
        public static final String LEFT_PROMPT = "left-prompt";
        public static final String RIGHT_PROMPT = "right-prompt";

        public static final String TOP_CONTENT = "top-content";
    }
}
