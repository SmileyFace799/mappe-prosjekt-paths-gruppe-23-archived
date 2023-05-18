package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.LineNumberReader;
import java.io.StringReader;
import no.ntnu.idata2001.g23.model.actions.InventoryAction;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.UsableItem;
import no.ntnu.idata2001.g23.model.items.Weapon;
import no.ntnu.idata2001.g23.model.misc.Provider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemLoaderTest {
    /**
     * Asserts that a {@link CorruptFileException} is thrown,
     * and asserts that the exception thrown is of the specified type.
     *
     * @param itemsString The items string to parse
     * @param type The {@link CorruptFileException.Type} to check for
     */
    private void assertCfeType(
            String itemsString, CorruptFileException.Type type) {
        CorruptFileException exception = assertThrows(CorruptFileException.class, () -> ItemLoader
                .parseItems(new LineNumberReader(new StringReader(itemsString))));
        assertEquals(type, exception.getType());
    }

    @Test
    void testLoadingOfValidItems() {
        String validItemsString = """
                #Big Sword UwU
                -melee Weapon
                Damage: 5
                Crit Chance: 0.3
                Value: 500
                Description: Your mother B)
                
                #Usable Test
                -Usable
                Value: 50
                Description: UwU
                On Use: {Inventory: Big Sword UwU}
                """;
        Provider<Item> parsedItemProvider = assertDoesNotThrow(() ->
                ItemLoader.parseItems(new LineNumberReader(new StringReader(validItemsString))));

        Provider<Item> validItemProvider = new Provider<>();
        validItemProvider.addProvidable("Big Sword UwU", () -> new Weapon(5,
                0.3, 500, "Big Sword UwU", "Your mother B)"));
        validItemProvider.addProvidable("Usable Test", () -> new UsableItem(
                50, "Usable Test", "UwU",
                new InventoryAction(validItemProvider.provide("Big Sword UwU"))));

        parsedItemProvider.getIdentifiers().forEach(identifier -> assertEquals(
                validItemProvider.provide(identifier), parsedItemProvider.provide(identifier)));
    }

    @Test
    void testLoadingOfInvalidItem() {
        assertCfeType("""
                Invalid item lol
                """, CorruptFileException.Type.INVALID_ITEM);
    }

    @Test
    void testLoadingOfItemWithNoName() {
        assertCfeType("""
                #
                """, CorruptFileException.Type.ITEM_NO_NAME);
    }

    @Test
    void testLoadingOfItemWithNoType() {
        assertCfeType("""
                #Big Sword UwU
                Damage: 5
                """, CorruptFileException.Type.ITEM_NO_TYPE);
    }

    @Test
    void testLoadingOfItemWithInvalidType() {
        assertCfeType("""
                #Big Sword UwU
                -Invalid Type
                """, CorruptFileException.Type.ITEM_INVALID_TYPE);
    }

    @Test
    void testLoadingOfItemWithInvalidParameterValue() {
        assertCfeType("""
                #Big Sword UwU
                -melee Weapon
                Damage: 5
                Crit Chance: 0.3
                Value: Invalid Value
                Description: Your mother B)
                """, CorruptFileException.Type.ITEM_INVALID_PARAMETER_VALUE);
    }
}
