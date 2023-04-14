package no.ntnu.idata2001.g23.view.scenes;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import no.ntnu.idata2001.g23.controllers.GenericController;
import no.ntnu.idata2001.g23.view.textures.TxLoader;

/**
 * The new game scene, where the player creates & starts a new game.
 */
public class NewGameScene extends GenericScene<GenericController> {
    public NewGameScene(GenericController controller) {
        super(controller);
    }

    @Override
    protected Pane makeRoot() {
        VBox content = new VBox(60);
        content.setAlignment(Pos.CENTER);
        content.setBackground(new Background(new BackgroundImage(
                TxLoader.getImage("bg.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, true, false)
        )));

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
        backButton.setOnAction(ae -> controller.changeScene(PlayGameScene.class));
        content.getChildren().add(backButton);

        return content;
    }
}
