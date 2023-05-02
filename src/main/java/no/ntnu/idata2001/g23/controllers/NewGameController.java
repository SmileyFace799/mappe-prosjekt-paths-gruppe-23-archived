package no.ntnu.idata2001.g23.controllers;

import java.io.File;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import no.ntnu.idata2001.g23.middleman.GameplayManager;
import no.ntnu.idata2001.g23.model.fileparsing.GameLoader;
import no.ntnu.idata2001.g23.model.itemhandling.FullInventoryException;
import no.ntnu.idata2001.g23.model.fileparsing.CorruptFileException;
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
            setErrorMessage("Please enter a player name");
        } else if (storyPath == null || storyPath.isBlank()) {
            setErrorMessage("Please choose a story");
        } else {
            try {
                GameplayManager.getInstance().startGame(GameLoader.loadGame(playerName, storyPath));
                changeScreen(GameplayScreen.class);
            } catch (CorruptFileException cfe) {
                setErrorMessage("Story file is corrupt: " + cfe.getMessage());
            } catch (FullInventoryException fie) {
                setErrorMessage(fie.getMessage());
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

    /**
     * Shows an error message to the user when the story can't be started.
     *
     * @param errorMessage THe error message to show.
     *                     If this is {@code null}, the error message will be cleared
     */
    public void setErrorMessage(String errorMessage) {
        VBox errorBox = screen.getErrorBox();
        errorBox.getChildren().clear();
        if (errorMessage != null && !errorMessage.isBlank()) {
            Label errorLabel = new Label(errorMessage);
            errorLabel.getStyleClass().add("error-label");
            errorBox.getChildren().add(errorLabel);
        }
    }
}
