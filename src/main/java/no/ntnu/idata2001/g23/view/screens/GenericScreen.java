package no.ntnu.idata2001.g23.view.screens;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import no.ntnu.idata2001.g23.view.DungeonApp;

/**
 * Represents a screen, which consists of a scene,
 * and a reference to the scene's corresponding controller.
 */
public abstract class GenericScreen {
    private static final String CSS_PATH = "no/ntnu/idata2001/g23/view/";

    private final Scene scene;
    private Pane root;

    /**
     * Constructs a screen and makes its scene.
     * The scene's root will be the {@link Pane} returned by {@link #makeRoot()}.<br/>
     * <b>Note:</b> This does not make a controller,
     * extensions of this class are expected to make their own.
     *
     * @param cssFiles   Any additional {@code .css}-files to apply to the scene.<br/>
     *                   <b>Note: </b> {@code global.css} will always be applied to the scene
     */
    protected GenericScreen(String... cssFiles) {
        setDefaultParams(); //Sets default screen parameters
        this.root = makeRoot(); //Makes the root pane, in its default state
        root.getStyleClass().add("root");
        //Putting "root" in a group makes the scaling work properly.
        //This means "root" isn't actually the root node, but it's behavior is otherwise identical,
        //and it can be treated as if it was the actual root node
        this.scene = new Scene(new Group(root), 1280, 720);

        //Adds .css
        scene.getStylesheets().add(CSS_PATH + "global.css");
        for (String cssFile : cssFiles) {
            scene.getStylesheets().add(CSS_PATH + cssFile);
        }

        //Makes the root pane scale upon window resize
        scene.widthProperty().addListener((observableValue, oldValue, newValue) ->
                sizeChangeListener());
        scene.heightProperty().addListener((observableValue, oldValue, newValue) ->
                sizeChangeListener());

        sizeChangeListener(); //Updates scene size to the right screen size upon creation
    }

    /**
     * Makes the screen's root pane.
     *
     * @return The root pane made
     */
    protected abstract Pane makeRoot();

    /**
     * Screen can optionally override this to set default parameters. Does nothing by default.
     */
    protected void setDefaultParams() {
        //Does nothing by default, but screens can override this to set default parameter values
    }

    /**
     * Resets the state of the screen back the default state, and updates it.
     */
    public void resetToDefault() {
        setDefaultParams();
        updateRoot();
    }

    /**
     * Updates the screen's root pane by calling {@link #makeRoot()} again.
     * This should be called if changes happen to the screen, and it needs to be re-rendered.
     */
    public void updateRoot() {
        this.root = makeRoot();
        root.getStyleClass().add("root");
        scene.setRoot(new Group(root));
        sizeChangeListener(); //Update the root size to fit the screen again
    }

    /**
     * A listener function that runs whenever this screen is visible & resized.
     */
    private void sizeChangeListener() {
        double newWidth = scene.getWidth();
        double newHeight = scene.getHeight();

        double scaleFactor = Math.min(
                newWidth / DungeonApp.BASE_WIDTH,
                newHeight / DungeonApp.BASE_HEIGHT
        );


        Scale scale = new Scale(scaleFactor, scaleFactor);
        scale.setPivotX(0);
        scale.setPivotY(0);
        scene.getRoot().getTransforms().setAll(scale);

        root.setPrefWidth(newWidth / scaleFactor);
        root.setPrefHeight(newHeight / scaleFactor);
    }

    public Scene getScene() {
        return scene;
    }
}
