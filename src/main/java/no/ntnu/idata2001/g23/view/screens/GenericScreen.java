package no.ntnu.idata2001.g23.view.screens;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;

/**
 * Represents a screen, which consists of a scene,
 * and a reference to the scene's corresponding controller.
 */
public abstract class GenericScreen extends Scene {
    public static final int BASE_WIDTH = 3840;
    public static final int BASE_HEIGHT = 2160;

    protected static final int HORIZONTAL_SPACING = 60;
    protected static final int VERTICAL_SPACING = 60;

    private static final int DEFAULT_WINDOW_WIDTH = 1280;
    private static final int DEFAULT_WINDOW_HEIGHT = 720;
    private static final String CSS_PATH = "no/ntnu/idata2001/g23/view/";

    private final Pane rootPane;

    /**
     * Constructs a screen.
     * The scene's root will be the {@link Pane} returned by {@link #makeRoot()}.<br/>
     * <b>Note:</b> This does not make a controller,
     * extensions of this class are expected to make their own.
     *
     * @param cssFiles   Any additional {@code .css}-files to apply to the scene.<br/>
     *                   <b>Note: </b> {@code global.css} will always be applied to the scene
     */
    protected GenericScreen(String... cssFiles) {
        super(new Group(), DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);

        initializeNodes();
        this.rootPane = makeRoot(); //Makes the root pane

        //Putting "root" in a group makes the scaling work properly.
        //This means "root" isn't actually the root node, but its behavior is otherwise identical,
        //and it can be treated as if it was the actual root node
        setRoot(new Group(rootPane));

        //"root" style class must be added manually, due to the reason above
        rootPane.getStyleClass().add("root");

        //Adds .css
        getStylesheets().add(CSS_PATH + "global.css");
        for (String cssFile : cssFiles) {
            getStylesheets().add(CSS_PATH + cssFile);
        }

        //Makes the root pane scale upon window resize
        widthProperty().addListener((observableValue, oldValue, newValue) ->
                sizeChangeListener());
        heightProperty().addListener((observableValue, oldValue, newValue) ->
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
        double newWidth = getWidth();
        double newHeight = getHeight();

        double scaleFactor = Math.min(
                newWidth / BASE_WIDTH,
                newHeight / BASE_HEIGHT
        );


        Scale scale = new Scale(scaleFactor, scaleFactor);
        scale.setPivotX(0);
        scale.setPivotY(0);
        getRoot().getTransforms().setAll(scale);

        rootPane.setPrefWidth(newWidth / scaleFactor);
        rootPane.setPrefHeight(newHeight / scaleFactor);
    }

    public Pane getRootPane() {
        return rootPane;
    }
}
