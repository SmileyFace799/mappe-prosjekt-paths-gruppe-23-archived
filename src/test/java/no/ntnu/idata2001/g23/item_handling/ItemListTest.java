package no.ntnu.idata2001.g23.item_handling;

import no.ntnu.idata2001.g23.exceptions.NullValueException;
import no.ntnu.idata2001.g23.items.Item;
import no.ntnu.idata2001.g23.items.weapons.Sword;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class ItemListTest {
    private final ItemList testItemList = ItemList.getTestItems();

    @Test
    void testValidFetchingOfItems() {
        Item fetchedItem = assertDoesNotThrow(() -> testItemList.getItem(item -> true));
        assertNotNull(fetchedItem);

    }

    /**
     * Asserts the following:
     * <ul>
     *     <li>Fetching items with the same seed yields consistent results.<br/>
     *     <b>Note:</b> If this would prove to not be true, this assertion still has a
     *     1 / 515377520732011331036461129765621272702107522001 chance of falsely passing.</li>
     *     <li>Fetching items with different seeds yields different results.</li>
     * </ul>
     */
    @Test
    void testConsistentFetchingOfItemsWithSetSeed() {
        //Can be any pair of seeds where the first 100 items aren't identical.
        long seed1 = 1234;
        long seed2 = 4321;

        testItemList.setRandomSeed(seed1);
        ArrayList<Item> firstHundredItems1 = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            firstHundredItems1.add(testItemList.getItem(item -> true));
        }
        testItemList.setRandomSeed(seed1);
        ArrayList<Item> firstHundredItems2 = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            firstHundredItems2.add(testItemList.getItem(item -> true));
        }
        testItemList.setRandomSeed(seed2);
        ArrayList<Item> firstHundredItems3 = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            firstHundredItems3.add(testItemList.getItem(item -> true));
        }

        assertEquals(firstHundredItems2, firstHundredItems1);
        assertNotEquals(firstHundredItems3, firstHundredItems1);
    }

    /**
     * Asserts that the {@code getItem()} method actually fetches random items.
     */
    @Test
    void testRandomFetchingOfItems() {
        //Can be any seed that doesn't return the same item 100 times in a row.
        testItemList.setRandomSeed(6969);

        Item initialItem = testItemList.getItem(Item -> true);
        ArrayList<Item> randomlyFetchedItems = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            randomlyFetchedItems.add(testItemList.getItem(item -> true));
        }
        assertFalse(randomlyFetchedItems.stream().allMatch(item -> item == initialItem));
    }

    /**
     * Asserts that the {@code getItem()} method correctly filters items
     */
    @Test
    void testFilteredFetchingOfItems() {
        for (int i = 0; i < 100; i++) {
            assertEquals("Test name", testItemList.getItem(
                    item -> item.getName().equals("Test name")).getName());
            assertEquals("Another description", testItemList.getItem(
                    item -> item.getDescription().equals("Another description")).getDescription());
            assertEquals(5, testItemList.getItem(
                    Sword.class, sword -> sword.getBaseDamage() == 5).getBaseDamage());
        }

        assertThrows(NullValueException.class, () -> testItemList.getItem(item -> false));
    }
}
