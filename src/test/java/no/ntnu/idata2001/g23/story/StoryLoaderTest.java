package no.ntnu.idata2001.g23.story;

import java.util.List;
import no.ntnu.idata2001.g23.actions.GoldAction;
import no.ntnu.idata2001.g23.actions.HealthAction;
import no.ntnu.idata2001.g23.actions.InventoryAction;
import no.ntnu.idata2001.g23.itemhandling.ItemFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StoryLoaderTest {

    @Test
    void TestLoadingOfValidStory() {
        Story loadedStory = assertDoesNotThrow(() -> StoryLoader.loadStory("testStory"));
        Passage openingPassage = new Passage("Beginnings", "You are in a small, "
                + "dimly lit room. There is a door in front of you."
                + "\nThere is also an entrance behind you.");
        openingPassage.addLink(new Link(
                "Try to open the door", "Another room", null
        ));
        openingPassage.addLink(new Link("Go behind you", "Test room", List.of(
                new InventoryAction(ItemFactory.makeItem("Test item")),
                new GoldAction(500)
        )));
        Story actualStory = new Story("Haunted House", openingPassage);

        Passage anotherPassage = new Passage("Another room",
                "The door opens to another room. You have reached the end of the game, "
                        + "there is no other option than going back.");
        anotherPassage.addLink(new Link("Go back", "Beginnings", null));
        actualStory.addPassage(anotherPassage);

        Passage yetAnotherPassage = new Passage("Test room",
                "Wowie, you found the debug area!");
        yetAnotherPassage.addLink(new Link("Go back", "Beginnings",
                List.of(new HealthAction(-5))));
        actualStory.addPassage(yetAnotherPassage);

        assertEquals(actualStory, loadedStory);
    }
}
