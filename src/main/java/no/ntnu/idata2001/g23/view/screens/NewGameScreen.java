package no.ntnu.idata2001.g23.view.screens;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import no.ntnu.idata2001.g23.controllers.NewGameController;
import no.ntnu.idata2001.g23.view.DungeonApp;

/**
 * The new game screen, where the player creates & starts a new game.
 */
public class NewGameScreen extends GenericScreen {
    private final NewGameController controller;

    public NewGameScreen(DungeonApp application) {
        super();
        controller = new NewGameController(this, application);
    }

    private TextField playerNameInput;
    private VBox errorBox;
    private TextField storyPathInput;

    public TextField getPlayerNameInput() {
        return playerNameInput;
    }

    public TextField getStoryPathInput() {
        return storyPathInput;
    }

    @Override
    public void setDefaultState() {
        playerNameInput.setText("");
        setErrorMessage(null);
    }

    @Override
    protected void initializeNodes() {
        playerNameInput = new TextField();
        playerNameInput.setMaxWidth(650);
        playerNameInput.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isContentChange()) {
                String newText = change.getControlNewText();
                int maxLength = 10;
                if (newText.length() > maxLength) {
                    change.setText(newText.substring(0, maxLength));
                    change.setRange(0, change.getControlText().length());
                }
            }
            return change;
        }));

        errorBox = new VBox();
        errorBox.setAlignment(Pos.CENTER);

        storyPathInput = new TextField();
        storyPathInput.setAlignment(Pos.TOP_LEFT);
    }

    @Override
    protected Pane makeRoot() {
        VBox content = new VBox(VERTICAL_SPACING);

        Label newGameTitle = new Label("New game");
        newGameTitle.getStyleClass().add("header");
        content.getChildren().add(newGameTitle);

        HBox nameBox = new HBox(HORIZONTAL_SPACING);
        nameBox.setAlignment(Pos.CENTER);
        content.getChildren().add(nameBox);

        nameBox.getChildren().add(new Label("Name:"));

        nameBox.getChildren().add(playerNameInput);

        HBox storyBox = new HBox(HORIZONTAL_SPACING);
        storyBox.setAlignment(Pos.CENTER);
        content.getChildren().add(storyBox);

        storyBox.getChildren().add(new Label("Story:"));

        HBox chooseStory = new HBox();
        chooseStory.setAlignment(Pos.CENTER);
        storyBox.getChildren().add(chooseStory);

        chooseStory.getChildren().add(storyPathInput);

        Button browseStory = new Button("Browse...");
        browseStory.setOnAction(ae -> controller.browseStory());
        chooseStory.getChildren().add(browseStory);

        content.getChildren().add(errorBox);

        content.getChildren().add(new Rectangle(0, 150));

        Button startPlaying = new Button("Start playing");
        startPlaying.setOnAction(ae -> controller.startNewGame());
        content.getChildren().add(startPlaying);

        Button backButton = new Button("Go Back");
        backButton.setOnAction(ae -> controller.changeScreen(PlayGameScreen.class));
        content.getChildren().add(backButton);

        return content;
    }

    /**
     * Shows an error message to the user when the story can't be started.
     *
     * @param errorMessage THe error message to show.
     *                     If this is {@code null}, the error message will be cleared
     */
    public void setErrorMessage(String errorMessage) {
        errorBox.getChildren().clear();
        if (errorMessage != null && !errorMessage.isBlank()) {
            Label errorLabel = new Label(errorMessage);
            errorLabel.getStyleClass().add("error-label");
            errorBox.getChildren().add(errorLabel);
        }
    }
}
