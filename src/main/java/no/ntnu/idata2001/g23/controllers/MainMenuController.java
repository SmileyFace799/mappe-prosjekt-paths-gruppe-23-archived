package no.ntnu.idata2001.g23.controllers;

/**
 * Controller that handles user input in the main menu.
 */
public class MainMenuController {
    private static MainMenuController instance;

    private MainMenuController() {}

    /**
     * Singleton.
     *
     * @return Singleton instance.
     */
    public static synchronized MainMenuController getInstance() {
        if (instance == null) {
            instance = new MainMenuController();
        }
        return instance;
    }
}
