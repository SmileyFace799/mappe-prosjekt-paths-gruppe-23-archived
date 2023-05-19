package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.Weapon;
import no.ntnu.idata2001.g23.model.misc.Provider;

/**
 * Loads a player from a {@code .player}-file.
 */
public class PlayerLoader {
    private final Provider<Item> itemProvider;
    private final String name;
    private final String difficulty;

    /**
     * Makes a player loader.
     *
     * @param itemProvider A {@link Provider} that provides items for the loaded player
     * @param name The player's name
     * @param difficulty The difficulty to load the player for
     */
    public PlayerLoader(Provider<Item> itemProvider, String name, String difficulty) {
        this.itemProvider = itemProvider;
        this.name = name;
        this.difficulty = difficulty;
    }

    /**
     * Parses a player from a {@link LineNumberReader}.
     *
     * @param fileReader The {@link LineNumberReader} containing a player
     * @return The parsed player
     * @throws CorruptFileException If the player could not be parsed
     */
    public Player parsePlayer(LineNumberReader fileReader)
            throws CorruptFileException {
        Player player;
        try {
            String nextLine = "";
            //Moves the reader forward to the player stats for the specified difficulty.
            while (!nextLine.startsWith("#" + difficulty)) {
                nextLine = fileReader.readLine();
                if (nextLine == null) {
                    throw new CorruptFileException(CorruptFileException.Type.NO_PLAYER,
                            fileReader.getLineNumber(), difficulty);
                }
            }

            Map<String, String> playerParameterMap =
                    CollectionParserUtil.parseMap(fileReader, true,
                            Parameters.getPlayerParameters());
            player = new Player.PlayerBuilder(name,
                    Integer.parseInt(playerParameterMap.get(Parameters.HEALTH)))
                    .setGold(Integer.parseInt(playerParameterMap.get(Parameters.GOLD)))
                    .setScore(Integer.parseInt(playerParameterMap.get(Parameters.SCORE)))
                    .setStartingItems(itemProvider.provideAll(CollectionParserUtil
                            .parseList(playerParameterMap.get(Parameters.INVENTORY))))
                    .setEquippedWeapon((Weapon) itemProvider
                            .provide(playerParameterMap.get(Parameters.WEAPON)))
                    .build();
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN_ENEMIES,
                    fileReader.getLineNumber());
        } catch (ClassCastException | IllegalArgumentException e) {
            throw new CorruptFileException(CorruptFileException.Type.PLAYER_INVALID_VALUE,
                    fileReader.getLineNumber());
        }
        return player;
    }

    /**
     * Loads a player from a {@code .player}-file.
     *
     * @param playerFilePath The file path of the player to load
     * @return The loaded player
     * @throws CorruptFileException If the player could not be loaded
     */
    public Player loadPlayer(Path playerFilePath) throws CorruptFileException {
        Player player;
        try (LineNumberReader fileReader = new LineNumberReader(
                Files.newBufferedReader(playerFilePath)
        )) {
            player = parsePlayer(fileReader);
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN_PLAYER);
        }
        return player;
    }

    private static class Parameters {
        public static final String HEALTH = "health";
        public static final String SCORE = "score";
        public static final String GOLD = "gold";
        public static final String INVENTORY = "inventory";
        public static final String WEAPON = "weapon";

        private Parameters() {
            throw new IllegalStateException("Do not instantiate this class pls :)");
        }

        public static String[] getPlayerParameters() {
            return new String[]{HEALTH, SCORE, GOLD, INVENTORY, WEAPON};
        }
    }
}
