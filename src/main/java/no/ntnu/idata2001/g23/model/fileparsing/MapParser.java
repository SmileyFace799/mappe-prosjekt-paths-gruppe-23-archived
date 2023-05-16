package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

public class MapParser {
    private MapParser() {
        throw new IllegalStateException("Do not instantiate this class pls :)");
    }

    public static Map<String, String> parseMap(LineNumberReader fileReader, String... requiredKeys)
            throws IOException, CorruptFileException {
        Map<String, String> itemParameterMap = new HashMap<>();
        String nextLine;
        //Goes through one item
        while ((nextLine = fileReader.readLine()) != null && !nextLine.isBlank()) {
            String[] splitItemData = nextLine.split(":", 2);
            if (splitItemData.length < 2) {
                throw new CorruptFileException(CorruptFileException.Type.PARAMETER_INVALID_FORMAT,
                        fileReader.getLineNumber(), nextLine);
            }
            itemParameterMap.put(splitItemData[0].toLowerCase()
                    .replace(" ", ""), splitItemData[1].trim());
        }
        for (String key : requiredKeys) {
            if (!itemParameterMap.containsKey(key)) {
                throw new CorruptFileException(CorruptFileException.Type.REQUIRED_PARAMETER_MISSING,
                        fileReader.getLineNumber(), key);
            }
        }

        return itemParameterMap;
    }
}
