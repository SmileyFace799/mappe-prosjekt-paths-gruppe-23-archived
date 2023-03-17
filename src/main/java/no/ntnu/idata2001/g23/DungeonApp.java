package no.ntnu.idata2001.g23;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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

        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundImage(
                TxLoader.getTx("bg.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, true, false)
        )));

        Scene scene = new Scene(root, 1280, 720);
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
}
