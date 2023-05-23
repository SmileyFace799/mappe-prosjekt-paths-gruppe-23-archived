package no.ntnu.idata2001.g23.view.screens;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import no.ntnu.idata2001.g23.controllers.NewGameController;
import no.ntnu.idata2001.g23.view.PathsApp;
import no.ntnu.idata2001.g23.view.misc.GlobalCss;

/**
 * The new game screen, where the player creates and starts a new game.
 */
public class NewGameScreen extends GenericScreen {
    private final NewGameController controller;

    public NewGameScreen(PathsApp application) {
        super();
        controller = new NewGameController(this, application);
    }

    private BorderPane contentPane;
    private VBox gameSelect;
    private TextField playerNameInput;
    private Label gameSelectErrorText;
    private TextField gamePathInput;
    private VBox difficultySelect;
    private ListView<String> difficultyView;
    private Label difficultySelectErrorText;

    /**
     * Gets the contentPane field.
     *
     * @return The contentPane field
     */
    public BorderPane getContentPane() {
        return contentPane;
    }

    /**
     * Gets the gameSelect field.
     *
     * @return The gameSelect field
     */
    public VBox getGameSelect() {
        return gameSelect;
    }

    /**
     * Gets the playerNameInput field.
     *
     * @return The playerNameInput field
     */
    public TextField getPlayerNameInput() {
        return playerNameInput;
    }

    /**
     * Gets the gameSelectErrorText field.
     *
     * @return The gameSelectErrorText field
     */
    public Label getGameSelectErrorText() {
        return gameSelectErrorText;
    }

    /**
     * Gets the gamePathInput field.
     *
     * @return The gamePathInput field
     */
    public TextField getGamePathInput() {
        return gamePathInput;
    }

    /**
     * Gets the difficultySelect field.
     *
     * @return The difficultySelect field
     */
    public VBox getDifficultySelect() {
        return difficultySelect;
    }

    /**
     * Gets the difficultyView field.
     *
     * @return The difficultyView field
     */
    public ListView<String> getDifficultyView() {
        return difficultyView;
    }

    /**
     * Gets the difficultySelectErrorText field.
     *
     * @return The difficultySelectErrorText field
     */
    public Label getDifficultySelectErrorText() {
        return difficultySelectErrorText;
    }

    private void initializeGameSelect() {
        gameSelect = new VBox(VERTICAL_SPACING);
        gameSelect.setAlignment(Pos.CENTER);

        Label newGameTitle = new Label("New game");
        newGameTitle.getStyleClass().add(GlobalCss.HEADER);
        gameSelect.getChildren().add(newGameTitle);

        HBox nameBox = new HBox(HORIZONTAL_SPACING);
        nameBox.setAlignment(Pos.CENTER);
        gameSelect.getChildren().add(nameBox);

        nameBox.getChildren().add(new Label("Name:"));

        playerNameInput = new TextField();
        playerNameInput.setMaxWidth(1000);
        playerNameInput.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isContentChange()) {
                String newText = change.getControlNewText();
                int maxLength = 15;
                if (newText.length() > maxLength) {
                    change.setText(newText.substring(0, maxLength));
                    change.setRange(0, change.getControlText().length());
                }
            }
            return change;
        }));
        nameBox.getChildren().add(playerNameInput);

        HBox gameBox = new HBox(HORIZONTAL_SPACING);
        gameBox.setAlignment(Pos.CENTER);
        gameSelect.getChildren().add(gameBox);

        gameBox.getChildren().add(new Label("Game:"));

        HBox chooseGame = new HBox();
        chooseGame.setAlignment(Pos.CENTER);
        gameBox.getChildren().add(chooseGame);

        gamePathInput = new TextField();
        gamePathInput.setAlignment(Pos.TOP_LEFT);
        chooseGame.getChildren().add(gamePathInput);

        Button browseGame = new Button("Browse...");
        browseGame.setOnAction(ae -> controller.browseGame());
        chooseGame.getChildren().add(browseGame);

        gameSelectErrorText = new Label();
        gameSelectErrorText.getStyleClass().add(GlobalCss.ERROR_LABEL);
        gameSelect.getChildren().add(gameSelectErrorText);

        gameSelect.getChildren().add(new Rectangle(0, 25));

        Button nextButton = new Button("Next");
        nextButton.setOnAction(ae -> controller.showDifficultySelection());
        gameSelect.getChildren().add(nextButton);

        Button backButton = new Button("Back");
        backButton.setOnAction(ae -> controller.changeScreen(PlayGameScreen.class));
        gameSelect.getChildren().add(backButton);
    }

    private void initializeDifficultySelect() {
        difficultySelect = new VBox(VERTICAL_SPACING);
        difficultySelect.setAlignment(Pos.CENTER);

        Label newGameTitle = new Label("New game");
        newGameTitle.getStyleClass().add(GlobalCss.HEADER);
        difficultySelect.getChildren().add(newGameTitle);

        difficultyView = new ListView<>();
        difficultySelect.getChildren().add(difficultyView);

        difficultySelectErrorText = new Label();
        difficultySelectErrorText.getStyleClass().add(GlobalCss.ERROR_LABEL);
        difficultySelect.getChildren().add(difficultySelectErrorText);

        difficultySelect.getChildren().add(new Rectangle(0, 25));

        Button startGame = new Button("Start Game");
        startGame.setOnAction(ae -> controller.startNewGame());
        difficultySelect.getChildren().add(startGame);

        Button backButton = new Button("Back");
        backButton.setOnAction(ae -> controller.showGameSelection());
        difficultySelect.getChildren().add(backButton);
    }

    @Override
    protected void initializeNodes() {
        initializeGameSelect();
        initializeDifficultySelect();
    }

    @Override
    protected Pane makeRoot() {
        contentPane = new BorderPane();
        return contentPane;
    }

    @Override
    public void setDefaultState() {
        controller.showGameSelection();
        playerNameInput.setText("");
        controller.setGameSelectErrorMessage(null);
    }
}
