package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import no.ntnu.idata2001.g23.model.entities.enemies.Enemy;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.Weapon;
import no.ntnu.idata2001.g23.model.misc.Provider;

/**
 * Loads enemies from a {@code .enemies}-file.
 */
public class EnemyLoader {
    private final Provider<Item> itemProvider;

    /**
     * Makes an enemy loader.
     *
     * @param itemProvider A {@link Provider} that provides items for loaded enemies
     */
    public EnemyLoader(Provider<Item> itemProvider) {
        this.itemProvider = itemProvider;
    }

    private Supplier<Enemy> parseEnemy(String enemyName, LineNumberReader fileReader)
            throws IOException, CorruptFileException {
        String type = fileReader.readLine();
        if (!type.startsWith("-")) {
            throw new CorruptFileException(CorruptFileException.Type.ITEM_NO_TYPE,
                    fileReader.getLineNumber());
        }

        Supplier<Enemy> enemySupplier;
        try {
            switch (type.substring(1).toLowerCase().replace(" ", "")) {
                case "basic" -> {
                    Map<String, String> enemyParameterMap = CollectionParserUtil
                            .parseMap(fileReader, true, Parameters.getBasicParameters());
                    int health = Integer.parseInt(enemyParameterMap.get(Parameters.HEALTH));
                    int score = Integer.parseInt(enemyParameterMap.get(Parameters.SCORE));
                    int gold = Integer.parseInt(enemyParameterMap.get(Parameters.GOLD));
                    List<Item> items = itemProvider.provideAll(CollectionParserUtil.parseList(
                            enemyParameterMap.get(Parameters.INVENTORY)));
                    double dropChance =
                            Double.parseDouble(enemyParameterMap.get(Parameters.DROP_CHANCE));
                    Weapon weapon =
                            (Weapon) itemProvider.provide(enemyParameterMap.get(Parameters.WEAPON));
                    boolean dropWeapon =
                            Boolean.parseBoolean(enemyParameterMap.get(Parameters.DROP_WEAPON));
                    enemySupplier = () -> new Enemy.EnemyBuilder(enemyName, health)
                            .setScore(score)
                            .setGold(gold)
                            .setStartingItems(items)
                            .setItemDropChance(dropChance)
                            .setEquippedWeapon(weapon)
                            .canDropWeapon(dropWeapon)
                            .build();
                }
                default -> throw new CorruptFileException(
                        CorruptFileException.Type.ENEMY_INVALID_TYPE,
                        fileReader.getLineNumber());
            }
        } catch (ClassCastException | IllegalArgumentException e) {
            throw new CorruptFileException(CorruptFileException.Type.ENEMY_INVALID_PARAMETER_VALUE,
                    fileReader.getLineNumber());
        }
        return enemySupplier;
    }

    /**
     * Parses enemies from a {@link LineNumberReader},
     * and returns a {@link Provider} that can provide all the enemies.
     *
     * @param fileReader A {@link LineNumberReader} that contains some enemies.
     * @return A {@link Provider} that can provide all the parsed enemies
     * @throws CorruptFileException If the enemies could not be parsed
     */
    public Provider<Enemy> parseEnemies(LineNumberReader fileReader) throws CorruptFileException {
        Provider<Enemy> provider = new Provider<>();
        try {
            String nextLine;
            //Goes through entire file
            while ((nextLine = fileReader.readLine()) != null) {
                if (nextLine.startsWith("!")) {
                    if (nextLine.length() == 1) {
                        throw new CorruptFileException(CorruptFileException.Type.ENEMY_NO_NAME,
                                fileReader.getLineNumber());
                    }
                    String enemyName = nextLine.substring(1);
                    provider.addProvidable(enemyName, parseEnemy(enemyName, fileReader));
                } else if (!nextLine.isBlank()) {
                    throw new CorruptFileException(CorruptFileException.Type.INVALID_ENEMY,
                            fileReader.getLineNumber());
                }
            }
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN_ENEMIES,
                    fileReader.getLineNumber());
        }
        return provider;
    }

    /**
     * Loads enemies from a {@code .enemies}-file,
     * and returns a {@link Provider} that can provide all the enemies.
     *
     * @param enemiesFilePath The file path of the enemies to load
     * @return A {@link Provider} that can provide all the loaded enemies
     * @throws CorruptFileException If the enemies could not be loaded
     */
    public Provider<Enemy> loadEnemies(Path enemiesFilePath) throws CorruptFileException {
        Provider<Enemy> enemyProvider;
        try (LineNumberReader fileReader = new LineNumberReader(
                Files.newBufferedReader(enemiesFilePath)
        )) {
            enemyProvider = parseEnemies(fileReader);
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN_ENEMIES);
        }
        return enemyProvider;
    }

    private static class Parameters {
        public static final String HEALTH = "health";
        public static final String SCORE = "score";
        public static final String GOLD = "gold";
        public static final String INVENTORY = "inventory";
        public static final String DROP_CHANCE = "dropchance";
        public static final String WEAPON = "weapon";
        public static final String DROP_WEAPON = "dropweapon";

        private Parameters() {
            throw new IllegalStateException("Do not instantiate this class pls :)");
        }

        public static String[] getBasicParameters() {
            return new String[]{HEALTH, SCORE, GOLD, INVENTORY, DROP_CHANCE, WEAPON, DROP_WEAPON};
        }
    }
}
