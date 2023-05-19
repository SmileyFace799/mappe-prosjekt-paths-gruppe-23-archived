package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CollectionParserUtilTest {
    /**
     * Asserts that a {@link CorruptFileException} is thrown,
     * and asserts that the exception thrown is of the specified type.
     *
     * @param mapString The map string to parse
     * @param type The {@link CorruptFileException.Type} to check for
     */
    private void assertCfeType(
            String mapString, CorruptFileException.Type type, String... requiredKeys) {
        CorruptFileException exception = assertThrows(CorruptFileException.class, () -> CollectionParserUtil
                .parseMap(new LineNumberReader(new StringReader(mapString)), true, requiredKeys));
        assertEquals(type, exception.getType());
    }

    @Test
    void testParsingOfValidMap() {
        String validMapString = """
                Key1:  Value1
                k3yTw0  :v4lu3Tw0
                kE Y3: vA L uE 3
                """;

        Map<String, String> loadedMap = assertDoesNotThrow(() -> CollectionParserUtil
                .parseMap(new LineNumberReader(new StringReader(validMapString)), true,
                        "keY 3", "K3Y tW0"));

        Map<String, String> validMap = Map.of(
                "key1", "Value1",
                "k3ytw0", "v4lu3Tw0",
                "key3", "vA L uE 3"
        );

        assertEquals(validMap, loadedMap);
    }

    @Test
    void testParsingOfMapWithInvalidEntryFormat() {
        assertCfeType("""
                Key1:  Value1
                k3yTw0 v4lu3Tw0
                kE Y3: vA L uE 3
                """, CorruptFileException.Type.ENTRY_INVALID_FORMAT);
    }

    @Test
    void testParsingOfMapWithMissingRequiredKeys() {
        assertCfeType("""
                Key1:  Value1
                k3yTw0  :v4lu3Tw0
                kE Y3: vA L uE 3
                """, CorruptFileException.Type.REQUIRED_KEY_MISSING, "key1", "key4");
    }
}
