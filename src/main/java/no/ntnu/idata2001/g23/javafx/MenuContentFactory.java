package no.ntnu.idata2001.g23.javafx;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import no.ntnu.idata2001.g23.textures.TxLoader;

public class MenuContentFactory {
    private final StackPane root;

    public MenuContentFactory(StackPane root) {
        this.root = root;
    }

    public Node getMainMenu() {
        VBox content = new VBox(60);

        content.setAlignment(Pos.CENTER);
        //content.setStyle(Css.CONTENT_FONT_STYLE);
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
        playButton.setOnAction(ae -> Transitions.contentTransition(
                root, content, getPlayGame()));
        content.getChildren().add(playButton);

        Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(ae -> Transitions.contentTransition(
                root, content, getSettings()));
        content.getChildren().add(settingsButton);

        Button credits = new Button("Credits");
        credits.setDisable(true);
        content.getChildren().add(credits);

        Button quitButton = new Button("Quit Game");
        quitButton.setOnAction(ae -> Platform.exit());
        content.getChildren().add(quitButton);
        return content;
    }

    public Node getSettings() {
        VBox content = new VBox(60);

        content.setAlignment(Pos.CENTER);
        //content.setStyle(Css.CONTENT_FONT_STYLE);
        content.setBackground(new Background(new BackgroundImage(
                TxLoader.getImage("bg.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, true, false)
        )));

        Text settingsTitle = new Text("Settings");
        //settingsTitle.setStyle(Css.TITLE_FONT_STYLE);
        content.getChildren().add(settingsTitle);

        content.getChildren().add(new Rectangle(0, 200));

        Button backButton = new Button("Go Back");
        backButton.setOnAction(ae -> Transitions.contentTransition(
                root, content, getMainMenu()));
        content.getChildren().add(backButton);
        return content;
    }

    public Node getPlayGame() {
        VBox content = new VBox(60);

        content.setAlignment(Pos.CENTER);
        //content.setStyle(Css.CONTENT_FONT_STYLE);
        content.setBackground(new Background(new BackgroundImage(
                TxLoader.getImage("bg.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, true, false)
        )));

        Text playGameTitle = new Text("Play Game");
        //playGameTitle.setStyle(Css.TITLE_FONT_STYLE);
        content.getChildren().add(playGameTitle);

        content.getChildren().add(new Rectangle(0, 200));

        Button startNewStory = new Button("Start New Story");
        startNewStory.setOnAction(ae -> Transitions.contentTransition(
                root, content, getNewGame()
        ));
        content.getChildren().add(startNewStory);

        Button loadgame = new Button("Load Game");
        content.getChildren().add(loadgame);

        Button tutorial = new Button("Tutorial");
        content.getChildren().add(tutorial);

        Button backButton = new Button("Go Back");
        backButton.setOnAction(ae -> Transitions.contentTransition(
                root, content, getMainMenu()));
        content.getChildren().add(backButton);
        return content;
    }

    // side og button som starter nytt spill
    public Node getNewGame() {
        VBox content = new VBox(60);
        content.setAlignment(Pos.CENTER);
        //content.setStyle(Css.CONTENT_FONT_STYLE);
        content.setBackground(new Background(new BackgroundImage(
                TxLoader.getImage("bg.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, true, false)
        )));

        Text newGameTitle = new Text("New game");
        //newGameTitle.setStyle(Css.TITLE_FONT_STYLE);
        content.getChildren().add(newGameTitle);

        TextField playerName = new TextField();
        playerName.setMaxWidth(500);
        playerName.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isContentChange()) {
                String newText = change.getControlNewText();
                int maxLength = 10;
                if (newText.length() > maxLength) {
                    change.setText(newText.substring(0, maxLength));
                    change.setRange(0, change.getControlText().length());
                }
            }
            return change;
        }));
        content.getChildren().add(playerName);

        Button startPlaying = new Button("Start playing");
        content.getChildren().add(startPlaying);

        Button backButton = new Button("Go Back");
        backButton.setOnAction(ae -> Transitions.contentTransition(
                root, content, getPlayGame()));
        content.getChildren().add(backButton);

        return content;
    }
}
