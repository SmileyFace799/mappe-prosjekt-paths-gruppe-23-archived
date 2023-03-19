package no.ntnu.idata2001.g23;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import no.ntnu.idata2001.g23.javafx.MenuContentFactory;

/**
 * Main class that boots the game.
 */
public class DungeonApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static final int BASE_WIDTH = 3840;
    public static final int BASE_HEIGHT = 2160;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Paths game");
        primaryStage.setMinWidth(720);
        primaryStage.setMinHeight(540);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (KeyCode.F11.equals(event.getCode())) {
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
            }
        });

        StackPane root = new StackPane();
        MenuContentFactory menuContentFactory = new MenuContentFactory(root);
        root.getChildren().add(menuContentFactory.getContent("mainMenu"));

        Scene scene = new Scene(new Group(root), 1280, 720);
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);

        Runnable sizeChangeListener = () -> {
            double newWidth = scene.getWidth();
            double newHeight = scene.getHeight();

            double scaleFactor = Math.min(
                    newWidth / BASE_WIDTH,
                    newHeight / BASE_HEIGHT
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
