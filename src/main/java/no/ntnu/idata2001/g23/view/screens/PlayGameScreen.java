package no.ntnu.idata2001.g23.view.screens;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import no.ntnu.idata2001.g23.controllers.PlayGameController;
import no.ntnu.idata2001.g23.view.DungeonApp;

/**
 * The play game screen, that shows after the player selects "play game".
 */
public class PlayGameScreen extends GenericScreen {
    private final PlayGameController controller;

    public PlayGameScreen(DungeonApp application) {
        super();
        controller = new PlayGameController(application);
    }

    @Override
    protected Pane makeRoot() {
        VBox content = new VBox(60);

        Label playGameTitle = new Label("Play Game");
        playGameTitle.getStyleClass().add("header");
        content.getChildren().add(playGameTitle);

        content.getChildren().add(new Rectangle(0, 200));

        Button startNewStory = new Button("Start New Story");
        startNewStory.setOnAction(ae -> controller.changeScreen(NewGameScreen.class));
        content.getChildren().add(startNewStory);

        Button loadgame = new Button("Load Game");
        //TODO: Make this button work
        loadgame.setDisable(true);
        content.getChildren().add(loadgame);

        Button tutorial = new Button("Tutorial");
        //TODO: Make this button work
        tutorial.setDisable(true);
        content.getChildren().add(tutorial);

        Button backButton = new Button("Go Back");
        backButton.setOnAction(ae -> controller.changeScreen(MainMenuScreen.class));
        content.getChildren().add(backButton);
        return content;
    }
}
