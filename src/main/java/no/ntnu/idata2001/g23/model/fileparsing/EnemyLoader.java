package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Supplier;
import no.ntnu.idata2001.g23.model.entities.enemies.Enemy;
import no.ntnu.idata2001.g23.model.entities.enemies.VampireEnemy;
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

    private Supplier<Enemy> parseBasicEnemy(String enemyName, Map<String, String> parameterMap) {
        Enemy.EnemyBuilder enemyBuilder = new Enemy.EnemyBuilder(enemyName,
                Integer.parseInt(parameterMap.get(Parameters.HEALTH)))
                .setScore(Integer.parseInt(parameterMap.get(Parameters.SCORE)))
                .setGold(Integer.parseInt(parameterMap.get(Parameters.GOLD)));
        String inventoryString = parameterMap.get(Parameters.INVENTORY);
        if (inventoryString != null) {
            enemyBuilder.setStartingItems(itemProvider.provideAll(CollectionParserUtil
                    .parseList(inventoryString)));
        }
        String dropChanceString = parameterMap.get(Parameters.DROP_CHANCE);
        if (dropChanceString != null) {
            enemyBuilder.setItemDropChance(Double.parseDouble(dropChanceString));
        }
        String weaponString = parameterMap.get(Parameters.WEAPON);
        if (weaponString != null) {
            enemyBuilder.setEquippedWeapon((Weapon) itemProvider.provide(weaponString));
        }
        String dropWeaponString = parameterMap.get(Parameters.DROP_WEAPON);
        if (dropWeaponString != null) {
            enemyBuilder.canDropWeapon(Boolean.parseBoolean(dropWeaponString));
        }
        return enemyBuilder::build;
    }

    private Supplier<Enemy> parseVampireEnemy(String enemyName, Map<String, String> parameterMap) {
        VampireEnemy.VampireEnemyBuilder vampireBuilder =
                new VampireEnemy.VampireEnemyBuilder(enemyName,
                        Integer.parseInt(parameterMap.get(Parameters.HEALTH)))
                        .setScore(Integer.parseInt(parameterMap.get(Parameters.SCORE)))
                        .setGold(Integer.parseInt(parameterMap.get(Parameters.GOLD)));
        String inventoryString = parameterMap.get(Parameters.INVENTORY);
        if (inventoryString != null) {
            vampireBuilder.setStartingItems(itemProvider.provideAll(CollectionParserUtil
                    .parseList(inventoryString)));
        }
        String dropChanceString = parameterMap.get(Parameters.DROP_CHANCE);
        if (dropChanceString != null) {
            vampireBuilder.setItemDropChance(Double.parseDouble(dropChanceString));
        }
        String weaponString = parameterMap.get(Parameters.WEAPON);
        if (weaponString != null) {
            vampireBuilder.setEquippedWeapon((Weapon) itemProvider.provide(weaponString));
        }
        String dropWeaponString = parameterMap.get(Parameters.DROP_WEAPON);
        if (dropWeaponString != null) {
            vampireBuilder.canDropWeapon(Boolean.parseBoolean(dropWeaponString));
        }
        return vampireBuilder::build;
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
                case "basic" -> enemySupplier = parseBasicEnemy(enemyName,
                        CollectionParserUtil.parseMap(fileReader, true,
                                Parameters.getBasicRequired()));
                case "vampire" -> enemySupplier = parseVampireEnemy(enemyName,
                        CollectionParserUtil.parseMap(fileReader, true,
                                Parameters.getVampireRequired()));

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
                    String enemyName = nextLine.substring(1).trim();
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
        public static final String HEALTH = "Health";
        public static final String SCORE = "Score";
        public static final String GOLD = "Gold";
        public static final String INVENTORY = "Inventory";
        public static final String DROP_CHANCE = "Drop Chance";
        public static final String WEAPON = "Weapon";
        public static final String DROP_WEAPON = "Drop Weapon";

        private Parameters() {
            throw new IllegalStateException("Do not instantiate this class pls :)");
        }

        public static String[] getBasicRequired() {
            return new String[]{HEALTH, SCORE, GOLD};
        }

        public static String[] getVampireRequired() {
            return getBasicRequired();
        }
    }
}
