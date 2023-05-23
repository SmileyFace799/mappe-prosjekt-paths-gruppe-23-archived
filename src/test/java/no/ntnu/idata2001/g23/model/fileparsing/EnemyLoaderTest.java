package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.LineNumberReader;
import java.io.StringReader;
import no.ntnu.idata2001.g23.model.entities.enemies.Enemy;
import no.ntnu.idata2001.g23.model.entities.enemies.VampireEnemy;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.Weapon;
import no.ntnu.idata2001.g23.model.misc.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnemyLoaderTest {
    private Provider<Item> itemProvider;
    private EnemyLoader enemyLoader;

    @BeforeEach
    void before() {
        itemProvider = new Provider<>();
        itemProvider.addProvidable("Test Item", () ->
                new Item("Test Item", "Test description"));
        itemProvider.addProvidable("Test Weapon", () ->
                new Weapon(5, "Test Weapon", "Other description"));
        enemyLoader = new EnemyLoader(itemProvider);
    }

    /**
     * Asserts that a {@link CorruptFileException} is thrown,
     * and asserts that the exception thrown is of the specified type.
     *
     * @param enemiesString The enemies string to parse
     * @param type The {@link CorruptFileException.Type} to check for
     */
    private void assertCfeType(
            String enemiesString, CorruptFileException.Type type) {
        CorruptFileException exception = assertThrows(CorruptFileException.class, () -> enemyLoader
                .parseEnemies(new LineNumberReader(new StringReader(enemiesString))));
        assertEquals(type, exception.getType());
    }

    @Test
    void testLoadingOfValidEnemies() {
        String validEnemiesString = """
                !Test Enemy
                -Vampire
                Health: 20
                Score: 50
                Gold: 5
                Weapon: Test Weapon
                """;

        Provider<Enemy> parsedEnemyProvider = assertDoesNotThrow(() -> enemyLoader.parseEnemies(
                new LineNumberReader(new StringReader(validEnemiesString))));

        Provider<Enemy> validEnemyProvider = new Provider<>();
        validEnemyProvider.addProvidable("Test Enemy", () ->
                new VampireEnemy.VampireEnemyBuilder("Test Enemy", 20)
                        .setGold(5)
                        .setScore(50)
                        .setEquippedWeapon((Weapon) itemProvider.provide("Test Weapon"))
                        .build());

        parsedEnemyProvider.getIdentifiers().forEach(identifier -> assertEquals(
                validEnemyProvider.provide(identifier), parsedEnemyProvider.provide(identifier)));
    }

    @Test
    void testLoadingOfInvalidEnemy() {
        assertCfeType("""
                Invalid enemy lol
                """, CorruptFileException.Type.INVALID_ENEMY);
    }

    @Test
    void testLoadingOfEnemyWithNoName() {
        assertCfeType("""
                !
                """, CorruptFileException.Type.ENEMY_NO_NAME);
    }

    @Test
    void testLoadingOfEnemyWithNoType() {
        assertCfeType("""
                !Test Enemy
                Health: 20
                """, CorruptFileException.Type.ENEMY_NO_TYPE);
    }

    @Test
    void testLoadOfEnemyWithInvalidType() {
        assertCfeType("""
                !Enemy
                -Invalid type
                """, CorruptFileException.Type.ENEMY_INVALID_TYPE);
    }

    @Test
    void testLoadingOfEnemyWithInvalidParameterValue() {
        assertCfeType("""
                !Test Enemy
                -Vampire
                Health: -1
                Score: 0.5
                Gold: 9999999999999999999999999999999999999999999999
                Weapon: Test Item
                """, CorruptFileException.Type.ENEMY_INVALID_PARAMETER_VALUE);
    }
}
