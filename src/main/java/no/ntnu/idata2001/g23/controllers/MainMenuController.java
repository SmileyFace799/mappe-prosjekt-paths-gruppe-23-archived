package no.ntnu.idata2001.g23.controllers;

import javafx.application.Platform;

public class MainMenuController {
    private static MainMenuController instance;

    private MainMenuController() {}

    public static synchronized MainMenuController getInstance() {
        if (instance == null) {
            instance = new MainMenuController();
        }
        return instance;
    }

    public void onQuitButtonClick() {
        Platform.exit();
    }
}
