package no.ntnu.idata2001.g23.view;

import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import no.ntnu.idata2001.g23.controllers.GenericController;
import no.ntnu.idata2001.g23.view.screens.GenericScreen;
import no.ntnu.idata2001.g23.view.screens.MainMenuScreen;

/**
 * The top-level application class.
 */
public class DungeonApp extends Application {
    public static final int BASE_WIDTH = 3840;
    public static final int BASE_HEIGHT = 2160;
    private Stage primaryStage;

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

        ScreenManager.getInstance().makeScreens(this);
        changeScreen(MainMenuScreen.class);
        primaryStage.show();
    }

    /**
     * Changes the application to a different screen.
     *
     * @param screenClass The class of the screen to change to.
     * @see <a href="https://bugs.openjdk.org/browse/JDK-8089209">JavaFX bug when changing scenes while in fullscreen</a>
     */
    public void changeScreen(
            Class<? extends GenericScreen<? extends GenericController>> screenClass
    ) {
        double width = primaryStage.getWidth();
        double height = primaryStage.getHeight();
        boolean fullscreen = primaryStage.isFullScreen();
        primaryStage.setScene(ScreenManager.getInstance().getScreen(screenClass).getScene());
        primaryStage.setFullScreen(fullscreen);
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
    }
}
