package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;
import no.ntnu.idata2001.g23.view.textures.ImageLoader;

/**
 * Loads spritePaths for various objects used in the game.
 * This is only for loading external spritePaths that aren't part of the application itself.
 *
 * @see ImageLoader TxLoader
 */
public class SpritePathsLoader {
    private final Path gamePath;

    public SpritePathsLoader(Path gamePath) {
        this.gamePath = gamePath;
    }

    //TODO: Unit testing
    public Map<String, String> parseSpritePaths(LineNumberReader fileReader)
            throws CorruptFileException {
        Map<String, String> spritePaths;
        try {
            spritePaths = CollectionParserUtil
                    .parseMap(fileReader, false)
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> gamePath.resolve(entry.getValue()).toString()
                    ));
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN_SPRITES,
                    fileReader.getLineNumber());
        } catch (InvalidPathException ipe) {
            throw new CorruptFileException(CorruptFileException.Type.INVALID_SPRITE);
        }
        return spritePaths;
    }

    public Map<String, String> loadSpritePaths(Path spritesFilePath)
            throws CorruptFileException {
        Map<String, String> spritePaths;
        try (LineNumberReader fileReader = new LineNumberReader(
                Files.newBufferedReader(spritesFilePath)
        )) {
            spritePaths = parseSpritePaths(fileReader);
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN_SPRITES);
        }
        return spritePaths;
    }
}
