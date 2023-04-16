package no.ntnu.idata2001.g23.controllers;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.screens.GameplayScreen;
import no.ntnu.idata2001.g23.view.screens.MainMenuScreen;

/**
 * Controller for the gameplay screen, where gameplay happens.
 */
public class GameplayController extends GenericController {
    public GameplayController(GameplayScreen screen, DungeonApp application) {
        super(application);
        this.screen = screen;
    }

    private final GameplayScreen screen;

    public void showBottomDefault() {
        screen.setBottomContent(null);
        screen.updateRoot();
    }

    /**
     * Shows the game's menu at the bottom of the screen.
     */
    public void showBottomMenu() {
        HBox bottomContent = new HBox(100);

        Button resumeButton = new Button("Resume");
        resumeButton.setOnAction(ae -> showBottomDefault());
        bottomContent.getChildren().add(resumeButton);

        Button saveButton = new Button("Save game");
        //TODO: Save game features
        saveButton.setDisable(true);
        bottomContent.getChildren().add(saveButton);

        Button mainMenuButton = new Button("Main menu");
        mainMenuButton.setOnAction(ae -> changeScreen(MainMenuScreen.class));
        bottomContent.getChildren().add(mainMenuButton);

        screen.setBottomContent(bottomContent);
        screen.updateRoot();
    }
}
