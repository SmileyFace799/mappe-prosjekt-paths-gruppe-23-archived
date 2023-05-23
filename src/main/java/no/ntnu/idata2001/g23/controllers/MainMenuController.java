package no.ntnu.idata2001.g23.controllers;

import javafx.application.Platform;
import no.ntnu.idata2001.g23.view.PathsApp;

/**
 * A controller for the main menu scene.
 */
public class MainMenuController extends GenericController {
    /**
     * Makes a main menu controller.
     *
     * @param application The applifaction itself
     */
    public MainMenuController(PathsApp application) {
        super(application);
    }

    /**
     * Closes the application.
     */
    public void closeApplication() {
        Platform.exit();
    }
}
