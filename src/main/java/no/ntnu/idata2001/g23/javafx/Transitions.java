package no.ntnu.idata2001.g23.javafx;

import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Transitions {
    private Transitions() {
        throw new IllegalStateException("Utility class");
    }

    public static void contentTransition(Pane contentContainer, Node oldContent, Node newContent) {
        oldContent.setDisable(true);
        newContent.setOpacity(0);
        contentContainer.getChildren().add(newContent);
        new Transition() {
            {
                setCycleDuration(Duration.millis(500));
                setOnFinished(ae -> {
                    contentContainer.getChildren().remove(oldContent);
                    oldContent.setDisable(false);
                });
            }

            @Override
            protected void interpolate(double completion) {
                oldContent.setOpacity(1 - completion);
                newContent.setOpacity(completion);
            }
        }.play();
    }
}
