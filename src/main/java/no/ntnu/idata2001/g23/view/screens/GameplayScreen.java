package no.ntnu.idata2001.g23.view.screens;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
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
import no.ntnu.idata2001.g23.view.PathsApp;
import no.ntnu.idata2001.g23.view.misc.DebugScrollPane;
import no.ntnu.idata2001.g23.view.misc.GlobalCss;
import no.ntnu.idata2001.g23.view.textures.ImageLoader;

/**
 * The gameplay screen, where the game is played.
 */
public class GameplayScreen extends GenericScreen {
    /**
     * The default horizontal spacing between elements.
     */
    public static final int HORIZONTAL_SPACING = 50;
    /**
     * The default vertical spacing between elements.
     */
    public static final int VERTICAL_SPACING = 39;

    /**
     * Filename for the back icon.
     */
    private static final String BACK_ICON = "backIcon.png";

    private final GameplayController controller;

    private BorderPane contentPane;

    private VBox pauseModal;
    private VBox confirmRestartModal;

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

    private ScrollPane goalsPrompt;
    private VBox goalsBox;

    private ScrollPane historyPrompt;
    private VBox historyContent;

    private VBox statPrompt;
    private Label nameLabel;
    private Label hpLabel;
    private Label goldLabel;
    private Label scoreLabel;
    private Label weaponLabel;

    private ScrollPane enemyPane;
    private TilePane enemyContent;

    /**
     * Makes the gameplay screen.
     *
     * @param application The application instance to give to the controller.
     */
    public GameplayScreen(PathsApp application) {
        super("gameplay.css");
        controller = new GameplayController(this, application);
    }

    @Override
    public StackPane getRootPane() {
        return (StackPane) super.getRootPane();
    }

    /**
     * Gets the contentPane field.
     *
     * @return The contentPane field
     */
    public BorderPane getContentPane() {
        return contentPane;
    }

    /**
     * Gets the pauseModal field.
     *
     * @return The pauseModal field
     */
    public VBox getPauseModal() {
        return pauseModal;
    }

    /**
     * Gets the confirmRestartModal field.
     *
     * @return The confirmRestartModal field
     */
    public VBox getConfirmRestartModal() {
        return confirmRestartModal;
    }

    /**
     * Gets the passageTitle field.
     *
     * @return The passageTitle field
     */
    public Label getPassageTitle() {
        return passageTitle;
    }

    /**
     * Gets the passageText field.
     *
     * @return The passageText field
     */
    public Label getPassageText() {
        return passageText;
    }

    /**
     * Gets the actionPrompt field.
     *
     * @return The actionPrompt field
     */
    public VBox getActionPrompt() {
        return actionPrompt;
    }

    /**
     * Gets the movePrompt field.
     *
     * @return The movePrompt field
     */
    public ScrollPane getMovePrompt() {
        return movePrompt;
    }

    /**
     * Gets the moveOptions field.
     *
     * @return The moveOptions field
     */
    public VBox getMoveOptions() {
        return moveOptions;
    }

    /**
     * Gets the inventoryPrompt field.
     *
     * @return The inventoryPrompt field
     */
    public VBox getInventoryPrompt() {
        return inventoryPrompt;
    }

    /**
     * Gets the viewItemsPrompt field.
     *
     * @return The viewItemsPrompt field
     */
    public ScrollPane getViewItemsPrompt() {
        return viewItemsPrompt;
    }

    /**
     * Gets the viewItemsView field.
     *
     * @return The viewItemsView field
     */
    public ListView<Item> getViewItemsView() {
        return viewItemsView;
    }

    /**
     * Gets the useItemPrompt field.
     *
     * @return The useItemPrompt field
     */
    public ScrollPane getUseItemPrompt() {
        return useItemPrompt;
    }

    /**
     * Gets the useItemView field.
     *
     * @return The useItemView field
     */
    public ListView<UsableItem> getUseItemView() {
        return useItemView;
    }

    /**
     * Gets the equipWeaponPrompt field.
     *
     * @return The equipWeaponPrompt field
     */
    public ScrollPane getEquipWeaponPrompt() {
        return equipWeaponPrompt;
    }

    /**
     * Gets the equipWeaponView field.
     *
     * @return The equipWeaponView field
     */
    public ListView<Weapon> getEquipWeaponView() {
        return equipWeaponView;
    }

