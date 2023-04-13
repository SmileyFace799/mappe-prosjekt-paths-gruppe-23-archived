package no.ntnu.idata2001.g23.view.textures;

import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import no.ntnu.idata2001.g23.exceptions.unchecked.NegativeNumberException;

/**
 * A factory class for loading textures.
 */
public class TxLoader {
    private TxLoader() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Loads a image from a file path.
     *
     * @param filePath The image's file path.
     * @return The found image file as an image.
     */
    public static Image getImage(String filePath) {
        InputStream is = TxLoader.class.getResourceAsStream(filePath);
        if (is == null) {
            //TODO: Load a default texture here.
            throw new RuntimeException("Couldn't find image \"" + filePath + "\"");
        }
        return new Image(is);
    }

    public static ImageView getImageView(String filePath) {
        return getImageView(filePath, 0, 0, false);
    }

    /**
     * Loads a viewable image from a file path.
     *
     * @param filePath      The image's file path.
     * @param width         The image's width.
     * @param height        The image's height.
     * @param preserveRatio If the image should preserve it's aspect ratio.
     * @return The found image file as a viewable image.
     * @throws NegativeNumberException  If {@code width} & {@code height} is less than 0.
     * @throws IllegalArgumentException If {@code preserveRatio == true}
     *                                  & both {@code width} & {@code height} are not 0.
     */
    public static ImageView getImageView(
            String filePath, double width, double height, boolean preserveRatio
    ) {
        if (width < 0) {
            throw new NegativeNumberException("int \"width\" cannot be less than 0");
        }
        if (height < 0) {
            throw new NegativeNumberException("int \"height\" cannot be less than 0");
        }
        if (preserveRatio && width != 0 && height != 0) {
            throw new IllegalArgumentException(
                    "Cannot preserve ratio with both width & height specified");
        }
        ImageView iv = new ImageView(getImage(filePath));
        iv.setPreserveRatio(preserveRatio);
        if (width != 0) {
            iv.setFitWidth(width);
        }
        if (height != 0) {
            iv.setFitHeight(height);
        }
        return iv;
    }
}
