package no.ntnu.idata2001.g23;

import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import no.ntnu.idata2001.g23.controllers.GenericController;
import no.ntnu.idata2001.g23.view.SceneManager;
import no.ntnu.idata2001.g23.view.scenes.GenericScene;
import no.ntnu.idata2001.g23.view.scenes.MainMenuScene;

/**
 * Main class that boots the game.
 */
public class DungeonApp extends Application {
    public static final int BASE_WIDTH = 3840;
    public static final int BASE_HEIGHT = 2160;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Paths game");
        primaryStage.setMinWidth(720);
        primaryStage.setMinHeight(540);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (KeyCode.F11.equals(event.getCode())) {
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
            }
        });

        SceneManager.getInstance().makeScenes(this);
        changeScene(MainMenuScene.class);
        primaryStage.show();
    }

    /**
     * Changes the application to a different scene.
     *
     * @param sceneClass The class of the scene to change to.
     * @see <a href="https://bugs.openjdk.org/browse/JDK-8089209">JavaFX bug when changing scenes while in fullscreen</a>
     */
    public void changeScene(Class<? extends GenericScene<? extends GenericController>> sceneClass) {
        double width = primaryStage.getWidth();
        double height = primaryStage.getHeight();
        boolean fullscreen = primaryStage.isFullScreen();
        primaryStage.setScene(SceneManager.getInstance().getScene(sceneClass).getScene());
        primaryStage.setFullScreen(fullscreen);
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
    }
}
