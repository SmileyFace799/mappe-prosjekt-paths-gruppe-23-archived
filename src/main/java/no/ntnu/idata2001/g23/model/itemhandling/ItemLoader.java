package no.ntnu.idata2001.g23.model.itemhandling;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.Weapon;
import no.ntnu.idata2001.g23.model.story.CorruptFileException;
import no.ntnu.idata2001.g23.model.story.CorruptFileExceptionType;

/**
 * Loads items from a {@code .items}-file.
 */
public class ItemLoader {
    private ItemLoader() {
        throw new IllegalStateException("Utility class");
    }

    private static Supplier<Item> parseItem(String name, LineNumberReader fileReader)
            throws IOException, CorruptFileException {
        String type = fileReader.readLine();
        if (!type.startsWith("-")) {
            throw new CorruptFileException(CorruptFileExceptionType.ITEM_NO_TYPE,
                    fileReader.getLineNumber());
        }
        Map<String, String> allItemData = new HashMap<>();
        String nextLine;
        //Goes through one item
        while ((nextLine = fileReader.readLine()) != null && !nextLine.isBlank()) {
            String[] splitItemData = nextLine.split(":", 2);
            if (splitItemData.length < 2) {
                throw new CorruptFileException(CorruptFileExceptionType.ITEM_INVALID_FORMAT,
                        fileReader.getLineNumber(), nextLine);
            }
            allItemData.put(splitItemData[0].toLowerCase()
                    .replace(" ", ""), splitItemData[1].trim());
        }
        Supplier<Item> itemSupplier;
        switch (type.substring(1).toLowerCase().replace(" ", "")) {
            case "meleeweapon" -> {
                if (!allItemData.keySet().containsAll(
                        Set.of("damage", "critchance", "value", "description"))) {
                    throw new CorruptFileException(CorruptFileExceptionType.ITEM_MISSING_PARAMETERS,
                            fileReader.getLineNumber());
                }
                itemSupplier = () -> new Weapon(
                        Integer.parseInt(allItemData.get("damage")),
                        Double.parseDouble(allItemData.get("critchance")),
                        Integer.parseInt(allItemData.get("value")),
                        name,
                        allItemData.get("description")
                );
            }
            default -> throw new CorruptFileException(CorruptFileExceptionType.ITEM_INVALID_TYPE,
                    fileReader.getLineNumber());
        }
        return itemSupplier;
    }

    /**
     * Parses items from a {@link LineNumberReader},
     * and returns an {@link ItemProvider} that can provide all the items.
     *
     * @param fileReader A {@link LineNumberReader} that contains some items.
     * @return An {@link ItemProvider} that can provide all the parsed items
     * @throws CorruptFileException If the items could not be parsed
     */
    public static ItemProvider parseItems(LineNumberReader fileReader) throws CorruptFileException {
        ItemProvider itemProvider = new ItemProvider();
        try {
            String nextLine;
            //Goes through entire file
            while ((nextLine = fileReader.readLine()) != null) {
                if (nextLine.startsWith("#")) {
                    if (nextLine.length() == 1) {
                        throw new CorruptFileException(CorruptFileExceptionType.ITEM_NO_NAME,
                                fileReader.getLineNumber());
                    }
                    String itemName = nextLine.substring(1);
                    itemProvider.addProvidableItem(itemName, parseItem(itemName, fileReader));
                } else if (!nextLine.isBlank()) {
                    throw new CorruptFileException(CorruptFileExceptionType.INVALID_ITEM,
                            fileReader.getLineNumber());
                }
            }
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileExceptionType.UNKNOWN,
                    fileReader.getLineNumber());
        }
        return itemProvider;
    }

    /**
     * Loads items from a {@code .items}-file,
     * and returns an {@link ItemProvider} that can provide all the items.
     *
     * @param itemsFilePath The file path of the items to load
     * @return An {@link ItemProvider} that can provide all the loaded items
     * @throws CorruptFileException If the items could not be loaded
     */
    public static ItemProvider loadItems(String itemsFilePath) throws CorruptFileException {
        ItemProvider loadedItems;
        try (LineNumberReader fileReader = new LineNumberReader(
                Files.newBufferedReader(
                        Path.of(itemsFilePath)
                )
        )) {
            loadedItems = parseItems(fileReader);
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileExceptionType.UNKNOWN);
        }
        return loadedItems;
    }
}
