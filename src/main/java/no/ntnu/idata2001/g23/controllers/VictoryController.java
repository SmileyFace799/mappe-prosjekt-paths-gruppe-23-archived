package no.ntnu.idata2001.g23.controllers;

import no.ntnu.idata2001.g23.intermediary.GameUpdateListener;
import no.ntnu.idata2001.g23.intermediary.GameplayManager;
import no.ntnu.idata2001.g23.intermediary.events.AllGoalsFulfilledEvent;
import no.ntnu.idata2001.g23.intermediary.events.GameUpdateEvent;
import no.ntnu.idata2001.g23.model.fileparsing.CorruptFileException;
import no.ntnu.idata2001.g23.view.PathsApp;
import no.ntnu.idata2001.g23.view.screens.GameplayScreen;
import no.ntnu.idata2001.g23.view.screens.VictoryScreen;

/**
 * A controller for {@link VictoryScreen}.
 */
public class VictoryController extends GenericController implements GameUpdateListener {
    private final VictoryScreen screen;

    /**
     * Makes the controller.
     *
     * @param screen      The screen this controller belongs to
     * @param application The main application
     */
    public VictoryController(VictoryScreen screen, PathsApp application) {
        super(application);
        this.screen = screen;
        GameplayManager.getInstance().addUpdateListener(this);
    }

    /**
     * Restarts the game where the player died.
     */
    public void restartGame() {
        try {
            GameplayManager.getInstance().startGame();
        } catch (CorruptFileException cfe) {
            throw new IllegalStateException("Game could not restart (This should never happen)");
        }
        changeScreen(GameplayScreen.class);
    }

    @Override
    public void onUpdate(GameUpdateEvent event) {
        if (event instanceof AllGoalsFulfilledEvent allGoalsFulfilledEvent) {
            screen.getVictoryText().setText(allGoalsFulfilledEvent.getDescriptiveText());
        }
    }
}
