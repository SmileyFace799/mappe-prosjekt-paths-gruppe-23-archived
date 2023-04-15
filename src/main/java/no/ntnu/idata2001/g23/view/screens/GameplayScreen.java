package no.ntnu.idata2001.g23.view.screens;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import no.ntnu.idata2001.g23.controllers.GameplayController;
import no.ntnu.idata2001.g23.middleman.GameUpdateListener;
import no.ntnu.idata2001.g23.middleman.GameplayManager;
import no.ntnu.idata2001.g23.model.Game;

public class GameplayScreen extends GenericScreen<GameplayController>
        implements GameUpdateListener {
    public GameplayScreen(GameplayController controller) {
        super(controller, "gameplay.css");
        GameplayManager.getInstance().addUpdateListener(this);
    }

    @Override
    protected Pane makeRoot() {
        Game game = GameplayManager.getInstance().getGame();
        BorderPane content = new BorderPane();
        if (game != null) {
            content.setCenter(new Label("story loaded :D"));
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
