package no.ntnu.idata2001.g23.view.screens;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import no.ntnu.idata2001.g23.controllers.GameOverController;
import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.misc.GlobalCss;

/**
 * The screen shown when the player loses a game.
 */
public class GameOverScreen extends GenericScreen {
    private final GameOverController controller;

    private Label deathCause;

    public GameOverScreen(DungeonApp application) {
        super();
        controller = new GameOverController(this, application);
    }

    public Label getDeathCause() {
        return deathCause;
    }

    @Override
    protected void initializeNodes() {
        deathCause = new Label();
    }

    @Override
    protected Pane makeRoot() {
        VBox root = new VBox((double) VERTICAL_SPACING * 5);

        Label title = new Label("Game over");
        title.getStyleClass().add(GlobalCss.HEADER);
        root.getChildren().add(title);

        root.getChildren().add(deathCause);

        VBox buttonBox = new VBox(VERTICAL_SPACING);
        root.getChildren().add(buttonBox);

        Button restartButton = new Button("Restart");
        restartButton.setOnAction(ae -> controller.restartGame());
        buttonBox.getChildren().add(restartButton);

        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setOnAction(ae -> controller.changeScreen(MainMenuScreen.class));
        buttonBox.getChildren().add(mainMenuButton);

        return root;
    }
}
