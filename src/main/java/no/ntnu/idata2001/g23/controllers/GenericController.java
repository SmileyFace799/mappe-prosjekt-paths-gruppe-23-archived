package no.ntnu.idata2001.g23.controllers;

import no.ntnu.idata2001.g23.DungeonApp;
import no.ntnu.idata2001.g23.view.scenes.GenericScene;

/**
 * A generic controller for any scene in the application.
 */
public class GenericController {
    protected final DungeonApp application;

    protected GenericController(DungeonApp application) {
        this.application = application;
    }

    public void changeScene(Class<? extends GenericScene<? extends GenericController>> sceneClass) {
        application.changeScene(sceneClass);
    }
}