    /**
     * Gets the goalsPrompt field.
     *
     * @return The goalsPrompt field
     */
    public ScrollPane getGoalsPrompt() {
        return goalsPrompt;
    }

    /**
     * Gets the goalsBox field.
     *
     * @return The goalsBox field
     */
    public VBox getGoalsBox() {
        return goalsBox;
    }

    /**
     * Gets the historyContent field.
     *
     * @return The historyContent field
     */
    public VBox getHistoryContent() {
        return historyContent;
    }

    /**
     * Gets the nameLabel field.
     *
     * @return The nameLabel field
     */
    public Label getNameLabel() {
        return nameLabel;
    }

    /**
     * Gets the hpLabel field.
     *
     * @return The hpLabel field
     */
    public Label getHpLabel() {
        return hpLabel;
    }

    /**
     * Gets the goldLabel field.
     *
     * @return The goldLabel field
     */
    public Label getGoldLabel() {
        return goldLabel;
    }

    /**
     * Gets the scoreLabel field.
     *
     * @return The scoreLabel field
     */
    public Label getScoreLabel() {
        return scoreLabel;
    }

    /**
     * Gets the weaponLabel field.
     *
     * @return The weaponLabel field
     */
    public Label getWeaponLabel() {
        return weaponLabel;
    }

    /**
     * Gets the enemyContent field.
     *
     * @return The enemyContent field
     */
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

        Button restartButton = new Button("Restart");
        restartButton.setOnAction(ae -> controller.showConfirmRestartModal());
        pauseModal.getChildren().add(restartButton);

