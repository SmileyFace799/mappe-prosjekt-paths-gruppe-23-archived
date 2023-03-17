package no.ntnu.idata2001.g23.textures;

import java.io.InputStream;
import javafx.scene.image.Image;

/**
 * A factory class for loading textures.
 */
public class TxLoader {
    private TxLoader() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Loads a texture.
     *
     * @param filePath The texture's file path.
     * @return The found texture as an image.
     */
    public static Image getTx(String filePath) {
        InputStream is = TxLoader.class.getResourceAsStream(filePath);
        if (is == null) {
            //TODO: Load a default texture here.
            throw new RuntimeException("Couldn't find image \"" + filePath + "\"");
        }
        return new Image(is);
    }
}
