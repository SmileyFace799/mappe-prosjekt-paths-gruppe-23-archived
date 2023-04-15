package no.ntnu.idata2001.g23.controllers;

import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.screens.GenericScreen;

/**
 * A generic controller for any scene in the application.
 */
public class GenericController {
    protected final DungeonApp application;

    protected GenericController(DungeonApp application) {
        this.application = application;
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
        application.changeScreen(screenClass);
    }
}
