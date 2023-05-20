package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A utility class for parsing collections of data from a string.
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

    private static Map.Entry<String, String> parseEntry(String entryString, int lineNumber)
            throws CorruptFileException {
        String[] splitData = entryString.split(":", 2);
        if (splitData.length < 2) {
            throw new CorruptFileException(CorruptFileException.Type.ENTRY_INVALID_FORMAT,
                    lineNumber, entryString);
        }
        return new AbstractMap.SimpleEntry<>(splitData[0].trim(), splitData[1].trim());
    }

    /**
     * Parses multiple lines of text containing key/value-pairs into a {@link Map}.
     * Colons ({@code :}) are used as separators to separate the keys & values.
     *
     * <p>The keys & values are trimmed for leading & trailing spaces.</p>
     *
     * <p>An example of how the parsing works:
     * <h2>Input (Multiline {@link String}):</h2>
     * <ol>
     *     <li>""" Key1:  Value1</li>
     *     <li>k3yTw0  :v4lu3Tw0</li>
     *     <li>kE Y3: vA L uE 3 """</li>
     * </ol>
     * <h2>Output ({@link Map}<{@link String}, {@link String}>)</h2>
     * <ol>
     *     <li>"Key1": "Value1"</li>
     *     <li>"k3yTw0": "v4lu3Tw0"</li>
     *     <li>"kE Y3": "vA L uE 3"</li>
     * </ol></p>
     *
     * @param fileReader      The {@link LineNumberReader} that contains the map to parse
     * @param stopAtBlankLine If the file reader should stop parsing upon reaching a blank line
     * @param requiredKeys    A list of required keys to find. Will throw a
     *                        {@link CorruptFileException} with type {@link
     *                        CorruptFileException.Type#REQUIRED_KEY_MISSING REQUIRED_KEY_MISSING}
     *                        if any of these are not present
     * @return A parsed map with any found string keys & values
     * @throws IOException          If an I/O error occurs
     * @throws CorruptFileException If a line is missing a ":"-separator,
     *                              or a required key is missing
     */
    public static Map<String, String> parseMap(
            LineNumberReader fileReader,
            boolean stopAtBlankLine,
            String... requiredKeys
    ) throws IOException, CorruptFileException {
        Map<String, String> parsedMap = new HashMap<>();
        String nextLine;
        //Goes through one map of data
        while ((nextLine = fileReader.readLine()) != null
                && (!nextLine.isBlank() || !stopAtBlankLine)) {
            if (!nextLine.isBlank()) {
                Map.Entry<String, String> parsedEntry =
                        parseEntry(nextLine, fileReader.getLineNumber());
                parsedMap.put(parsedEntry.getKey(), parsedEntry.getValue());
            }
        }
        for (String key : requiredKeys) {
            if (!parsedMap.containsKey(key.trim())) {
                throw new CorruptFileException(CorruptFileException.Type.REQUIRED_KEY_MISSING,
                        fileReader.getLineNumber(), key);
            }
        }

        return parsedMap;
    }
}
