package no.ntnu.idata2001.g23.view.screens;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import no.ntnu.idata2001.g23.controllers.MainMenuController;
import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.textures.ImageLoader;

/**
 * The main menu screen.
 */
public class MainMenuScreen extends GenericScreen {
    private final MainMenuController controller;

    public MainMenuScreen(DungeonApp application) {
        super();
        controller = new MainMenuController(application);
    }

    @Override
    protected Pane makeRoot() {
        VBox content = new VBox(60);
        content.getChildren().add(ImageLoader.getImageView(
                ImageLoader.getImageResource("title.png"), 0, 500, true));

        content.getChildren().add(new Rectangle(0, 200));

        Button playButton = new Button("Play Game");
        playButton.setOnAction(ae -> controller.changeScreen(PlayGameScreen.class));
        content.getChildren().add(playButton);

        Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(ae -> controller.changeScreen(SettingsScreen.class));
        content.getChildren().add(settingsButton);

        Button credits = new Button("Credits");
        credits.setDisable(true);
        content.getChildren().add(credits);

        Button quitButton = new Button("Quit Game");
        quitButton.setOnAction(ae -> controller.closeApplication());
        content.getChildren().add(quitButton);
        return content;
    }
}
