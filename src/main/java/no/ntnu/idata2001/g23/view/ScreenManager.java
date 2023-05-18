package no.ntnu.idata2001.g23.view;

import java.util.Collection;
import java.util.Set;
import no.ntnu.idata2001.g23.view.screens.GameplayScreen;
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
    private Collection<GenericScreen> screenCollection;

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
    public void makeMenuScreens(DungeonApp application) {
        screenCollection = Set.of(
                new MainMenuScreen(application),
                new SettingsScreen(application),
                new PlayGameScreen(application),
                new NewGameScreen(application),
                new GameplayScreen(application)
        );
    }

    /**
     * Gets a screen given it's class.
     * The returned screen will be of the same type as the given class.
     *
     * @param screenClass The class of the screen to get
     * @param <S>        The screen type
     * @return The screen of the type that belongs to the provided class
     * @throws IllegalArgumentException If the screen class passed has not been initialized yet
     */
    public <S extends GenericScreen> S getScreen(
            Class<S> screenClass
    ) {
        return screenClass.cast(screenCollection
                .stream()
                .filter(screen -> screen.getClass() == screenClass)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("This screen hasn't been made yet")
                ));
    }
}
