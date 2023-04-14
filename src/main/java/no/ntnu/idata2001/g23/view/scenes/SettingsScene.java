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
import no.ntnu.idata2001.g23.controllers.SettingsController;
import no.ntnu.idata2001.g23.view.textures.TxLoader;

/**
 * The settings scene, where the user can customize various application & game settings.
 */
public class SettingsScene extends GenericScene<SettingsController> {
    public SettingsScene(SettingsController controller) {
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

        Label settingsTitle = new Label("Settings");
        settingsTitle.getStyleClass().add("header");
        content.getChildren().add(settingsTitle);

        content.getChildren().add(new Rectangle(0, 200));

        Button backButton = new Button("Go Back");
        backButton.setOnAction(ae -> controller.changeScene(MainMenuScene.class));
        content.getChildren().add(backButton);
        return content;
    }
}
