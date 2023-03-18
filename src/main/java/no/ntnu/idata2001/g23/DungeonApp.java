package no.ntnu.idata2001.g23;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
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
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import no.ntnu.idata2001.g23.textures.TxLoader;

/**
 * Main class that boots the game.
 */
public class DungeonApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private static final Integer[] BASE_RESOLUTION = new Integer[]{3840, 2160};

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Paths game");
        primaryStage.setMinWidth(720);
        primaryStage.setMinHeight(540);
        primaryStage.setFullScreenExitHint("");

        VBox root = new VBox(60);

        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-font-size: 72px;");
        root.setBackground(new Background(new BackgroundImage(
                TxLoader.getImage("bg.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, true, false)
        )));
        root.getChildren().add(TxLoader.getImageView(
                "tempTitle.png", 2000, 0, true));
        root.getChildren().add(new Rectangle(0, 200));

        Button playButton = new Button("Play Game");
        root.getChildren().add(playButton);

        Button settingsButton = new Button("Settings");
        root.getChildren().add(settingsButton);

        Button quitButton = new Button("Quit Game");
        quitButton.setOnAction(ae -> Platform.exit());
        root.getChildren().add(quitButton);

        Scene scene = new Scene(new Group(root), 1280, 720);
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);

        Runnable sizeChangeListener = () -> {
            double newWidth = scene.getWidth();
            double newHeight = scene.getHeight();

            double scaleFactor = Math.min(
                    newWidth / BASE_RESOLUTION[0],
                    newHeight / BASE_RESOLUTION[1]
            );


            Scale scale = new Scale(scaleFactor, scaleFactor);
            scale.setPivotX(0);
            scale.setPivotY(0);
            scene.getRoot().getTransforms().setAll(scale);

            root.setPrefWidth(newWidth  / scaleFactor);
            root.setPrefHeight(newHeight / scaleFactor);
        };

        scene.widthProperty().addListener((observableValue, oldValue, newValue) ->
                sizeChangeListener.run());
        scene.heightProperty().addListener((observableValue, oldValue, newValue) ->
                sizeChangeListener.run());
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
}
