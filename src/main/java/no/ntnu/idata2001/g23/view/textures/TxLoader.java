package no.ntnu.idata2001.g23.view.textures;

import java.io.InputStream;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import no.ntnu.idata2001.g23.exceptions.unchecked.NegativeNumberException;

/**
 * A factory class for loading textures.
 */
public class TxLoader {
    private static final Image defaultTexture = makeDefaultTexture();

    private TxLoader() {
        throw new IllegalStateException("Do not instantiate this class pls :)");
    }

    private static Image makeDefaultTexture() {
        WritableImage defaultTx = new WritableImage(2, 2);
        PixelWriter writer = defaultTx.getPixelWriter();
        writer.setColor(0, 0, Color.MAGENTA);
        writer.setColor(1, 0, Color.BLACK);
        writer.setColor(0, 1, Color.BLACK);
        writer.setColor(1, 1, Color.MAGENTA);
        return defaultTx;
    }

    /**
     * Loads an image from a file path.
     *
     * @param filePath The image's file path.
     * @return The found image file as an image.
     */
    private static Image getImage(String filePath) {
        Image image;
        InputStream is = TxLoader.class.getResourceAsStream(filePath);
        if (is == null) {
            image = defaultTexture;
            System.out.println(
                    "WARNING: Couldn't find image \"" + filePath + "\", loading default texture");
        } else {
            image = new Image(is);
        }
        return image;
    }

    /**
     * Loads a viewable image from a file path.
     *
     * @param filePath      The image's file path.
     * @param width         The image's width. If this is 0, it is ignored.
     * @param height        The image's height. If this is 0, it is ignored.
     * @param preserveRatio If the image should preserve its aspect ratio.
     * @return The loaded image file as a viewable image. If the image cannot be loaded,
     *         this returns a default texture
     * @throws NegativeNumberException  If {@code width} & {@code height} is less than 0.
     * @throws IllegalArgumentException If {@code preserveRatio == true},
     *                                  but both {@code width} & {@code height} are not 0.
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

    /**
     * Loads an icon from a file path.
     *
     * <p>This is a special convenience method that calls
     * {@link #getImageView(String, double, double, boolean)},
     * but the image will always be square, and fully white</p>
     *
     * @param filePath The icon's file path
     * @param size     The icon's rendered size
     * @return The loaded icon. If the icon cannot be loaded, this returns a default texture
     */
    public static ImageView getIcon(String filePath, double size) {
        ImageView icon = getImageView(filePath, size, size, false);
        if (!defaultTexture.equals(icon.getImage())) {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(1); //Makes the icon fully white
            icon.setEffect(colorAdjust);
        }
        return icon;
    }
}