        Button mainMenuButton = new Button("Main menu");
        mainMenuButton.setOnAction(ae -> controller.changeScreen(MainMenuScreen.class));
        pauseModal.getChildren().add(mainMenuButton);
    }

    private void initializeConfirmRestartModal() {
        confirmRestartModal = new VBox(VERTICAL_SPACING);
        confirmRestartModal.getStyleClass().add(Css.PROMPT);

        Label confirmTitle = new Label("Are you sure you want to restart?");
        confirmTitle.getStyleClass().add(GlobalCss.HEADER);
        confirmRestartModal.getChildren().add(confirmTitle);

        HBox buttonBox = new HBox((double) HORIZONTAL_SPACING * 10);
        buttonBox.setStyle("-fx-alignment: center");
        confirmRestartModal.getChildren().add(buttonBox);

        Button yesButton = new Button("Yes");
        yesButton.setOnAction(ae -> controller.restartGame());
        buttonBox.getChildren().add(yesButton);

        Button noButton = new Button("No");
        noButton.setOnAction(ae -> controller.removeTopModal());
        buttonBox.getChildren().add(noButton);
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

        Button inventoryButton = new Button("Inventory");
        inventoryButton.setOnAction(ae -> controller.showInventoryPrompt());
        actionPrompt.getChildren().add(inventoryButton);

        actionPrompt.getChildren().add(new Separator());

        Button goalsButton = new Button("View goals");
        goalsButton.setOnAction(ae -> controller.showGoalsPrompt());
        actionPrompt.getChildren().add(goalsButton);
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
        selectedItemDetails.getStyleClass().add(Css.SIDE_TEXT);
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
        selectedItemDetails.getStyleClass().add(Css.SIDE_TEXT);
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
        selectedItemDetails.getStyleClass().add(Css.SIDE_TEXT);
        equipWeaponView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> selectedItemDetails
                        .setText(newValue != null ? newValue.getDetails() : null));
        equipWeaponContent.getChildren().add(selectedItemDetails);

        Button equipButton = new Button("Equip");
        equipButton.setOnAction(ae -> controller.equipSelectedWeapon());
        equipWeaponContent.getChildren().add(equipButton);

        Button unEquipButton = new Button("Un-equip current weapon");
        unEquipButton.setOnAction(ae -> controller.unEquipCurrentWeapon());
        equipWeaponContent.getChildren().add(unEquipButton);
    }

    private void initializeGoalsPrompt() {
        goalsPrompt = new DebugScrollPane();
        goalsPrompt.getStyleClass().add(Css.LEFT_PROMPT);

        VBox goalsContent = new VBox(VERTICAL_SPACING);
        goalsPrompt.setContent(goalsContent);

        Label goalsTitle = new Label("Goals");
        goalsTitle.getStyleClass().add(GlobalCss.HEADER);
        goalsContent.getChildren().add(goalsTitle);

        goalsBox = new VBox(VERTICAL_SPACING);
        goalsContent.getChildren().add(goalsBox);

        Button backButton = new Button("Back");
        backButton.setOnAction(ae -> controller.showActionPrompt());
        goalsContent.getChildren().add(backButton);
    }

    private void initializeHistoryPrompt() {
        historyPrompt = new DebugScrollPane();
        historyPrompt.getStyleClass().add(Css.RIGHT_PROMPT);

        VBox historyContentContainer = new VBox(VERTICAL_SPACING);
        historyContentContainer.heightProperty().addListener(observable ->
                historyPrompt.setVvalue(historyPrompt.getVmax()));
        historyPrompt.setContent(historyContentContainer);

        Label historyHeader = new Label("History");
        historyHeader.getStyleClass().add(GlobalCss.HEADER);
        historyContentContainer.getChildren().add(historyHeader);

        historyContent = new VBox(VERTICAL_SPACING);
        historyContent.setPrefWidth(750);
        historyContentContainer.getChildren().add(historyContent);
    }

    private void initializeStatPrompt() {
        statPrompt = new VBox(VERTICAL_SPACING);
        statPrompt.getStyleClass().add(Css.PROMPT);

        nameLabel = new Label();
        nameLabel.getStyleClass().add(GlobalCss.HEADER);
        statPrompt.getChildren().add(nameLabel);

        HBox statBox = new HBox(HORIZONTAL_SPACING * 4d);
        statBox.setStyle("-fx-alignment: center;");
        statPrompt.getChildren().add(statBox);

        hpLabel = new Label();
        statBox.getChildren().add(hpLabel);

        goldLabel = new Label();
        statBox.getChildren().add(goldLabel);

        scoreLabel = new Label();
        statBox.getChildren().add(scoreLabel);

        weaponLabel = new Label();
        statBox.getChildren().add(weaponLabel);
    }

    private void initializeEnemyPane() {
        enemyPane = new DebugScrollPane();
        enemyPane.getStyleClass().add(Css.CENTER_PANE);

        enemyContent = new TilePane();
        enemyContent.getStyleClass().add(Css.TILE_PANE);
        enemyPane.setContent(enemyContent);
    }

    @Override
    protected void initializeNodes() {
        initializePauseModal();
        initializeConfirmRestartModal();

        initializeTopPrompt();

        initializeActionPrompt();
        initializeMovePrompt();
        initializeInventoryPrompt();
        initializeViewItemsPrompt();
        initializeUseItemPrompt();
        initializeEquipWeaponPrompt();
        initializeGoalsPrompt();

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
    }

    /**
     * Contains custom {@code .css} style classes used by the gameplay screen.
     * This class is public so {@link GameplayController} can access it.
     */
    public static class Css {
        /**
         * overlay .css class.
         */
        public static final String OVERLAY = "overlay";
        /**
         * prompt .css class.
         */
        public static final String PROMPT = "prompt";
        /**
         * top-prompt .css class.
         */
        public static final String TOP_PROMPT = "top-prompt";
        /**
         * left-prompt .css class.
         */
        public static final String LEFT_PROMPT = "left-prompt";
        /**
         * right-prompt .css class.
         */
        public static final String RIGHT_PROMPT = "right-prompt";
        /**
         * center-pane .css class.
         */
        public static final String CENTER_PANE = "center-pane";
        /**
         * top-content .css class.
         */
        public static final String TOP_CONTENT = "top-content";
        /**
         * tile-pane .css class.
         */
        public static final String TILE_PANE = "tile-pane";
        /**
         * back-button-box .css class.
         */
        public static final String BACK_BUTTON_BOX = "back-button-box";
        /**
         * enemy-button .css class.
         */
        public static final String ENEMY_BUTTON = "enemy-button";
        /**
         * side-text .css class.
         */
        public static final String SIDE_TEXT = "side-text";

        private Css() {
            throw new IllegalStateException("Do not instantiate this class pls :)");
        }
    }
}
