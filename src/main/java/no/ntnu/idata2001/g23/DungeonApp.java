package no.ntnu.idata2001.g23;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import no.ntnu.idata2001.g23.view.scenes.MainMenuScene;

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

        MainMenuScene mainMenuScene = new MainMenuScene();
        Scene scene = mainMenuScene.getScene();
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
}
