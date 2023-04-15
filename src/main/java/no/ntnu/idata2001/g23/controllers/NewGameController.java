package no.ntnu.idata2001.g23.controllers;

import java.util.List;
import no.ntnu.idata2001.g23.middleman.GameplayManager;
import no.ntnu.idata2001.g23.model.Game;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.story.CorruptFileException;
import no.ntnu.idata2001.g23.model.story.StoryLoader;
import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.screens.GameplayScreen;

/**
 * A controller for the new game scene.
 */
public class NewGameController extends GenericController {
    public NewGameController(DungeonApp application) {
        super(application);
    }

    private String storyFileName;

    /**
     * Makes a new game with the selected player details, story & goals.
     *
     * @param playerName The name of the player in this game
     */
    public void startNewGame(String playerName) {
        try {
            GameplayManager.getInstance().startGame(new Game(
                    new Player.PlayerBuilder(playerName, 30).build(),
                    StoryLoader.loadStory("testStory"),
                    List.of()
                    ));
            changeScreen(GameplayScreen.class);
        } catch (CorruptFileException cfe) {
            //TODO: Tell user the file is corrupted
            cfe.printStackTrace();
        }
    }
}
