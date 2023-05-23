package no.ntnu.idata2001.g23.controllers;

import no.ntnu.idata2001.g23.view.PathsApp;
import no.ntnu.idata2001.g23.view.screens.GenericScreen;

/**
 * A generic controller for any scene in the application.
 */
public abstract class GenericController {
    protected final PathsApp application;

    protected GenericController(PathsApp application) {
        this.application = application;
    }

    /**
     * Convenience method for easy access to {@link PathsApp#changeScreen(Class)}.
     *
     * @param screenClass The class of the screen to change to
     */
    public void changeScreen(
            Class<? extends GenericScreen> screenClass
    ) {
        application.changeScreen(screenClass);
    }
}
