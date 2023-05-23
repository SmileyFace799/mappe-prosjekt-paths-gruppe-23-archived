package no.ntnu.idata2001.g23.view.screens;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import no.ntnu.idata2001.g23.controllers.VictoryController;
import no.ntnu.idata2001.g23.view.PathsApp;
import no.ntnu.idata2001.g23.view.misc.GlobalCss;

/**
 * The screen shown when the player wins a game.
 */
public class VictoryScreen extends GenericScreen {
    private final VictoryController controller;

    private Label victoryText;

    /**
     * Makes the victory screen.
     *
     * @param application The application instance to give to the controller
     */
    public VictoryScreen(PathsApp application) {
        super();
        this.controller = new VictoryController(this, application);
    }

    /**
     * Gets the victoryText field.
     *
     * @return The victoryText field
     */
    public Label getVictoryText() {
        return victoryText;
    }

    @Override
    protected void initializeNodes() {
        victoryText = new Label();
        victoryText.setStyle("-fx-text-alignment: center;");
    }

    @Override
    protected Pane makeRoot() {
        VBox root = new VBox((double) VERTICAL_SPACING * 3);

        Label victoryHeader = new Label("You Win!");
        victoryHeader.getStyleClass().add(GlobalCss.HEADER);
        root.getChildren().add(victoryHeader);

        root.getChildren().add(victoryText);

        VBox buttonBox = new VBox(VERTICAL_SPACING);
        buttonBox.setStyle("-fx-alignment: center;");
        root.getChildren().add(buttonBox);

        Button playAgainButton = new Button("Play Again");
        playAgainButton.setOnAction(ae -> controller.restartGame());
        buttonBox.getChildren().add(playAgainButton);

        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setOnAction(ae -> controller.changeScreen(MainMenuScreen.class));
        buttonBox.getChildren().add(mainMenuButton);

        return root;
    }
}
