package no.ntnu.idata2001.g23.controllers;

import javafx.application.Platform;
import no.ntnu.idata2001.g23.DungeonApp;

/**
 * A controller for the main menu scene.
 */
public class MainMenuController extends GenericController {
    public MainMenuController(DungeonApp application) {
        super(application);
    }

    public void closeApplication() {
        Platform.exit();
    }
}
