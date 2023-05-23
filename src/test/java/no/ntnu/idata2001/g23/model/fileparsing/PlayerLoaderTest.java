package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.List;
import no.ntnu.idata2001.g23.model.actions.InventoryAction;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.UsableItem;
import no.ntnu.idata2001.g23.model.items.Weapon;
import no.ntnu.idata2001.g23.model.misc.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerLoaderTest {
    private String name;
    private String difficulty;
    private Provider<Item> itemProvider;
    private PlayerLoader playerLoader;

    @BeforeEach
    void before() {
        name = "TestPlayer69";
        difficulty = "Test";

        itemProvider = new Provider<>();
        itemProvider.addProvidable("Big Sword UwU", () -> new Weapon(5,
                "Big Sword UwU", "Your mother B)"));
        itemProvider.addProvidable("Usable Test", () -> new UsableItem(
                "Usable Test", "UwU", new InventoryAction(
                        itemProvider.provide("Big Sword UwU"))));

        playerLoader = new PlayerLoader(itemProvider, name, difficulty);
    }

    /**
     * Asserts that a {@link CorruptFileException} is thrown,
     * and asserts that the exception thrown is of the specified type.
     *
     * @param playerString The player string to parse
     * @param type The {@link CorruptFileException.Type} to check for
     */
    private void assertCfeType(String playerString, CorruptFileException.Type type) {
        CorruptFileException exception = assertThrows(CorruptFileException.class, () ->
                playerLoader.parsePlayer(new LineNumberReader(new StringReader(playerString))));
        assertEquals(type, exception.getType());
    }

    @Test
    void testLoadingOfValidPlayer() {
        String validPlayerString = String.format("""
                #Easy
                Health: 30
                Score: 100
                Gold: 300
                Inventory: Big Sword UwU, Usable Test, Usable Test
                Weapon: Big Sword UwU
                                
                #%s
                Health: 15
                Score: 0
                Gold: 0
                Inventory: Usable Test
                Weapon: Big Sword UwU
                """, difficulty);

        Player parsedPlayer = assertDoesNotThrow(() -> playerLoader.parsePlayer(
                new LineNumberReader(new StringReader(validPlayerString))));

        Player validPlayer = new Player.PlayerBuilder(name, 15)
                .setStartingItems(List.of(itemProvider.provide("Usable Test")))
                .setEquippedWeapon((Weapon) itemProvider.provide("Big Sword UwU"))
                .build();

        assertEquals(validPlayer, parsedPlayer);
    }

    @Test
    void testLoadingOfPlayerWithNoPlayer() {
        assertCfeType("", CorruptFileException.Type.NO_PLAYER);
        assertCfeType("""
                #Other difficulty
                Health: 30
                Score: 100
                Gold: 300
                Inventory: Big Sword UwU, Usable Test, Usable Test
                Weapon: Big Sword UwU
                """, CorruptFileException.Type.NO_PLAYER);

        //Shouldn't throw, should only care about the playerLoader's difficulty, and nothing else
        assertDoesNotThrow(() -> playerLoader.parsePlayer(new LineNumberReader(new StringReader(
                String.format("""
                        #Other difficulty
                        
                        #%s
                        Health: 15
                        Score: 0
                        Gold: 0
                        Inventory: Usable Test
                        Weapon: Big Sword UwU
                        """, difficulty)))));
    }

    @Test
    void testLoadingOfPlayerWithInvalidParameters() {
        assertCfeType(String.format("""
                #%s
                Health: -1
                Score: 0
                Gold: 0
                Inventory: Usable Test
                Weapon: Big Sword UwU
                """, difficulty), CorruptFileException.Type.PLAYER_INVALID_VALUE);
        assertCfeType(String.format("""
                #%s
                Health: 30
                Score: 0.05
                Gold: 300
                Inventory: Big Sword UwU, Usable Test, Usable Test
                Weapon: Big Sword UwU
                """, difficulty), CorruptFileException.Type.PLAYER_INVALID_VALUE);
        assertCfeType(String.format("""
                #%s
                Health: 30
                Score: 100
                Gold: 999999999999999999999999999999999999999999999999999999999999999
                Inventory: Big Sword UwU, Usable Test, Usable Test
                Weapon: Big Sword UwU
                """, difficulty), CorruptFileException.Type.PLAYER_INVALID_VALUE);
        assertCfeType(String.format("""
                #%s
                Health: 30
                Score: 100
                Gold: 300
                Inventory: Invalid value
                Weapon: Big Sword UwU
                """, difficulty), CorruptFileException.Type.PLAYER_INVALID_VALUE);
        assertCfeType(String.format("""
                #%s
                Health: 30
                Score: 100
                Gold: 300
                Inventory: Big Sword UwU, Usable Test, Usable Test
                Weapon: Usable Test
                """, difficulty), CorruptFileException.Type.PLAYER_INVALID_VALUE);
    }
}
