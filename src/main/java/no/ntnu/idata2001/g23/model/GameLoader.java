package no.ntnu.idata2001.g23.model;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.itemhandling.FullInventoryException;
import no.ntnu.idata2001.g23.model.itemhandling.ItemLoader;
import no.ntnu.idata2001.g23.model.itemhandling.ItemProvider;
import no.ntnu.idata2001.g23.model.story.CorruptFileException;
import no.ntnu.idata2001.g23.model.story.CorruptFileExceptionType;
import no.ntnu.idata2001.g23.model.story.Story;
import no.ntnu.idata2001.g23.model.story.StoryLoader;

/**
 * Loads a game from a directory containing a full game.
 */
public class GameLoader {
    private GameLoader() {
        throw new IllegalStateException("Utility class");
    }

    private static Path getPath(String fileExtension, Collection<Path> paths) {
        Optional<Path> path = paths
                .stream()
                .filter(p -> p.toString().endsWith(fileExtension))
                .findFirst();
        return path.orElse(null);
    }

    /**
     * Parses the game's {@code story.info}-file.
     *
     * @param gamePath The game's path
     * @param fileReader A {@link LineNumberReader} containing the contents of the file
     * @return A collection of {@link Path}s with all the files to load
     * @throws CorruptFileException If the {@code story.info}-file cannot be parsed
     */
    public static Collection<Path> parseInfo(Path gamePath, LineNumberReader fileReader)
            throws CorruptFileException {
        Collection<Path> filesToRead = new HashSet<>();
        String nextLine = "";
        try {
            //Reads through entire file
            while ((nextLine = fileReader.readLine()) != null) {
                filesToRead.add(gamePath.resolve(nextLine));
            }
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileExceptionType.UNKNOWN_INFO,
                    fileReader.getLineNumber());
        } catch (InvalidPathException ipe) {
            throw new CorruptFileException(CorruptFileExceptionType.INFO_INVALID_PATH,
                    fileReader.getLineNumber(), nextLine);
        }
        return filesToRead;
    }

    /**
     * Loads a game from a directory containing a full game.
     *
     * @param playerName    The player's name
     * @param gameDirectory The game's directory
     * @return The loaded game
     * @throws CorruptFileException   If any files in the game directory cannot be loaded
     * @throws FullInventoryException If a player is initialized with more starting items than
     *                                their inventory size allows
     */
    public static Game loadGame(String playerName, String gameDirectory)
            throws CorruptFileException, FullInventoryException {
        Game game;
        Path gamePath = Path.of(gameDirectory);
        try (LineNumberReader fileReader = new LineNumberReader(
                Files.newBufferedReader(gamePath.resolve("story.info")))
        ) {
            Collection<Path> filesToRead = parseInfo(gamePath, fileReader);
            Path itemsPath = getPath(".items", filesToRead);
            ItemProvider itemProvider = itemsPath != null
                    ? ItemLoader.loadItems(itemsPath.toString())
                    : null;

            Path storyPath = getPath(".paths", filesToRead);
            if (storyPath == null) {
                throw new CorruptFileException(CorruptFileExceptionType.NO_STORY);
            }
            Story story = StoryLoader.loadStory(storyPath.toString());

            game = new Game.GameBuilder(new Player.PlayerBuilder(playerName, 30).build(),
                    story, "Easy").setItemProvider(itemProvider).build();
            for (String itemName : new String[]{"Big Sword UwU"}) {
                game.getPlayer()
                        .getInventory()
                        .addItem(game.getItemProvider().provideItem(itemName));
            }
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileExceptionType.UNKNOWN_INFO);
        }
        return game;
    }
}
