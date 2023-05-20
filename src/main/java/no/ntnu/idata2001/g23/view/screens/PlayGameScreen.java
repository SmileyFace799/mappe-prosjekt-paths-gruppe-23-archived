package no.ntnu.idata2001.g23.view.screens;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import no.ntnu.idata2001.g23.controllers.PlayGameController;
import no.ntnu.idata2001.g23.middleman.GameUpdateListener;
import no.ntnu.idata2001.g23.middleman.GameplayManager;
import no.ntnu.idata2001.g23.middleman.events.GameUpdateEvent;
import no.ntnu.idata2001.g23.middleman.events.NewGameEvent;
import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.misc.GlobalCss;

/**
 * The play game screen, that shows after the player selects "play game".
 */
public class PlayGameScreen extends GenericScreen implements GameUpdateListener {
    private final PlayGameController controller;
    private Button continueStory;

    /**
     * Makes the play game screen.
     *
     * @param application The application instance to give to the controller
     */
    public PlayGameScreen(DungeonApp application) {
        super();
        controller = new PlayGameController(application);
        GameplayManager.getInstance().addUpdateListener(this);
    }

    @Override
    protected void initializeNodes() {
        continueStory = new Button("Continue Story");
        continueStory.setOnAction(ae -> controller.changeScreen(GameplayScreen.class));
        continueStory.setDisable(true);
    }

    @Override
    protected Pane makeRoot() {
        VBox content = new VBox(60);

        Label playGameTitle = new Label("Play Game");
        playGameTitle.getStyleClass().add(GlobalCss.HEADER);
        content.getChildren().add(playGameTitle);

        content.getChildren().add(new Rectangle(0, 200));

        content.getChildren().add(continueStory);

        Button startNewStory = new Button("Start New Story");
        startNewStory.setOnAction(ae -> controller.changeScreen(NewGameScreen.class));
        content.getChildren().add(startNewStory);

        Button tutorial = new Button("Tutorial");
        //TODO: Make this button work
        tutorial.setDisable(true);
        content.getChildren().add(tutorial);

        Button backButton = new Button("Back");
        backButton.setOnAction(ae -> controller.changeScreen(MainMenuScreen.class));
        content.getChildren().add(backButton);
        return content;
    }

    @Override
    public void onUpdate(GameUpdateEvent event) {
        if (event instanceof NewGameEvent) {
            continueStory.setDisable(false);
        }
    }
}
