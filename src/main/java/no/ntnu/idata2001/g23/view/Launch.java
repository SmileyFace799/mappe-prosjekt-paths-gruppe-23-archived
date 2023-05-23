package no.ntnu.idata2001.g23.view;

import javafx.application.Application;

/**
 * Launches the application.
 */
public class Launch {
    private Launch() {
        throw new IllegalStateException("Do not instantiate this class pls :)");
    }

    /**
     * Launches the application.
     *
     * @param args jvm args
     */
    public static void launch(String[] args) {
        Application.launch(PathsApp.class, args);
    }
}
