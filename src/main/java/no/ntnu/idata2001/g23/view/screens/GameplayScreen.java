package no.ntnu.idata2001.g23.view.screens;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import no.ntnu.idata2001.g23.controllers.GameplayController;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.UsableItem;
import no.ntnu.idata2001.g23.model.items.Weapon;
import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.misc.DebugScrollPane;
import no.ntnu.idata2001.g23.view.misc.GlobalCss;
import no.ntnu.idata2001.g23.view.textures.ImageLoader;

/**
 * The gameplay screen, where the game is played.
 */
public class GameplayScreen extends GenericScreen {
    private static final int HORIZONTAL_SPACING = 50;
    private static final int VERTICAL_SPACING = 30;
    private static final String BACK_ICON = "backIcon.png";

    private final GameplayController controller;

    private BorderPane contentPane;

    private VBox pauseModal;

    private BorderPane topPrompt;
    private Label passageTitle;
    private Label passageText;

    private VBox actionPrompt;

    private ScrollPane movePrompt;
    private VBox moveOptions;

    private VBox inventoryPrompt;
    private ScrollPane viewItemsPrompt;
    private ListView<Item> viewItemsView;
    private ScrollPane useItemPrompt;
    private ListView<UsableItem> useItemView;
    private ScrollPane equipWeaponPrompt;
    private ListView<Weapon> equipWeaponView;

    private ScrollPane historyPrompt;
    private VBox historyContent;

    private HBox statPrompt;
    private Label nameLabel;
    private Label hpLabel;
    private Label goldLabel;
    private Label scoreLabel;

    private ScrollPane enemyPane;
    private TilePane enemyContent;

