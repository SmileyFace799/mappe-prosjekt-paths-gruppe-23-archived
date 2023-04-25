package no.ntnu.idata2001.g23.view.screens;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;

/**
 * Represents a screen, which consists of a scene,
 * and a reference to the scene's corresponding controller.
 */
public abstract class GenericScreen {
    private static final String CSS_PATH = "no/ntnu/idata2001/g23/view/";
    public static final int BASE_WIDTH = 3840;
    public static final int BASE_HEIGHT = 2160;

    private final Scene scene;
    private final Pane root;

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
        initializeNodes();
        this.root = makeRoot(); //Makes the root pane

        //Putting "root" in a group makes the scaling work properly.
        //This means "root" isn't actually the root node, but its behavior is otherwise identical,
        //and it can be treated as if it was the actual root node
        this.scene = new Scene(new Group(root), 1280, 720);

        //"root" style class must be added manually, due to the reason above
        root.getStyleClass().add("root");

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
    }

    /**
     * If a screen needs to initialize certain nodes before the root is made,
     * it can override this to do exactly that.
     *
     * <p>This is called once in the constructor before {@link #makeRoot()},
     * and should never be called again.</p>
     */
    protected void initializeNodes() {
        //Does nothing by default, but screens can override this to initialize nodes if they need
    }

    /**
     * Makes the initial root pane.
     *
     * <p>This is called once in the constructor after {@link #initializeNodes()},
     * and should never be called again.</p>
     */
    protected abstract Pane makeRoot();

    /**
     * If a screen can change its state,
     * it can override this to reset itself back to its default state.
     *
     * <p>This can be called multiple times,
     * and is always called when the application changes to this screen.</p>
     */
    public void setDefaultState() {
        //Does nothing by default, but screens can override this to set default parameter values
    }

    /**
     * A listener function that runs whenever this screen is visible & resized.
     */
    public void sizeChangeListener() {
        double newWidth = scene.getWidth();
        double newHeight = scene.getHeight();

        double scaleFactor = Math.min(
                newWidth / BASE_WIDTH,
                newHeight / BASE_HEIGHT
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

    public Pane getRoot() {
        return root;
    }
}
