package no.ntnu.idata2001.g23.model.itemhandling;

import java.io.LineNumberReader;
import java.io.StringReader;
import no.ntnu.idata2001.g23.model.fileparsing.ItemLoader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemLoaderTest {
    @Test
    void testLoadingOfValidFile() {
        String validTestItems = """
                #Big Sword UwU
                -melee Weapon
                Damage: 5
                Crit Chance: 0.3
                Value: 500
                Description: Your mother B)
                """;
        ItemProvider itemProvider = assertDoesNotThrow(() ->
                ItemLoader.parseItems(new LineNumberReader(new StringReader(validTestItems))));
        System.out.println(itemProvider.provideItem("Big Sword UwU"));
    }
}
