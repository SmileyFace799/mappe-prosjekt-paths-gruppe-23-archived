package no.ntnu.idata2001.g23;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import no.ntnu.idata2001.g23.controllers.MainMenuController;
import no.ntnu.idata2001.g23.textures.TxLoader;

/**
 * Main class that boots the game.
 */
public class DungeonApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Paths game");
        primaryStage.setMinWidth(720);
        primaryStage.setMinHeight(540);

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-font-size: 32px;");
        root.setBackground(new Background(new BackgroundImage(
                TxLoader.getImage("bg.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, true, false)
        )));
        root.getChildren().add(TxLoader.getImageView(
                "tempTitle.png", 720, 0, true));
        root.getChildren().add(new Rectangle(0, 85));
        Button playButton = new Button("Play Game");
        root.getChildren().add(playButton);
        Button settingsButton = new Button("Settings");
        root.getChildren().add(settingsButton);
        Button quitButton = new Button("Quit Game");
        quitButton.setOnAction(ae -> MainMenuController.getInstance().onQuitButtonClick());
        root.getChildren().add(quitButton);

        Scene scene = new Scene(root, 1280, 720);
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
}
