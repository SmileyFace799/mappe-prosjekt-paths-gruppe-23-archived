package no.ntnu.idata2001.g23.controllers;

import java.util.List;
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
    public NewGameController(NewGameScreen screen, DungeonApp application) {
        super(application);
        this.screen = screen;
    }

    private final NewGameScreen screen;
    private String storyFileName = "testStory"; //TODO: Choose file prompt for this

    /**
     * Makes a new game with the selected player details, story & goals.
     *
     * @param playerName The name of the player in this game
     */
    public void startNewGame(String playerName) {
        if (playerName == null || playerName.isBlank()) {
            screen.setErrorMessage("Please enter a player name");
        } else {
            try {
                GameplayManager.getInstance().startGame(new Game(
                        new Player.PlayerBuilder(playerName, 30).build(),
                        StoryLoader.loadStory(storyFileName),
                        List.of()
                ));
                changeScreen(GameplayScreen.class);
            } catch (CorruptFileException cfe) {
                screen.setErrorMessage("Story file is corrupt: " + cfe.getMessage());
            }
        }
    }
}
