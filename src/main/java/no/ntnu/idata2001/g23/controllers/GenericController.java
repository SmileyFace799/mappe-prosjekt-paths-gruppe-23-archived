package no.ntnu.idata2001.g23.controllers;

import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.screens.GenericScreen;

/**
 * A generic controller for any scene in the application.
 */
public abstract class GenericController {
    protected final DungeonApp application;

    protected GenericController(DungeonApp application) {
        this.application = application;
    }

    /**
     * Convenience method for easy access to {@link DungeonApp#changeScreen(Class)}.
     *
     * @param screenClass The class of the screen to change to
     */
    public void changeScreen(
            Class<? extends GenericScreen> screenClass
    ) {
        application.changeScreen(screenClass);
    }
}
