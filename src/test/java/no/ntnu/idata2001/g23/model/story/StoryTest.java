package no.ntnu.idata2001.g23.model.story;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

class StoryTest {
    private Passage openingPassage;
    private Link nextLink;
    private Passage nextPassage;
    private Story validStory;

    @BeforeEach
    void before() {
        nextPassage = new Passage("Next passage", "Test content");
        nextLink = new Link("Next link", nextPassage.getTitle());
        openingPassage = new Passage("Opening passage", "Test content");
        openingPassage.addLink(nextLink);
        validStory = new Story("Test story", openingPassage);
    }

    @Test
    void testCreationOfStoryWithInvalidTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Story(null, openingPassage));
        assertThrows(IllegalArgumentException.class, () -> new Story("  ", openingPassage));
    }

    @Test
    void testCreationOfStoryWithNoOpeningPassage() {
        assertThrows(IllegalArgumentException.class, () -> new Story("Test story", null));
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
        assertEquals(openingPassage, validStory.getOpeningPassage());
    }

    @Test
    void testAdditionOfNullPassage() {
        assertThrows(IllegalArgumentException.class, () -> validStory.addPassage(null));
    }

    @Test
    void testAdditionOfDuplicatePassage() {
        assertThrows(IllegalArgumentException.class, () -> validStory.addPassage(openingPassage));
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
        assertDoesNotThrow(() -> validStory.addPassage(nextPassage));
        assertEquals(2, passages.size());
        assertTrue(passages.contains(nextPassage));
        assertEquals(nextPassage, validStory.getPassage(nextLink));
    }

    @Test
    void testRemovingOfPassageWithNullLink() {
        assertThrows(IllegalArgumentException.class, () -> validStory.removePassage(null));
    }

    @Test
    void testRemovingOfPassageThatIsLinkedTo() {
        validStory.addPassage(nextPassage);
        assertThrows(IllegalStateException.class, () -> validStory.removePassage(nextLink));
    }

    @Test
    void testValidRemovingOfPassage() {
        validStory.addPassage(new Passage("Unlinked passage", "Unlinked content"));
        assertDoesNotThrow(() -> validStory.removePassage(new Link(
                "Unlinked link", "Unlinked passage")));
    }

    @Test
    void testGettingOfPassageWithNullLink() {
        assertThrows(IllegalArgumentException.class, () -> validStory.getPassage(null));
    }

    @Test
    void testGettingOfPassageWithBrokenLink() {
        assertThrows(IllegalArgumentException.class, () -> validStory.getPassage(nextLink));
    }

    @Test
    void testGettingBrokenLinks() {
        validStory.addPassage(nextPassage);
        Link brokenLink = new Link(
                "Broken link", "A non-existant passage");
        validStory.getOpeningPassage().addLink(brokenLink);
        assertEquals(List.of(brokenLink), validStory.getBrokenLinks());
    }
}
