package no.ntnu.idata2001.g23.javafx;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import no.ntnu.idata2001.g23.DungeonApp;
import no.ntnu.idata2001.g23.textures.TxLoader;

public class MenuContentFactory {
    private final StackPane root;

    public MenuContentFactory(StackPane root) {
        this.root = root;
    }

    public Node getContent(String contentName) {
        Node node;
        switch (contentName) {
            case "mainMenu" -> {
                VBox content = new VBox(60);

                content.setAlignment(Pos.CENTER);
                content.setMinSize(DungeonApp.BASE_WIDTH, DungeonApp.BASE_HEIGHT);
                content.setStyle("-fx-font-size: 72px;");
                content.setBackground(new Background(new BackgroundImage(
                        TxLoader.getImage("bg.png"),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(1.0, 1.0, true, true, true, false)
                )));
                content.getChildren().add(TxLoader.getImageView(
                        "tempTitle.png", 2000, 0, true));

                content.getChildren().add(new Rectangle(0, 200));

                Button playButton = new Button("Play Game");
                playButton.setDisable(true);
                content.getChildren().add(playButton);

                Button settingsButton = new Button("Settings");
                settingsButton.setOnAction(ae -> Transitions.contentTransition(
                        root, content, getContent("settings")));
                content.getChildren().add(settingsButton);

                Button quitButton = new Button("Quit Game");
                quitButton.setOnAction(ae -> Platform.exit());
                content.getChildren().add(quitButton);
                node = content;
            }
            case "settings" -> {
                VBox content = new VBox(60);

                content.setAlignment(Pos.CENTER);
                content.setMinSize(DungeonApp.BASE_WIDTH, DungeonApp.BASE_HEIGHT);
                content.setStyle("-fx-font-size: 72px;");
                content.setBackground(new Background(new BackgroundImage(
                        TxLoader.getImage("bg.png"),
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(1.0, 1.0, true, true, true, false)
                )));

                Text settingsTitle = new Text("Settings");
                settingsTitle.setStyle("-fx-font-size: 144px; -fx-font-weight: bold;");
                content.getChildren().add(settingsTitle);

                content.getChildren().add(new Rectangle(0, 200));

                Button backButton = new Button("Go Back");
                backButton.setOnAction(ae -> Transitions.contentTransition(
                        root, content, getContent("mainMenu")));
                content.getChildren().add(backButton);
                node = content;
            }
            default -> throw new IllegalArgumentException(
                    "No content with name \"" + contentName + "\"");
        }
        return node;
    }
}
