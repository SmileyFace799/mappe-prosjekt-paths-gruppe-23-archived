package no.ntnu.idata2001.g23.view.screens;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import no.ntnu.idata2001.g23.controllers.GenericController;
import no.ntnu.idata2001.g23.view.DungeonApp;

/**
 * Represents a screen, which consists of a scene,
 * and a reference to the scene's corresponding controller.
 *
 * @param <C> The controller class for this screen
 */
public abstract class GenericScreen<C extends GenericController> {
    private static final String CSS_PATH = "no/ntnu/idata2001/g23/view/";

    protected final C controller;
    private Scene scene;

    /**
     * Constructs a screen.
     *
     * @param controller The screen's controller.
     * @param cssFiles   Any additional {@code .css}-files to apply to the scene.<br/>
     *                   <b>Note: </b> {@code global.css} will always be applied to the scene
     */
    protected GenericScreen(C controller, String... cssFiles) {
        this.controller = controller;
        initialize(makeRoot(), cssFiles);
    }

    protected abstract Pane makeRoot();

    /**
     * A listener function that runs whenever this screen is visible & resized.
     */
    private void sizeChangeListener(Pane root) {
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

    /**
     * Initializes the screen and makes it's scene.
     * Should always be called at the end of the constructor.
     *
     * @param root     The root node for the scene. If this is not a {@link Pane},
     *                 it will be put in the center of an otherwise empty {@link BorderPane}.
     *                 This is done so it can be conveniently resized
     * @param cssFiles Any additional {@code .css}-files to apply to the scene.<br/>
     *                 <b>Note: </b> {@code global.css} will always be applied to the scene
     */
    private void initialize(Pane root, String... cssFiles) {
        root.getStyleClass().add("root");
        //Putting "root" in a group makes the scaling work properly.
        //This means "root" isn't actually the root node, but it's behavior is otherwise identical,
        //and it can be treated as if it was the actual root node
        this.scene = new Scene(new Group(root), 1280, 720);

        scene.getStylesheets().add(CSS_PATH + "global.css");
        for (String cssFile : cssFiles) {
            scene.getStylesheets().add(CSS_PATH + cssFile);
        }

        scene.widthProperty().addListener((observableValue, oldValue, newValue) ->
                sizeChangeListener(root));
        scene.heightProperty().addListener((observableValue, oldValue, newValue) ->
                sizeChangeListener(root));

        sizeChangeListener(root); //Updates scene size to the right screen size upon creation
    }

    public Scene getScene() {
        return scene;
    }
}
