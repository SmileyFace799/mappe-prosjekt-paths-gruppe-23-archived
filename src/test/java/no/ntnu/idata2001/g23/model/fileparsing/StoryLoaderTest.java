package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.List;
import no.ntnu.idata2001.g23.model.actions.GoldAction;
import no.ntnu.idata2001.g23.model.actions.HealthAction;
import no.ntnu.idata2001.g23.model.actions.InventoryAction;
import no.ntnu.idata2001.g23.model.misc.Provider;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.MiscItem;
import no.ntnu.idata2001.g23.model.story.Link;
import no.ntnu.idata2001.g23.model.story.Passage;
import no.ntnu.idata2001.g23.model.story.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StoryLoaderTest {
    private Provider<Item> itemProvider;
    private StoryLoader validStoryLoader;

    @BeforeEach
    void before() {
        itemProvider = new Provider<>();
        itemProvider.addProvidable("Test item", () ->
                new MiscItem(500, "Test Item", "Test description"));
        this.validStoryLoader = new StoryLoader(itemProvider);
    }

    /**
     * Asserts that a {@link CorruptFileException} is thrown,
     * and asserts that the exception thrown is of the specified type.
     *
     * @param storyString The story string to parse
     * @param type The {@link CorruptFileException.Type} to check for
     */
    private void assertCfeType(String storyString, CorruptFileException.Type type) {
        CorruptFileException exception = assertThrows(CorruptFileException.class, () ->
                validStoryLoader.parseStory(new LineNumberReader(new StringReader(storyString))));
        assertEquals(type, exception.getType());
    }

    /**
     * Verifies that parsing happens correctly.
     */
    @Test
    void testLoadingOfValidFile() {
        String validStoryString = """
                Haunted House
                     
                ::Beginnings
                You are in a small, dimly lit room. There is a door in front of you.
                There is also an entrance behind you.
                [Try to open the door] (Another room)
                [Go behind you] (Test room) {Inventory: Test item} {Gold: 500}
                                
                ::Another room
                The door opens to another room. You have reached the end of the game, there is no other option than going back.
                [Back] (Beginnings)
                                
                ::Test room
                Wowie, you found the debug area!
                [Back] (Beginnings) {Health: -5}
                                
                ::Link-less room
                You are now soft-locked :D
                """;
        Story loadedStory = assertDoesNotThrow(() -> validStoryLoader
                .parseStory(new LineNumberReader(new StringReader(validStoryString))));

        Passage openingPassage = new Passage("Beginnings", "You are in a small, "
                + "dimly lit room. There is a door in front of you."
                + "\nThere is also an entrance behind you.");
        openingPassage.addLink(new Link(
                "Try to open the door", "Another room", null
        ));
        openingPassage.addLink(new Link("Go behind you", "Test room", List.of(
                new InventoryAction(itemProvider.provide("Test item")),
                new GoldAction(500)
        )));
        Story validStory = new Story("Haunted House", openingPassage);

        Passage anotherPassage = new Passage("Another room",
                "The door opens to another room. You have reached the end of the game, "
                        + "there is no other option than going back.");
        anotherPassage.addLink(new Link("Back", "Beginnings", null));
        validStory.addPassage(anotherPassage);

        Passage yetAnotherPassage = new Passage("Test room",
                "Wowie, you found the debug area!");
        yetAnotherPassage.addLink(new Link("Back", "Beginnings",
                List.of(new HealthAction(-5))));
        validStory.addPassage(yetAnotherPassage);

        validStory.addPassage(new Passage("Link-less room",
                "You are now soft-locked :D"));

        assertEquals(validStory, loadedStory);
    }

    /**
     * Verifies that an empty story is handled correctly
     */
    @Test
    void testParsingEmptyFile() {
        assertCfeType("", CorruptFileException.Type.EMPTY_FILE);
    }

    @Test
    void testParsingStoryWithoutTitle() {
        assertCfeType("""
                                
                """, CorruptFileException.Type.NO_TITLE
        );
        assertCfeType("""
                ::Some passage
                """, CorruptFileException.Type.NO_TITLE)
        ;
    }

    @Test
    void testParsingFileWithoutPassages() {
        assertCfeType("""
                Story title
                """, CorruptFileException.Type.NO_PASSAGES);
    }

    @Test
    void testParsingFileWithInvalidPassage() {
        assertCfeType("""
                Story title
                
                ::Valid passage
                Passage content
                
                Invalid passage
                More content
                """, CorruptFileException.Type.INVALID_PASSAGE);
    }

    @Test
    void testParsingFileWithNoPassageName() {
        assertCfeType("""
                Story title
                
                ::
                Story content
                """, CorruptFileException.Type.PASSAGE_NO_NAME);
    }

    @Test
    void testParsingFileWithNoPassageContent() {
        assertCfeType("""
                Story title
                
                ::Passage name
                
                """, CorruptFileException.Type.PASSAGE_NO_CONTENT);
    }

    @Test
    void testParsingFileWithNoLinkText() {
        assertCfeType("""
                Story title
                
                ::Passage name
                Passage Content
                (Link reference)
                """, CorruptFileException.Type.LINK_NO_TEXT);
    }

    @Test
    void testParsingFileWithNoLinkReference() {
        assertCfeType("""
                Story title
                
                ::Passage name
                Passage content
                [Link text]
                """, CorruptFileException.Type.LINK_NO_REFERENCE);
    }

    @Test
    void testParsingFileWithInvalidAction() {
        assertCfeType("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) }Invalid action{
                """, CorruptFileException.Type.LINK_INVALID_ACTION);
    }

    @Test
    void testParsingFileWithInvalidActionFormat() {
        assertCfeType("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) {Invalid action format}
                """, CorruptFileException.Type.ACTION_INVALID_FORMAT);
    }

    @Test
    void testParsingFileWithInvalidActionType() {
        assertCfeType("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) {Invalid type: Some value}
                """, CorruptFileException.Type.ACTION_INVALID_TYPE);
    }

    @Test
    void testParsingFileWithInvalidActionValue() {
        assertCfeType("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) {Gold: Invalid value}
                """, CorruptFileException.Type.ACTION_INVALID_VALUE);
        assertCfeType("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) {Health: Invalid value}
                """, CorruptFileException.Type.ACTION_INVALID_VALUE);
        assertCfeType("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) {Inventory: Invalid value}
                """, CorruptFileException.Type.ACTION_INVALID_VALUE);
        assertCfeType("""
                Story title
                
                ::Passage name
                Passage content
                [Link text] (Link reference) {Score: Invalid value}
                """, CorruptFileException.Type.ACTION_INVALID_VALUE);
    }
}
