package no.ntnu.idata2001.g23.story;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.List;
import no.ntnu.idata2001.g23.actions.GoldAction;
import no.ntnu.idata2001.g23.actions.HealthAction;
import no.ntnu.idata2001.g23.actions.InventoryAction;
import no.ntnu.idata2001.g23.itemhandling.ItemFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StoryLoaderTest {
    void checkStory(String story, CorruptFileExceptionType type) {
        CorruptFileException exception = assertThrows(CorruptFileException.class, () ->
                StoryLoader.parseStory(new LineNumberReader(new StringReader(story))));
        assertEquals(type, exception.getType());
    }

    /**
     * Verifies that parsing happens correctly.
     */
    @Test
    void testLoadingOfValidFile() {
        String validTestStory = """
                Haunted House
                                
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
                StoryLoader.parseStory(new LineNumberReader(new StringReader(validTestStory))));

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

        actualStory.addPassage(new Passage("Link-less room",
                "You are now soft-locked :D"));

        assertEquals(actualStory, loadedStory);
    }

    /**
     * Verifies that an empty story is handled correctly
     */
    @Test
    void testParsingEmptyFile() {
        checkStory("", CorruptFileExceptionType.EMPTY_FILE);
    }

    @Test
    void testParsingStoryWithoutTitle() {
        checkStory("""
                                
                """, CorruptFileExceptionType.NO_TITLE
        );
        checkStory("""
                ::Some passage
                """, CorruptFileExceptionType.NO_TITLE)
        ;
    }

    @Test
    void testParsingFileWithoutPassages() {
        checkStory("""
                Story title
                """, CorruptFileExceptionType.NO_PASSAGES);
    }

    @Test
    void testParsingFileWithInvalidPassage() {
        checkStory("""
                Story title
                
                ::Valid passage
                Passage content
                
                Invalid passage
                More content
                """, CorruptFileExceptionType.INVALID_PASSAGE);
    }

    @Test
    void testParsingFileWithNoPassageName() {
        checkStory("""
                Story title
                
                ::
                Story content
                """, CorruptFileExceptionType.PASSAGE_NO_NAME);
    }

    @Test
    void testParsingFileWithNoPassageContent() {
        checkStory("""
                Story title
                
                ::Passage name
                
                """, CorruptFileExceptionType.PASSAGE_NO_CONTENT);
    }

    @Test
    void testParsingFileWithNoLinkText() {
        checkStory("""
                Story title
                
                ::Passage name
                Passage Content
                (Link reference)
                """, CorruptFileExceptionType.LINK_NO_TEXT);
    }

    @Test
    void testParsingFileWithNoLinkReference() {
        checkStory("""
                Story title
                
                ::Passage name
                Passage content
                [Link text]
                """, CorruptFileExceptionType.LINK_NO_REFERENCE);
    }

    @Test
    void testParsingFileWithInvalidAction() {
        checkStory("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) }Invalid action{
                """, CorruptFileExceptionType.LINK_INVALID_ACTION);
    }

    @Test
    void testParsingFileWithInvalidActionFormat() {
        checkStory("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) {Invalid action format}
                """, CorruptFileExceptionType.ACTION_INVALID_FORMAT);
    }

    @Test
    void testParsingFileWithInvalidActionType() {
        checkStory("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) {Invalid type: Some value}
                """, CorruptFileExceptionType.ACTION_INVALID_TYPE);
    }

    @Test
    void testParsingFileWithInvalidActionValue() {
        checkStory("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) {Gold: Invalid value}
                """, CorruptFileExceptionType.ACTION_INVALID_VALUE);
        checkStory("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) {Health: Invalid value}
                """, CorruptFileExceptionType.ACTION_INVALID_VALUE);
        checkStory("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) {Inventory: Invalid value}
                """, CorruptFileExceptionType.ACTION_INVALID_VALUE);
        checkStory("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) {Score: Invalid value}
                """, CorruptFileExceptionType.ACTION_INVALID_VALUE);
    }
}
