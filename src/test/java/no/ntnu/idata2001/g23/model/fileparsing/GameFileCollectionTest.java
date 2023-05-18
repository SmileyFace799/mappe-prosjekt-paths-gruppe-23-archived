package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameFileCollectionTest {
    private Path gamePath;

    @BeforeEach
    void before() {
        gamePath = Path.of("test/path/");
    }

    /**
     * Asserts that a {@link CorruptFileException} is thrown,
     * and asserts that the exception thrown is of the specified type.
     *
     * @param infoString The info string to parse
     */
    private void assertCfeType(String infoString) {
        CorruptFileException exception = assertThrows(CorruptFileException.class, () ->
                GameFileCollection.parseInfo(new LineNumberReader(new StringReader(infoString)),
                        gamePath));
        assertEquals(CorruptFileException.Type.INFO_INVALID_PATH, exception.getType());
    }

    @Test
    void testParsingOfValidInfo() {
        String validInfoString = """
                testStory.items
                
                testStory.paths
                duplicate.items
                """;

        Collection<Path> parsedGameFiles = assertDoesNotThrow(() -> GameFileCollection
                .parseInfo(new LineNumberReader(new StringReader(validInfoString)), gamePath));

        Collection<Path> validGameFiles = Set.of(
                gamePath.resolve("testStory.items"),
                gamePath.resolve("testStory.paths")
        );

        assertEquals(validGameFiles, parsedGameFiles);
    }

    @Test
    void testParsingOfInfoWithInvalidPath() {
        assertCfeType("""
                testStory.items
                
                testStoryPaths
                duplicate.items
                """);

        assertCfeType("""
                ///...:?ææåø^'\\`´..-\\\\\\
                """);
    }
}
