package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import no.ntnu.idata2001.g23.model.Game;
import no.ntnu.idata2001.g23.model.entities.Enemy;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.goals.Goal;
import no.ntnu.idata2001.g23.model.itemhandling.FullInventoryException;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.misc.Provider;
import no.ntnu.idata2001.g23.model.story.Story;

/**
 * Loads a game from a directory containing a full game.
 */
public class GameLoader {
    private GameLoader() {
        throw new IllegalStateException("Do not instantiate this class pls :)");
    }

    private static Optional<Path> getOptionalPath(String fileExtension, Collection<Path> paths) {
        return paths
                .stream()
                .filter(p -> p.toString().endsWith(fileExtension))
                .findFirst();
    }

    private static Path getPath(String fileExtension, Collection<Path> paths) {
        return getOptionalPath(fileExtension, paths).orElse(null);
    }

    private static Path getPathRequired(
            String fileExtension, Collection<Path> paths, CorruptFileException.Type exceptionType
    ) throws CorruptFileException {
        return getOptionalPath(fileExtension, paths)
                .orElseThrow(() -> new CorruptFileException(exceptionType));
    }

    /**
     * Parses the game's {@code story.info}-file.
     *
     * @param gamePath The game's path
     * @return A collection of {@link Path}s with all the files to load
     * @throws CorruptFileException If the {@code story.info}-file cannot be parsed
     */
    private static Collection<Path> parseInfo(Path gamePath)
            throws CorruptFileException {
        Collection<Path> filesToRead = new HashSet<>();
        String nextLine = "";
        int lineNumber = 0;
        try (LineNumberReader fileReader = new LineNumberReader(
                Files.newBufferedReader(gamePath.resolve("story.info"))
        )) {
            //Reads through entire file
            while ((nextLine = fileReader.readLine()) != null) {
                lineNumber = fileReader.getLineNumber();
                filesToRead.add(gamePath.resolve(nextLine));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN_INFO,
                    lineNumber);
        } catch (InvalidPathException ipe) {
            throw new CorruptFileException(CorruptFileException.Type.INFO_INVALID_PATH,
                    lineNumber, nextLine);
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
        Collection<Path> filesToRead = parseInfo(gamePath);

        //Parse items, create itemProvider
        Path itemsPath = getPath(".items", filesToRead);
        Provider<Item> itemProvider = itemsPath != null
                ? ItemLoader.loadItems(itemsPath)
                : null;

        //Parse enemies, create enemyProvider, using itemProvider
        Path enemiesPath = getPath(".enemies", filesToRead);
        Provider<Enemy> enemyProvider = enemiesPath != null
                ? new EnemyLoader(itemProvider).loadEnemies(enemiesPath)
                : null;

        System.out.println(enemyProvider.provide("Test Enemy").getName());

        //parse & create story, using itemProvider
        Path storyPath = getPathRequired(".paths", filesToRead,
                CorruptFileException.Type.NO_STORY);
        Story story = new StoryLoader(itemProvider).loadStory(storyPath);

        //parse & create goals, using itemProvider
        Path goalsPath = getPathRequired(".goals", filesToRead,
                CorruptFileException.Type.NO_GOALS);
        List<Goal> goals = new GoalLoader(itemProvider, "Easy").loadGoals(goalsPath);

        //Create game, using story & goals
        game = new Game(new Player.PlayerBuilder(playerName, 30).build(),
                story, goals);

        for (String itemName : new String[]{"Big Sword UwU", "Usable Test", "Usable Test"}) {
            game.getPlayer()
                    .getInventory()
                    .addItem(itemProvider.provide(itemName));
        }
        return game;
    }
}
