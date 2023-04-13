package no.ntnu.idata2001.g23.view.scenes;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import no.ntnu.idata2001.g23.view.textures.TxLoader;

public class MainMenuScene extends GenericScene {

    @Override
    protected Pane makeRoot() {
        VBox content = new VBox(60);

        content.setAlignment(Pos.CENTER);
        //content.setStyle(Css.CONTENT_FONT_STYLE);
        content.setBackground(new Background(new BackgroundImage(
                TxLoader.getImage("bg.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, true, false)
        )));
        content.getChildren().add(TxLoader.getImageView(
                "tempTitle.png", 2000, 0, true));

        content.getChildren().add(new Rectangle(0, 200));

        Button playButton = new Button("Play Game");
        //TODO: Fix scene change
        //playButton.setOnAction(ae -> Transitions.contentTransition(
        //        root, content, getPlayGame()));
        content.getChildren().add(playButton);

        Button settingsButton = new Button("Settings");
        //TODO: FIx scene change
        //settingsButton.setOnAction(ae -> Transitions.contentTransition(
        //        root, content, getSettings()));
        content.getChildren().add(settingsButton);

        Button credits = new Button("Credits");
        credits.setDisable(true);
        content.getChildren().add(credits);

        Button quitButton = new Button("Quit Game");
        quitButton.setOnAction(ae -> Platform.exit());
        content.getChildren().add(quitButton);
        return content;
    }
}
