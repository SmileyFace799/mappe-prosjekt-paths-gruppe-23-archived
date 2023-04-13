package no.ntnu.idata2001.g23.view.scenes;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import no.ntnu.idata2001.g23.DungeonApp;

/**
 * Represents the basics of any scene contained in the applications.
 */
public abstract class GenericScene {
    private Node root;
    private Pane rootPane;
    private Scene scene;

    protected GenericScene() {
        initialize(makeRoot());
    }

    protected abstract Pane makeRoot();

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

        //These methods are why the rootPane field exists
        rootPane.setPrefWidth(newWidth  / scaleFactor);
        rootPane.setPrefHeight(newHeight / scaleFactor);
    }

    protected void initialize(Node root) {
        this.root = root;
        if (root instanceof Pane pane) {
            this.rootPane = pane;
        } else {
            BorderPane newPane = new BorderPane();
            newPane.setCenter(root);
            this.rootPane = newPane;
        }
        this.scene = new Scene(new Group(root), 1280, 720);
        scene.setFill(Color.BLACK);

        scene.widthProperty().addListener((observableValue, oldValue, newValue) ->
                sizeChangeListener());
        scene.heightProperty().addListener((observableValue, oldValue, newValue) ->
                sizeChangeListener());
    }

    public Node getRoot() {
        return root;
    }

    public Scene getScene() {
        return scene;
    }
}
