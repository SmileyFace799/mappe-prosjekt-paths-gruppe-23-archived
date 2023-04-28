package no.ntnu.idata2001.g23.controllers;

import java.io.File;
import javafx.stage.DirectoryChooser;
import no.ntnu.idata2001.g23.middleman.GameplayManager;
import no.ntnu.idata2001.g23.model.GameLoader;
import no.ntnu.idata2001.g23.model.itemhandling.FullInventoryException;
import no.ntnu.idata2001.g23.model.story.CorruptFileException;
import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.screens.GameplayScreen;
import no.ntnu.idata2001.g23.view.screens.NewGameScreen;

/**
 * A controller for the new game scene.
 */
public class NewGameController extends GenericController {
    /**
     * Makes the controller, and initializes a storyChooser.
     *
     * @param screen The screen this controller belongs to
     * @param application The main application
     */
    public NewGameController(NewGameScreen screen, DungeonApp application) {
        super(application);
        this.screen = screen;
        this.storyChooser = new DirectoryChooser();
        storyChooser.setTitle("Select a story");
        storyChooser.setInitialDirectory(new File("./"));
    }

    private final NewGameScreen screen;
    private final DirectoryChooser storyChooser;

    /**
     * Makes a new game with the selected player details, story & goals.
     */
    public void startNewGame() {
        String playerName = screen.getPlayerNameInput().getText();
        String storyPath = screen.getStoryPathInput().getText();
        if (playerName == null || playerName.isBlank()) {
            screen.setErrorMessage("Please enter a player name");
        } else if (storyPath == null || storyPath.isBlank()) {
            screen.setErrorMessage("Please choose a story");
        } else {
            try {
                GameplayManager.getInstance().startGame(GameLoader.loadGame(playerName, storyPath));
                changeScreen(GameplayScreen.class);
            } catch (CorruptFileException cfe) {
                screen.setErrorMessage("Story file is corrupt: " + cfe.getMessage());
            } catch (FullInventoryException fie) {
                screen.setErrorMessage(fie.getMessage());
            }
        }
    }

    /**
     * Opens the storyChooser, allowing the user to select a story.
     */
    public void browseStory() {
        File chosenFile = storyChooser.showDialog(screen.getScene().getWindow());
        if (chosenFile != null) {
            screen.getStoryPathInput().setText(chosenFile.getPath());
        }
    }
}
