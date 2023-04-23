package no.ntnu.idata2001.g23.view.screens;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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

    private TextField playerName;
    private Label errorLabel;

    @Override
    public void setDefaultState() {
        playerName.setText("");
        setErrorMessage(null);
    }

    @Override
    protected Pane makeRoot() {
        VBox content = new VBox(60);

        Label newGameTitle = new Label("New game");
        newGameTitle.getStyleClass().add("header");
        content.getChildren().add(newGameTitle);

        playerName = new TextField();
        playerName.setMaxWidth(500);
        playerName.setTextFormatter(new TextFormatter<>(change -> {
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
        content.getChildren().add(playerName);

        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        content.getChildren().add(errorLabel);

        Button startPlaying = new Button("Start playing");
        startPlaying.setOnAction(ae -> controller.startNewGame(playerName.getText()));
        content.getChildren().add(startPlaying);

        Button backButton = new Button("Go Back");
        backButton.setOnAction(ae -> controller.changeScreen(PlayGameScreen.class));
        content.getChildren().add(backButton);

        return content;
    }

    public void setErrorMessage(String errorMessage) {
        errorLabel.setText(errorMessage);
        errorLabel.setVisible(errorMessage != null && !errorMessage.isBlank());
    }
}
