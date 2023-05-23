package no.ntnu.idata2001.g23.view.textures;

import java.io.InputStream;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * A factory class for loading resources as images.
 */
public class ImageLoader {
    private static final Image DEFAULT_TEXTURE = makeDefaultTexture();

    private ImageLoader() {
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
     * Loads a resource as an {@link Image}.
     * To load external images that aren't resources, use {@link #getImage(String)}.
     *
     * @param resource The resource's relative file path
     * @return The found resource as an image. If it is not found, a default texture is returned.
     */
    public static Image getImageResource(String resource) {
        Image image;
        InputStream is = ImageLoader.class.getResourceAsStream(resource);
        if (is == null) {
            image = DEFAULT_TEXTURE;
        } else {
            image = new Image(is);
        }
        return image;
    }

    /**
     * Loads an external url as an {@link Image}.
     * To load resources that aren't external images, use {@link #getImageResource(String)}.
     *
     * @param filePath The image's file path
     * @return The image that the file path leads to.
     *         If this cannot be found, a default texture is returned
     */
    public static Image getImage(String filePath) {
        Image image;
        try {
            image = new Image(filePath);
        } catch (IllegalArgumentException | NullPointerException e) {
            image = DEFAULT_TEXTURE;
        }
        return image;
    }

    /**
     * Loads an {@link Image} as a viewable {@link ImageView}.
     *
     * @param image         The image to load
     * @param width         The resulting image view's width. If this is 0, it is ignored.
     * @param height        The resulting image view's height. If this is 0, it is ignored.
     * @param preserveRatio If the resulting image view should preserve its aspect ratio.
     * @return The image as a viewable image
     * @throws IllegalArgumentException <ul>
     *                                  <li>If {@code width} or {@code height} is less than 0</li>
     *                                  <li>If {@code preserveRatio == true},
     *                                  but both {@code width} and {@code height} are not 0.</li>
     *                                  </ul>
     */
    public static ImageView getImageView(
            Image image, double width, double height, boolean preserveRatio
    ) {
        if (width < 0) {
            throw new IllegalArgumentException("int \"width\" cannot be less than 0");
        }
        if (height < 0) {
            throw new IllegalArgumentException("int \"height\" cannot be less than 0");
        }
        if (preserveRatio && width != 0 && height != 0) {
            throw new IllegalArgumentException(
                    "Cannot preserve ratio with both width & height specified");
        }
        ImageView iv = new ImageView(image);
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
     * Loads an {@link Image} as a viewable {@link ImageView}.
     *
     * <p>This is a special convenience method that calls
     * {@link #getImageView(Image, double, double, boolean)},
     * but the resulting image view will always be square, and fully white</p>
     *
     * @param image The icon image to load
     * @param size  The resulting icon's rendered size
     * @return The loaded resource, as an icon
     */
    public static ImageView getIcon(Image image, double size) {
        ImageView icon = getImageView(image, size, size, false);
        if (!DEFAULT_TEXTURE.equals(icon.getImage())) {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(1); //Makes the icon fully white
            icon.setEffect(colorAdjust);
        }
        return icon;
    }
}
