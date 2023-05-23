package no.ntnu.idata2001.g23.model.story;

import no.ntnu.idata2001.g23.model.actions.HealthAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LinkTest {
    private Link validLink;

    @BeforeEach
    void before() {
        validLink = new Link("Test link", "Test reference");
    }

    @Test
    void testCreationOfLinkWithInvalidText() {
        assertThrows(IllegalArgumentException.class, () ->
                new Link(null, "Test reference"));
        assertThrows(IllegalArgumentException.class, () ->
                new Link("  ", "Test reference"));
    }

    @Test
    void testCreationOfLinkWithInvalidReference() {
        assertThrows(IllegalArgumentException.class, () ->
                new Link("Test link", null));
        assertThrows(IllegalArgumentException.class, () ->
                new Link("Test link", "  "));
    }

    @Test
    void testCreationOfValidLink() {
        assertEquals("Test link", validLink.getText());
        assertEquals("Test reference", validLink.getReference());
        assertTrue(validLink.getActions().isEmpty());
    }

    @Test
    void testValidAdditionOfAction() {
        assertDoesNotThrow(() -> validLink.addAction(new HealthAction(5)));
        assertFalse(validLink.getActions().isEmpty());
    }

    @Test
    void testAdditionOfNullAction() {
        assertThrows(IllegalArgumentException.class, () -> validLink.addAction(null));
    }

    @Test
    void testLinkEquals() {
        assertEquals(validLink, validLink);
        assertNotEquals(null, validLink);
        assertNotEquals(new Object(), validLink);

        assertNotEquals(new Link("Test text", "Different reference"), validLink);

        assertEquals(new Link("Different text", "Test reference"), validLink);
        Link equalLink = new Link("Test text", "Test reference");
        equalLink.addAction(new HealthAction(5));
        assertEquals(equalLink, validLink);
        assertEquals(new Link("Test text", "Test reference"), validLink);
    }
}
