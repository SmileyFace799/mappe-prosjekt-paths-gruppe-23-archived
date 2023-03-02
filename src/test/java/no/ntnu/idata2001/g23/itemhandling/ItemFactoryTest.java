package no.ntnu.idata2001.g23.itemhandling;

import no.ntnu.idata2001.g23.exceptions.unchecked.ElementNotFoundException;
import no.ntnu.idata2001.g23.items.Item;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemFactoryTest {

    @Test
    void testMakingOfNonExistantItem() {
        assertThrows(ElementNotFoundException.class, () ->
                ItemFactory.makeItem("Item that doesn't exist"));
    }

    @Test
    void testValidMakingOfItem() {
        Item fetchedItem = assertDoesNotThrow(() -> ItemFactory.makeItem("Test item"));
        assertNotNull(fetchedItem);

    }
}
