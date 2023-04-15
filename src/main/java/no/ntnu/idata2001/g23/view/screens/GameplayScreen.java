package no.ntnu.idata2001.g23.view.screens;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import no.ntnu.idata2001.g23.controllers.GameplayController;
import no.ntnu.idata2001.g23.middleman.GameUpdateListener;
import no.ntnu.idata2001.g23.middleman.GameplayManager;
import no.ntnu.idata2001.g23.model.Game;
import no.ntnu.idata2001.g23.model.story.Passage;

/**
 * The gameplay screen, where the game is played.
 */
public class GameplayScreen extends GenericScreen<GameplayController>
        implements GameUpdateListener {
    /**
     * Makes the gameplay screen.
     *
     * @param controller The gameplay controller
     */
    public GameplayScreen(GameplayController controller) {
        super(controller, "gameplay.css");
        GameplayManager.getInstance().addUpdateListener(this);
        setBottomContent(null);
    }

    private HBox bottomContent;

    private void setBottomContent(HBox content) {
        if (content == null) {
            bottomContent = new HBox(100);
            bottomContent.getStyleClass().add("prompt");
            bottomContent.getStyleClass().add("bottom-prompt");

            Button fightButton = new Button("Fight");
            //TODO: Make this button work
            fightButton.setDisable(true);
            bottomContent.getChildren().add(fightButton);

            Button moveButton = new Button("Move");
            //TODO: Make this button work
            moveButton.setDisable(true);
            bottomContent.getChildren().add(moveButton);

            Button itemButton = new Button("Item");
            //TODO: Make this button work
            itemButton.setDisable(true);
            bottomContent.getChildren().add(itemButton);

            Button menuButton = new Button("Menu");
            menuButton.setOnAction(ae -> controller.changeScreen(MainMenuScreen.class));
            bottomContent.getChildren().add(menuButton);
        } else {
            bottomContent = content;
        }
        updateRoot();
    }

    @Override
    protected Pane makeRoot() {
        Game game = GameplayManager.getInstance().getGame();
        BorderPane content = new BorderPane();
        if (game != null) {
            Passage currentPassage = GameplayManager.getInstance().getCurrentPassage();

            //TOP
            VBox topContent = new VBox(30);
            topContent.getStyleClass().add("prompt");
            content.setTop(topContent);

            Label title = new Label(currentPassage.getTitle());
            title.getStyleClass().add("header");
            topContent.getChildren().add(title);

            topContent.getChildren().add(new Label(currentPassage.getContent()));

            //BOTTOM
            content.setBottom(bottomContent);
        } else {
            VBox errorVbox = new VBox(20);
            errorVbox.getStyleClass().add("menu");
            errorVbox.getChildren().add(new Label(
                    "Oops, there's no game loaded, spaghetti code moment"));
            Button backButton = new Button("Go back");
            backButton.setOnAction(ae -> controller.changeScreen(MainMenuScreen.class));
            errorVbox.getChildren().add(backButton);
            content.setCenter(errorVbox);
        }

        return content;
    }

    @Override
    public void onUpdate() {
        updateRoot();
    }
}
