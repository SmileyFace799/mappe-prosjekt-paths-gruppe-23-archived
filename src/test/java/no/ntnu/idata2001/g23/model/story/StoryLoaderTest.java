package no.ntnu.idata2001.g23.model.story;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.List;
import no.ntnu.idata2001.g23.model.actions.GoldAction;
import no.ntnu.idata2001.g23.model.actions.HealthAction;
import no.ntnu.idata2001.g23.model.actions.InventoryAction;
import no.ntnu.idata2001.g23.model.fileparsing.CorruptFileException;
import no.ntnu.idata2001.g23.model.fileparsing.StoryLoader;
import no.ntnu.idata2001.g23.model.goals.GoldGoal;
import no.ntnu.idata2001.g23.model.goals.HealthGoal;
import no.ntnu.idata2001.g23.model.itemhandling.ItemProvider;
import no.ntnu.idata2001.g23.model.items.MiscItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StoryLoaderTest {
    private ItemProvider itemProvider;
    private StoryLoader validStoryLoader;

    @BeforeEach
    void before() {
        itemProvider = new ItemProvider();
        itemProvider.addProvidableItem("Test item", () ->
                new MiscItem(500, "Test Item", "Test description"));
        this.validStoryLoader = new StoryLoader(itemProvider);
    }

    void checkStory(String story, CorruptFileException.Type type) {
        CorruptFileException exception = assertThrows(CorruptFileException.class, () ->
                validStoryLoader.parseStory(new LineNumberReader(new StringReader(story))));
        assertEquals(type, exception.getType());
    }

    /**
     * Verifies that parsing happens correctly.
     */
    @Test
    void testLoadingOfValidFile() {
        String validTestStory = """
                Haunted House
                
                #Normal
                Gold: 2500
                Health: 100
                                
                ::Beginnings
                You are in a small, dimly lit room. There is a door in front of you.
                There is also an entrance behind you.
                [Try to open the door] (Another room)
                [Go behind you] (Test room) {Inventory: Test item} {Gold: 500}
                                
                ::Another room
                The door opens to another room. You have reached the end of the game, there is no other option than going back.
                [Go back] (Beginnings)
                                
                ::Test room
                Wowie, you found the debug area!
                [Go back] (Beginnings) {Health: -5}
                                
                ::Link-less room
                You are now soft-locked :D
                """;
        Story loadedStory = assertDoesNotThrow(() ->
                validStoryLoader.parseStory(new LineNumberReader(new StringReader(validTestStory))));

        Passage openingPassage = new Passage("Beginnings", "You are in a small, "
                + "dimly lit room. There is a door in front of you."
                + "\nThere is also an entrance behind you.");
        openingPassage.addLink(new Link(
                "Try to open the door", "Another room", null
        ));
        openingPassage.addLink(new Link("Go behind you", "Test room", List.of(
                new InventoryAction(itemProvider.provideItem("Test item")),
                new GoldAction(500)
        )));
        Story actualStory = new Story("Haunted House", openingPassage);

        actualStory.setGoals("Normal",
                List.of(new GoldGoal(2500), new HealthGoal(100)));

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

        actualStory.addPassage(new Passage("Link-less room",
                "You are now soft-locked :D"));

        assertEquals(actualStory, loadedStory);
    }

    /**
     * Verifies that an empty story is handled correctly
     */
    @Test
    void testParsingEmptyFile() {
        checkStory("", CorruptFileException.Type.EMPTY_FILE);
    }

    @Test
    void testParsingStoryWithoutTitle() {
        checkStory("""
                                
                """, CorruptFileException.Type.NO_TITLE
        );
        checkStory("""
                ::Some passage
                """, CorruptFileException.Type.NO_TITLE)
        ;
    }

    @Test
    void testParsingFileWithoutPassages() {
        checkStory("""
                Story title
                """, CorruptFileException.Type.NO_PASSAGES);
    }

    @Test
    void testParsingFileWithInvalidPassage() {
        checkStory("""
                Story title
                
                ::Valid passage
                Passage content
                
                Invalid passage
                More content
                """, CorruptFileException.Type.INVALID_GOAL_OR_PASSAGE);
    }

    @Test
    void testParsingFileWithNoPassageName() {
        checkStory("""
                Story title
                
                ::
                Story content
                """, CorruptFileException.Type.PASSAGE_NO_NAME);
    }

    @Test
    void testParsingFileWithNoPassageContent() {
        checkStory("""
                Story title
                
                ::Passage name
                
                """, CorruptFileException.Type.PASSAGE_NO_CONTENT);
    }

    @Test
    void testParsingFileWithNoLinkText() {
        checkStory("""
                Story title
                
                ::Passage name
                Passage Content
                (Link reference)
                """, CorruptFileException.Type.LINK_NO_TEXT);
    }

    @Test
    void testParsingFileWithNoLinkReference() {
        checkStory("""
                Story title
                
                ::Passage name
                Passage content
                [Link text]
                """, CorruptFileException.Type.LINK_NO_REFERENCE);
    }

    @Test
    void testParsingFileWithInvalidAction() {
        checkStory("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) }Invalid action{
                """, CorruptFileException.Type.LINK_INVALID_ACTION);
    }

    @Test
    void testParsingFileWithInvalidActionFormat() {
        checkStory("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) {Invalid action format}
                """, CorruptFileException.Type.ACTION_INVALID_FORMAT);
    }

    @Test
    void testParsingFileWithInvalidActionType() {
        checkStory("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) {Invalid type: Some value}
                """, CorruptFileException.Type.ACTION_INVALID_TYPE);
    }

    @Test
    void testParsingFileWithInvalidActionValue() {
        checkStory("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) {Gold: Invalid value}
                """, CorruptFileException.Type.ACTION_INVALID_VALUE);
        checkStory("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) {Health: Invalid value}
                """, CorruptFileException.Type.ACTION_INVALID_VALUE);
        checkStory("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) {Inventory: Invalid value}
                """, CorruptFileException.Type.ACTION_INVALID_VALUE);
        checkStory("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) {Score: Invalid value}
                """, CorruptFileException.Type.ACTION_INVALID_VALUE);
    }
}
