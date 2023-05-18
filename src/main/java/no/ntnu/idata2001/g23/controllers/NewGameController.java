package no.ntnu.idata2001.g23.controllers;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import javafx.stage.DirectoryChooser;
import no.ntnu.idata2001.g23.middleman.GameplayManager;
import no.ntnu.idata2001.g23.model.Game;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.entities.enemies.Enemy;
import no.ntnu.idata2001.g23.model.fileparsing.CorruptFileException;
import no.ntnu.idata2001.g23.model.fileparsing.DifficultyLoader;
import no.ntnu.idata2001.g23.model.fileparsing.EnemyLoader;
import no.ntnu.idata2001.g23.model.fileparsing.GameFileCollection;
import no.ntnu.idata2001.g23.model.fileparsing.GoalLoader;
import no.ntnu.idata2001.g23.model.fileparsing.ItemLoader;
import no.ntnu.idata2001.g23.model.fileparsing.StoryLoader;
import no.ntnu.idata2001.g23.model.goals.Goal;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.misc.Provider;
import no.ntnu.idata2001.g23.model.story.Story;
import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.screens.GameplayScreen;
import no.ntnu.idata2001.g23.view.screens.NewGameScreen;

/**
 * A controller for the new game scene.
 */
public class NewGameController extends GenericController {
    private final NewGameScreen screen;
    private final DirectoryChooser gameChooser;
    private GameFileCollection gameFiles;

    /**
     * Makes the controller, and initializes a gameChooser.
     *
     * @param screen      The screen this controller belongs to
     * @param application The main application
     */
    public NewGameController(NewGameScreen screen, DungeonApp application) {
        super(application);
        this.screen = screen;
        this.gameChooser = new DirectoryChooser();
        gameChooser.setTitle("Select a game");
        gameChooser.setInitialDirectory(new File("./"));
    }

    /**
     * Opens the gameChooser, allowing the user to select a game.
     */
    public void browseGame() {
        File chosenFile = gameChooser.showDialog(screen.getScene().getWindow());
        if (chosenFile != null) {
            screen.getGamePathInput().setText(chosenFile.getPath());
        }
    }

    /**
     * Shows the game selection that allows the player to select a game to play.
     */
    public void showGameSelection() {
        setGameSelectErrorMessage(null);
        screen.getContentPane().setCenter(screen.getGameSelect());
    }

    /**
     * Shows the difficulty selection after the player has selected a game to play,
     * which allows the player to choose the difficulty of the story..
     */
    public void showDifficultySelection() {
        String playerName = screen.getPlayerNameInput().getText();
        String gamePath = screen.getGamePathInput().getText();
        if (playerName == null || playerName.isBlank()) {
            setGameSelectErrorMessage("Please enter a player name");
        } else if (gamePath == null || gamePath.isBlank()) {
            setGameSelectErrorMessage("Please choose a game");
        } else {
            try {
                screen.getDifficultyView().getItems().clear();
                gameFiles = new GameFileCollection(gamePath);
                screen.getDifficultyView().getItems().addAll(DifficultyLoader
                        .loadDifficulties(gameFiles.getPathRequired(".difficulties",
                                CorruptFileException.Type.NO_DIFFICULTIES)));
                setDifficultySelectErrorMessage(null);
                screen.getContentPane().setCenter(screen.getDifficultySelect());
            } catch (CorruptFileException cfe) {
                setGameSelectErrorMessage("Game files are corrupt: " + cfe.getMessage());
            }
        }
    }

    /**
     * Makes a new {@link Game} object from the game path & difficulty specified by the player.
     *
     * @param difficulty The difficulty of the game
     * @return The newly made game
     * @throws CorruptFileException If the game cannot be made due to one or more corrupt files
     */
    private Game makeNewGame(String difficulty) throws CorruptFileException {
        //Parse items, create itemProvider
        Path itemsPath = gameFiles.getPath(".items");
        Provider<Item> itemProvider = itemsPath != null
                ? ItemLoader.loadItems(itemsPath)
                : null;

        //Parse enemies, create enemyProvider, using itemProvider
        Path enemiesPath = gameFiles.getPath(".enemies");
        Provider<Enemy> enemyProvider = enemiesPath != null
                ? new EnemyLoader(itemProvider).loadEnemies(enemiesPath)
                : null;

        System.out.println(enemyProvider.provide("Test Enemy").getName());

        //parse & create story, using itemProvider
        Path storyPath = gameFiles.getPathRequired(".paths",
                CorruptFileException.Type.NO_STORY);
        Story story = new StoryLoader(itemProvider).loadStory(storyPath);

        //parse & create goals, using itemProvider
        Path goalsPath = gameFiles.getPathRequired(".goals",
                CorruptFileException.Type.NO_GOALS);
        List<Goal> goals = new GoalLoader(itemProvider, difficulty).loadGoals(goalsPath);

        //Create game, using story & goals
        Game game = new Game(new Player.PlayerBuilder(screen.getPlayerNameInput().getText(), 30)
                .build(),
                story, goals);

        for (String itemName : new String[]{"Big Sword UwU", "Usable Test", "Usable Test"}) {
            game.getPlayer()
                    .getInventory()
                    .addItem(itemProvider.provide(itemName));
        }
        return game;
    }

    /**
     * Starts a new game based on the player's selections.
     */
    public void startNewGame() {
        String difficulty = screen.getDifficultyView().getSelectionModel().getSelectedItem();
        if (difficulty == null) {
            setDifficultySelectErrorMessage("Please select a difficulty");
        } else {
            try {
                GameplayManager.getInstance().startGame(makeNewGame(difficulty));
                changeScreen(GameplayScreen.class);
            } catch (CorruptFileException cfe) {
                setDifficultySelectErrorMessage("Game files are corrupt: " + cfe.getMessage());
            }
        }
    }

    /**
     * Shows an error message to the user when the game can't be started.
     * This message is shown on the game selection screen
     *
     * @param errorMessage THe error message to show.
     *                     If this is {@code null}, the error message will be cleared
     */
    public void setGameSelectErrorMessage(String errorMessage) {
        screen.getGameSelectErrorText().setText(errorMessage);
    }

    /**
     * Shows an error message to the user when the game can't be started.
     * This message is shown on the difficulty selection screen
     *
     * @param errorMessage THe error message to show.
     *                     If this is {@code null}, the error message will be cleared
     */
    public void setDifficultySelectErrorMessage(String errorMessage) {
        screen.getDifficultySelectErrorText().setText(errorMessage);
    }
}
