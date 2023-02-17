package no.ntnu.idata2001.g23.story;

import static org.junit.jupiter.api.Assertions.*;

import no.ntnu.idata2001.g23.exceptions.BlankStringException;
import no.ntnu.idata2001.g23.exceptions.DuplicateElementException;
import no.ntnu.idata2001.g23.exceptions.NullValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

class StoryTest {
    private Passage openingPassage;
    private Passage nextPassage;
    private Story validStory;

    @BeforeEach
    void before() {
        openingPassage = new Passage("Opening passage", "Test content");
        nextPassage = new Passage("Next passage", "Test content");
        validStory = new Story("Test story", openingPassage);
    }

    @Test
    void testCreationOfStoryWithInvalidTitle() {
        assertThrows(BlankStringException.class, () -> new Story(null, openingPassage));
        assertThrows(BlankStringException.class, () -> new Story("  ", openingPassage));
    }

    @Test
    void testCreationOfStoryWithNoOpeningPassage() {
        assertThrows(NullValueException.class, () -> new Story("Test story", null));
    }

    /**
     * Asserts the following:
     * <ul>
     *     <li>The story's title is properly set when created.</li>
     *     <li>The story is created with 1 passage.</li>
     *     <li>The 1 passage contained in the story is the opening passage.</li>
     * </ul>
     */
    @Test
    void testCreationOfStoryWithValidParameters() {
        assertEquals("Test story", validStory.getTitle());
        Collection<Passage> passages = validStory.getPassages();
        assertEquals(1, passages.size());
        assertTrue(passages.contains(openingPassage));
    }

    /**
     * Asserts the following:
     * <ul>
     *     <li>Adding a new passage after the opening passage does not throw an exception.</li>
     *     <li>The story has 2 passages, the opening passage & the newly added passage.</li>
     *     <li>The story contains the new passage</li>
     *     <li>The new passage's associated link is added to the opening passage's links,
     *     since the opening passage was passed as the "after"-passage when adding the new one.</li>
     * </ul>
     */
    @Test
    void testValidAdditionOfPassage() {
        Collection<Passage> passages = validStory.getPassages();
        Link nextLink = assertDoesNotThrow(() -> validStory.addPassage(nextPassage, openingPassage));
        assertEquals(2, passages.size());
        assertTrue(passages.contains(nextPassage));
        assertTrue(openingPassage.getLinks().contains(nextLink));
    }

    @Test
    void testAdditionOfDuplicatePassage() {
        assertThrows(DuplicateElementException.class, () ->
                validStory.addPassage(openingPassage, openingPassage));
    }

    @Test
    void testAdditionOfPassageWithoutAfter() {
        assertThrows(NullValueException.class, () -> validStory.addPassage(nextPassage, null));
    }

}
