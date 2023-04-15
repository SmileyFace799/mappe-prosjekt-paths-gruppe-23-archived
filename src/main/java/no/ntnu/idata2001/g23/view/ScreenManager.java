package no.ntnu.idata2001.g23.view;

import java.util.Collection;
import java.util.Set;
import no.ntnu.idata2001.g23.controllers.GenericController;
import no.ntnu.idata2001.g23.controllers.MainMenuController;
import no.ntnu.idata2001.g23.controllers.NewGameController;
import no.ntnu.idata2001.g23.controllers.PlayGameController;
import no.ntnu.idata2001.g23.controllers.SettingsController;
import no.ntnu.idata2001.g23.exceptions.unchecked.ElementNotFoundException;
import no.ntnu.idata2001.g23.view.screens.GenericScreen;
import no.ntnu.idata2001.g23.view.screens.MainMenuScreen;
import no.ntnu.idata2001.g23.view.screens.NewGameScreen;
import no.ntnu.idata2001.g23.view.screens.PlayGameScreen;
import no.ntnu.idata2001.g23.view.screens.SettingsScreen;

/**
 * Creates & manages all the screens contained within the application.
 */
public class ScreenManager {
    private static ScreenManager instance;
    private Collection<GenericScreen<? extends GenericController>> screenCollection;

    private ScreenManager() {
    }

    /**
     * Singleton.
     *
     * @return Singleton instance
     */
    public static synchronized ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    /**
     * Makes all the screens used within the application.
     *
     * @param application The application itself, which is passed to the controller of each screen
     */
    public void makeScreens(DungeonApp application) {
        screenCollection = Set.of(
                new MainMenuScreen(new MainMenuController(application)),
                new SettingsScreen(new SettingsController(application)),
                new PlayGameScreen(new PlayGameController(application)),
                new NewGameScreen(new NewGameController(application))
        );
    }

    /**
     * Gets a screen given it's class.
     * The returned screen will be of the same type as the given class.
     * {@link #makeScreens(DungeonApp)} should always be called before this.
     *
     * @param screenClass The class of the screen to get
     * @param <S>        The screen type
     * @return The screen of the type that belongs to the provided class
     * @throws ElementNotFoundException If the screen class passed was not initialized in
     *                                  {@link #makeScreens(DungeonApp)}
     */
    public <S extends GenericScreen<? extends GenericController>> S getScreen(
            Class<S> screenClass
    ) {
        return screenClass.cast(screenCollection
                .stream()
                .filter(screen -> screen.getClass() == screenClass)
                .findFirst()
                .orElseThrow(() -> new ElementNotFoundException("This screen hasn't been made yet")
                ));
    }
}
