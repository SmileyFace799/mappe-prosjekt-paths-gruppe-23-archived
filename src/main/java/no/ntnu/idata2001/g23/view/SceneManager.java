package no.ntnu.idata2001.g23.view;

import java.util.Collection;
import java.util.Set;
import no.ntnu.idata2001.g23.DungeonApp;
import no.ntnu.idata2001.g23.controllers.GenericController;
import no.ntnu.idata2001.g23.controllers.MainMenuController;
import no.ntnu.idata2001.g23.controllers.NewGameController;
import no.ntnu.idata2001.g23.controllers.PlayGameController;
import no.ntnu.idata2001.g23.controllers.SettingsController;
import no.ntnu.idata2001.g23.exceptions.unchecked.ElementNotFoundException;
import no.ntnu.idata2001.g23.view.scenes.GenericScene;
import no.ntnu.idata2001.g23.view.scenes.MainMenuScene;
import no.ntnu.idata2001.g23.view.scenes.NewGameScene;
import no.ntnu.idata2001.g23.view.scenes.PlayGameScene;
import no.ntnu.idata2001.g23.view.scenes.SettingsScene;

/**
 * Creates & manages all the scenes contained within the application.
 */
public class SceneManager {
    private static SceneManager instance;
    private Collection<GenericScene<? extends GenericController>> sceneCollection;

    private SceneManager() {
    }

    /**
     * Singleton.
     *
     * @return Singleton instance
     */
    public static synchronized SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    /**
     * Makes all the scenes used within the application.
     *
     * @param application The application itself, which is passed to the controller of each scene
     */
    public void makeScenes(DungeonApp application) {
        sceneCollection = Set.of(
                new MainMenuScene(new MainMenuController(application)),
                new SettingsScene(new SettingsController(application)),
                new PlayGameScene(new PlayGameController(application)),
                new NewGameScene(new NewGameController(application))
        );
    }

    /**
     * Gets a scene given it's class.
     * The returned scene will be of the same type as the given class.
     *
     * @param sceneClass The class of the scene to get
     * @param <S> The scene type
     * @return The scene of the type that belongs to the provided class
     */
    public <S extends GenericScene<? extends GenericController>> S getScene(
            Class<S> sceneClass
    ) {
        return sceneClass.cast(sceneCollection
                .stream()
                .filter(scene -> scene.getClass() == sceneClass)
                .findFirst()
                .orElseThrow(() -> new ElementNotFoundException("This scene hasn't been made yet")
                ));
    }
}