    /**
     * Makes the gameplay screen.
     *
     * @param application The application instance to give to the controller.
     */
    public GameplayScreen(DungeonApp application) {
        super("gameplay.css");
        controller = new GameplayController(this, application);
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

    public ScrollPane getViewItemsPrompt() {
        return viewItemsPrompt;
    }

    public ListView<Item> getViewItemsView() {
        return viewItemsView;
    }

    public ScrollPane getUseItemPrompt() {
        return useItemPrompt;
    }

    public ListView<UsableItem> getUseItemView() {
        return useItemView;
    }

    public ScrollPane getEquipWeaponPrompt() {
        return equipWeaponPrompt;
    }

    public ListView<Weapon> getEquipWeaponView() {
        return equipWeaponView;
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

    public TilePane getEnemyContent() {
        return enemyContent;
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
        menuButton.setGraphic(ImageLoader.getIcon(
                ImageLoader.getImageResource("menuIcon.png"), 100));
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
        backButton.setGraphic(ImageLoader.getIcon(
                ImageLoader.getImageResource(BACK_ICON), 100));
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
        titleBox.getStyleClass().add(Css.BACK_BUTTON_BOX);
        inventoryPrompt.getChildren().add(titleBox);

        Button backButton = new Button();
        backButton.setGraphic(ImageLoader.getIcon(
                ImageLoader.getImageResource(BACK_ICON), 100));
        backButton.setOnAction(ae -> controller.showActionPrompt());
        titleBox.getChildren().add(backButton);

        Label inventoryHeader = new Label("Inventory");
        inventoryHeader.getStyleClass().add(GlobalCss.HEADER);
        titleBox.getChildren().add(inventoryHeader);

        Button viewItems = new Button("View items");
        viewItems.setOnAction(ae -> controller.showViewItemsPrompt());
        inventoryPrompt.getChildren().add(viewItems);

        Button useItem = new Button("Use item");
        useItem.setOnAction(ae -> controller.showUseItemPrompt());
        inventoryPrompt.getChildren().add(useItem);

        Button equipWeapon = new Button("Equip weapon");
        equipWeapon.setOnAction(ae -> controller.showEquipWeaponPrompt());
        inventoryPrompt.getChildren().add(equipWeapon);
    }

    private void initializeViewItemsPrompt() {
        viewItemsPrompt = new DebugScrollPane();
        viewItemsPrompt.getStyleClass().addAll(Css.LEFT_PROMPT);

        VBox viewItemsContent = new VBox(VERTICAL_SPACING);
        viewItemsPrompt.setContent(viewItemsContent);

        HBox titleBox = new HBox(HORIZONTAL_SPACING);
        titleBox.getStyleClass().add(Css.BACK_BUTTON_BOX);
        viewItemsContent.getChildren().add(titleBox);

        Button backButton = new Button();
        backButton.setGraphic(ImageLoader.getIcon(
                ImageLoader.getImageResource(BACK_ICON), 100));
        backButton.setOnAction(ae -> controller.showInventoryPrompt());
        titleBox.getChildren().add(backButton);

        Label inventoryHeader = new Label("Items");
        inventoryHeader.getStyleClass().add(GlobalCss.HEADER);
        titleBox.getChildren().add(inventoryHeader);

        viewItemsView = new ListView<>();
        viewItemsView.setPlaceholder(new Label("(No items)"));
        viewItemsView.setCellFactory(param -> new ListCell<>() {
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
        viewItemsContent.getChildren().add(viewItemsView);
        Label selectedItemDetails = new Label();
        viewItemsView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> selectedItemDetails
                        .setText(newValue != null ? newValue.getDetails() : null));
        viewItemsContent.getChildren().add(selectedItemDetails);
    }

    private void initializeUseItemPrompt() {
        useItemPrompt = new DebugScrollPane();
        useItemPrompt.getStyleClass().addAll(Css.LEFT_PROMPT);

        VBox useItemContent = new VBox(VERTICAL_SPACING);
        useItemPrompt.setContent(useItemContent);

        HBox titleBox = new HBox(HORIZONTAL_SPACING);
        titleBox.getStyleClass().add(Css.BACK_BUTTON_BOX);
        useItemContent.getChildren().add(titleBox);

        Button backButton = new Button();
        backButton.setGraphic(ImageLoader.getIcon(
                ImageLoader.getImageResource(BACK_ICON), 100));
        backButton.setOnAction(ae -> controller.showInventoryPrompt());
        titleBox.getChildren().add(backButton);

        Label inventoryHeader = new Label("Use an item");
        inventoryHeader.getStyleClass().add(GlobalCss.HEADER);
        titleBox.getChildren().add(inventoryHeader);

        useItemView = new ListView<>();
        useItemView.setPlaceholder(new Label("(No usable items)"));
        useItemView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(UsableItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        useItemContent.getChildren().add(useItemView);

        Label selectedItemDetails = new Label();
        useItemView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> selectedItemDetails
                        .setText(newValue != null ? newValue.getDetails() : null));
        useItemContent.getChildren().add(selectedItemDetails);

        Button useButton = new Button("Use");
        useButton.setOnAction(ae -> controller.useSelectedItem());
        useItemContent.getChildren().add(useButton);
    }

    private void initializeEquipWeaponPrompt() {
        equipWeaponPrompt = new DebugScrollPane();
        equipWeaponPrompt.getStyleClass().addAll(Css.LEFT_PROMPT);

        VBox equipWeaponContent = new VBox(VERTICAL_SPACING);
        equipWeaponPrompt.setContent(equipWeaponContent);

        HBox titleBox = new HBox(HORIZONTAL_SPACING);
        titleBox.getStyleClass().add(Css.BACK_BUTTON_BOX);
        equipWeaponContent.getChildren().add(titleBox);

        Button backButton = new Button();
        backButton.setGraphic(ImageLoader.getIcon(
                ImageLoader.getImageResource(BACK_ICON), 100));
        backButton.setOnAction(ae -> controller.showInventoryPrompt());
        titleBox.getChildren().add(backButton);

        Label inventoryHeader = new Label("Equip a weapon");
        inventoryHeader.getStyleClass().add(GlobalCss.HEADER);
        titleBox.getChildren().add(inventoryHeader);

        equipWeaponView = new ListView<>();
        equipWeaponView.setPlaceholder(new Label("(No weapons)"));
        equipWeaponView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Weapon item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        equipWeaponContent.getChildren().add(equipWeaponView);

        Label selectedItemDetails = new Label();
        equipWeaponView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> selectedItemDetails
                        .setText(newValue != null ? newValue.getDetails() : null));
        equipWeaponContent.getChildren().add(selectedItemDetails);

        Button equipButton = new Button("Equip");
        equipButton.setOnAction(ae -> controller.equipSelectedWeapon());
        equipWeaponContent.getChildren().add(equipButton);
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

    private void initializeEnemyPane() {
        enemyPane = new DebugScrollPane();
        enemyPane.getStyleClass().add(Css.CENTER_PANE);

        enemyContent = new TilePane();
        enemyContent.getStyleClass().add(Css.CENTER_CONTENT);
        enemyPane.setContent(enemyContent);
    }

    @Override
    protected void initializeNodes() {
        initializePauseModal();

        initializeTopPrompt();

        initializeActionPrompt();
        initializeMovePrompt();
        initializeInventoryPrompt();
        initializeViewItemsPrompt();
        initializeUseItemPrompt();
        initializeEquipWeaponPrompt();

        initializeHistoryPrompt();

        initializeStatPrompt();

        initializeEnemyPane();
    }

    @Override
    protected Pane makeRoot() {
        contentPane = new BorderPane();
        contentPane.setTop(topPrompt);
        contentPane.setRight(historyPrompt);
        contentPane.setBottom(statPrompt);
        contentPane.setCenter(enemyPane);

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

    /**
     * Contains custom {@code .css} style classes used by the gameplay screen.
     * This class is public so {@link GameplayController} can access it.
     */
    public static class Css {
        public static final String OVERLAY = "overlay";
        public static final String PROMPT = "prompt";
        public static final String TOP_PROMPT = "top-prompt";
        public static final String LEFT_PROMPT = "left-prompt";
        public static final String RIGHT_PROMPT = "right-prompt";
        public static final String CENTER_PANE = "center-pane";
        public static final String TOP_CONTENT = "top-content";
        public static final String CENTER_CONTENT = "center-content";
        public static final String BACK_BUTTON_BOX = "back-button-box";

        private Css() {
            throw new IllegalStateException("Do not instantiate this class pls :)");
        }
    }
}
