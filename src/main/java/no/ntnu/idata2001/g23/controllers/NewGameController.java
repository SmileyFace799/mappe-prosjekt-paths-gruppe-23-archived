package no.ntnu.idata2001.g23.controllers;

import java.io.File;
import javafx.stage.DirectoryChooser;
import no.ntnu.idata2001.g23.intermediary.GameplayManager;
import no.ntnu.idata2001.g23.model.fileparsing.CorruptFileException;
import no.ntnu.idata2001.g23.model.fileparsing.DifficultyLoader;
import no.ntnu.idata2001.g23.model.fileparsing.GameFileCollection;
import no.ntnu.idata2001.g23.view.PathsApp;
import no.ntnu.idata2001.g23.view.screens.GameplayScreen;
import no.ntnu.idata2001.g23.view.screens.NewGameScreen;

/**
 * A controller for the new game screen.
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
    public NewGameController(NewGameScreen screen, PathsApp application) {
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
        File chosenFile = gameChooser.showDialog(screen.getWindow());
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
     * which allows the player to choose the difficulty of the story.
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
                                CorruptFileException.Type.INFO_MISSING_DIFFICULTIES)));
                setDifficultySelectErrorMessage(null);
                screen.getContentPane().setCenter(screen.getDifficultySelect());
            } catch (CorruptFileException cfe) {
                setGameSelectErrorMessage("Game files are corrupt: " + cfe.getMessage());
            }
        }
    }

    /**
     * Starts a new game based on the player's selections.
     */
    public void startNewGame() {
        String difficulty = screen.getDifficultyView().getSelectionModel().getSelectedItem();
        if (difficulty == null) {
            setDifficultySelectErrorMessage("Please select a difficulty");
        } else {
            GameplayManager.getInstance().setGame(
                    gameFiles, screen.getPlayerNameInput().getText(), difficulty);
            try {
                GameplayManager.getInstance().startGame();
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
