package no.ntnu.idata2001.g23.view.screens;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import no.ntnu.idata2001.g23.controllers.SettingsController;
import no.ntnu.idata2001.g23.view.DungeonApp;

/**
 * The settings screen, where the user can customize various application & game settings.
 */
public class SettingsScreen extends GenericScreen {
    private final SettingsController controller;

    public SettingsScreen(DungeonApp application) {
        super();
        this.controller = new SettingsController(application);
    }

    @Override
    protected Pane makeRoot() {
        VBox content = new VBox(60);

        Label settingsTitle = new Label("Settings");
        settingsTitle.getStyleClass().add("header");
        content.getChildren().add(settingsTitle);

        content.getChildren().add(new Rectangle(0, 200));

        Button backButton = new Button("Go Back");
        backButton.setOnAction(ae -> controller.changeScreen(MainMenuScreen.class));
        content.getChildren().add(backButton);
        return content;
    }
}
