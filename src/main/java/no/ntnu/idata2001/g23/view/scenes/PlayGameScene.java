package no.ntnu.idata2001.g23.view.scenes;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import no.ntnu.idata2001.g23.controllers.PlayGameController;
import no.ntnu.idata2001.g23.view.textures.TxLoader;

/**
 * The play game scene, where after the player selects "play game".
 */
public class PlayGameScene extends GenericScene<PlayGameController> {
    public PlayGameScene(PlayGameController controller) {
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

        Label playGameTitle = new Label("Play Game");
        playGameTitle.getStyleClass().add("header");
        content.getChildren().add(playGameTitle);

        content.getChildren().add(new Rectangle(0, 200));

        Button startNewStory = new Button("Start New Story");
        startNewStory.setOnAction(ae -> controller.changeScene(NewGameScene.class));
        content.getChildren().add(startNewStory);

        Button loadgame = new Button("Load Game");
        content.getChildren().add(loadgame);

        Button tutorial = new Button("Tutorial");
        content.getChildren().add(tutorial);

        Button backButton = new Button("Go Back");
        backButton.setOnAction(ae -> controller.changeScene(MainMenuScene.class));
        content.getChildren().add(backButton);
        return content;
    }
}
