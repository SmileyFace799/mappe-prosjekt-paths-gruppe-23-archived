package no.ntnu.idata2001.g23.view.screens;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import no.ntnu.idata2001.g23.controllers.GenericController;

/**
 * The new game screen, where the player creates & starts a new game.
 */
public class NewGameScreen extends GenericScreen<GenericController> {
    public NewGameScreen(GenericController controller) {
        super(controller);
    }

    @Override
    protected Pane makeRoot() {
        VBox content = new VBox(60);

        Label newGameTitle = new Label("New game");
        newGameTitle.getStyleClass().add("header");
        content.getChildren().add(newGameTitle);

        TextField playerName = new TextField();
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

        Button startPlaying = new Button("Start playing");
        content.getChildren().add(startPlaying);

        Button backButton = new Button("Go Back");
        backButton.setOnAction(ae -> controller.changeScreen(PlayGameScreen.class));
        content.getChildren().add(backButton);

        return content;
    }
}
