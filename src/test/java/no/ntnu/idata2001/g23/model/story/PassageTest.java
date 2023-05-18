package no.ntnu.idata2001.g23.model.story;

import java.util.ArrayList;
import no.ntnu.idata2001.g23.exceptions.unchecked.BlankStringException;
import no.ntnu.idata2001.g23.exceptions.unchecked.DuplicateElementException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NullValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PassageTest {
    private Passage validPassage;
    private Link link;

    @BeforeEach
    public void before() {
        validPassage = new Passage("Test title", "Test content");
        link = new Link("Test text", "Test reference", new ArrayList<>());
    }

    @Test
    void testCreationOfPassageWithInvalidTitle() {
        assertThrows(BlankStringException.class, () -> new Passage(null, "Test content"));
        assertThrows(BlankStringException.class, () -> new Passage("  ", "Test content"));
    }

    @Test
    void testCreationOfPassageWithInvalidContent() {
        assertThrows(BlankStringException.class, () -> new Passage("Test title", null));
        assertThrows(BlankStringException.class, () -> new Passage("Test title", "  "));
    }

    @Test
    void testCreationOfPassageWithValidParameters() {
        assertEquals("Test title", validPassage.getTitle());
        assertEquals("Test content", validPassage.getContent());
        assertFalse(validPassage.hasLinks());
    }

    /**
     * Asserts the following:
     * <ul>
     *     <li>Adding a new link to the passage does not throw an exception.</li>
     *     <li>The passage has links after successfully adding one.</li>
     *     <li>The passage has exactly 1 link.</li>
     *     <li>The 1 link contained in the passage is the link added.</li>
     * </ul>
     */
    @Test
    void testValidAdditionOfLink() {
        assertDoesNotThrow(() -> validPassage.addLink(link));
        assertTrue(validPassage.hasLinks());
        List<Link> links = validPassage.getLinks();
        assertEquals(1, links.size());
        assertTrue(links.contains(link));
    }

    @Test
    void testAdditonofNullLink() {
        assertThrows(NullValueException.class, () -> validPassage.addLink(null));
    }

    @Test
    void testAdditionOfDuplicateLink() {
        validPassage.addLink(link);
        assertThrows(DuplicateElementException.class, () -> validPassage.addLink(link));
    }

    /**
     * Asserts the following:
     * <ul>
     *     <li>A passage equals itself</li>
     *     <li>A passage does not equal a non-passage object</li>
     *     <li>A passage does not equal another passage with a different title</li>
     *     <li>A passage does not equal another passage with different content</li>
     *     <li>A passage does not equal another passage with different links</li>
     *     <li>A passage equals another passage with the same title, content & links</li>
     * </ul>
     */
    @Test
    void testPassageEquals() {
        assertEquals(validPassage, validPassage);
        assertNotEquals(null, validPassage);
        assertNotEquals(new Object(), validPassage);

        assertNotEquals(new Passage("Different title", "Test content"), validPassage);
        assertNotEquals(new Passage("Test title", "Different content"), validPassage);
        Passage differentPassage = new Passage("Test title", "Test content");
        differentPassage.addLink(link);
        assertNotEquals(differentPassage, validPassage);

        assertEquals(new Passage("Test title", "Test content"), validPassage);

    }

}
