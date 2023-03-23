package no.ntnu.idata2001.g23.javafx;

/**
 * a.
 */
public class Css {
    private Css() {
        throw new IllegalStateException("Utility class");
    }

    private static final String TITLE_FONT_SIZE = "-fx-font-size: 144px;";
    private static final String TITLE_FONT_WEIGHT = "-fx-font-weight: bold;";
    private static final String TITLE_TEXT_COLOR = "-fx-text-fill: blue;";
    private static final String CONTENT_FONT_SIZE = "-fx-font-size: 72px;";

    public static final String TITLE_FONT_STYLE = TITLE_FONT_SIZE
            + TITLE_FONT_WEIGHT
            + TITLE_TEXT_COLOR;
    public static final String CONTENT_FONT_STYLE = CONTENT_FONT_SIZE;
}
