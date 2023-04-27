package no.ntnu.idata2001.g23.controllers;

import java.io.File;
import java.util.List;
import javafx.stage.FileChooser;
import no.ntnu.idata2001.g23.middleman.GameplayManager;
import no.ntnu.idata2001.g23.model.Game;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.story.CorruptFileException;
import no.ntnu.idata2001.g23.model.story.StoryLoader;
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
        this.storyChooser = new FileChooser();
        storyChooser.setTitle("Select a story");
        storyChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Story files", "*.paths")
        );
    }

    private final NewGameScreen screen;
    private final FileChooser storyChooser;

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
                GameplayManager.getInstance().startGame(new Game(
                        new Player.PlayerBuilder(playerName, 30).build(),
                        StoryLoader.loadStory(storyPath),
                        List.of()
                ));
                changeScreen(GameplayScreen.class);
            } catch (CorruptFileException cfe) {
                screen.setErrorMessage("Story file is corrupt: " + cfe.getMessage());
            }
        }
    }

    /**
     * Opens the storyChooser, allowing the user to select a story.
     */
    public void browseStory() {
        File chosenFile = storyChooser.showOpenDialog(screen.getScene().getWindow());
        if (chosenFile != null) {
            screen.getStoryPathInput().setText(chosenFile.getPath());
        }
    }
}
