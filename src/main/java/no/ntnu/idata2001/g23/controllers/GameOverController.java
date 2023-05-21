package no.ntnu.idata2001.g23.controllers;

import no.ntnu.idata2001.g23.middleman.GameUpdateListener;
import no.ntnu.idata2001.g23.middleman.GameplayManager;
import no.ntnu.idata2001.g23.middleman.events.GameUpdateEvent;
import no.ntnu.idata2001.g23.middleman.events.PlayerDeathEvent;
import no.ntnu.idata2001.g23.model.fileparsing.CorruptFileException;
import no.ntnu.idata2001.g23.view.DungeonApp;
import no.ntnu.idata2001.g23.view.screens.GameOverScreen;
import no.ntnu.idata2001.g23.view.screens.GameplayScreen;

/**
 * A controller for {@link GameOverScreen}.
 */
public class GameOverController extends GenericController implements GameUpdateListener {
    GameOverScreen screen;

    /**
     * Makes the controller.
     *
     * @param screen      The screen this controller belongs to
     * @param application The main application
     */
    public GameOverController(GameOverScreen screen, DungeonApp application) {
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
        if (event instanceof PlayerDeathEvent playerDeathEvent) {
            screen.getDeathCause().setText(playerDeathEvent.getDescriptiveText());
        }
    }
}
