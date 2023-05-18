package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses multiple lines of text containing key/value-pairs into a {@link Map}.
 * Colons ({@code :}) are used as separators to separate the keys & values.
 * The key has any spaces removed & all letters converted to lowercase,
 * while the value gets trimmed for leading & trailing spaces.
 *
 * <p>An example of how the parsing works:
 * <h2>Input (Multiline {@link String}):</h2>
 * <ol>
 *     <li>"""Key1:  Value1</li>
 *     <li>k3yTw0  :v4lu3Tw0</li>
 *     <li>kE Y3: vA L uE 3"""</li>
 * </ol>
 * <h2>Output ({@link Map}<{@link String}, {@link String}>)</h2>
 * <ol>
 *     <li>"key1": "Value1"</li>
 *     <li>"k3ytw0": "v4lu3Tw0"</li>
 *     <li>"key3": "vA L uE 3"</li>
 * </ol>
 * </p>
 */
public class CollectionParserUtil {
    private CollectionParserUtil() {
        throw new IllegalStateException("Do not instantiate this class pls :)");
    }

    /**
     * Parses a line of text containing list elements into a {@link List}.
     * Commas ({@code ,}) are used as separators to separate each element.
     *
     * <p>Each element gets trimmed for leading & trailing spaces.</p>
     *
     * <p>An example of how the parsing works
     * <h2>Input ({@link String})</h2>
     * " foo, BAR ,7357 "
     * <h2>Output ({@link List}<{@link String}>)</h2>
     * "foo", "BAR", "7357"</p>
     *
     * @param listString The string to be parsed as a list
     * @return A parsed list with every element in the provided string
     */
    public static List<String> parseList(String listString) {
        return Arrays
                .stream(listString.split(","))
                .map(String::trim)
                .toList();
    }

    /**
     * Parses multiple lines of text containing key/value-pairs into a {@link Map}.
     * Colons ({@code :}) are used as separators to separate the keys & values.
     *
     * <p>The key has any spaces removed & all letters converted to lowercase,
     * while the value gets trimmed for leading & trailing spaces.</p>
     *
     * <p>An example of how the parsing works:
     * <h2>Input (Multiline {@link String}):</h2>
     * <ol>
     *     <li>"""Key1:  Value1</li>
     *     <li>k3yTw0  :v4lu3Tw0</li>
     *     <li>kE Y3: vA L uE 3"""</li>
     * </ol>
     * <h2>Output ({@link Map}<{@link String}, {@link String}>)</h2>
     * <ol>
     *     <li>"key1": "Value1"</li>
     *     <li>"k3ytw0": "v4lu3Tw0"</li>
     *     <li>"key3": "vA L uE 3"</li>
     * </ol></p>
     *
     * @param fileReader   The {@link LineNumberReader} that contains the map to parse
     * @param requiredKeys A list of required keys to find. Will throw a
     *                     {@link CorruptFileException} with type
     *                     {@link CorruptFileException.Type#REQUIRED_KEY_MISSING}
     *                     if any of these are not present
     * @return A parsed map with any found string keys & values
     * @throws IOException          If an I/O error occurs
     * @throws CorruptFileException If a line is missing a ":"-separator,
     *                              or a required key is missing
     */
    public static Map<String, String> parseMap(LineNumberReader fileReader, String... requiredKeys)
            throws IOException, CorruptFileException {
        Map<String, String> parsedMap = new HashMap<>();
        String nextLine;
        //Goes through one map of data
        while ((nextLine = fileReader.readLine()) != null && !nextLine.isBlank()) {
            String[] splitData = nextLine.split(":", 2);
            if (splitData.length < 2) {
                throw new CorruptFileException(CorruptFileException.Type.ENTRY_INVALID_FORMAT,
                        fileReader.getLineNumber(), nextLine);
            }
            parsedMap.put(splitData[0].toLowerCase()
                    .replace(" ", ""), splitData[1].trim());
        }
        for (String key : requiredKeys) {
            if (!parsedMap.containsKey(key.toLowerCase().replace(" ", ""))) {
                throw new CorruptFileException(CorruptFileException.Type.REQUIRED_KEY_MISSING,
                        fileReader.getLineNumber(), key);
            }
        }

        return parsedMap;
    }
}
