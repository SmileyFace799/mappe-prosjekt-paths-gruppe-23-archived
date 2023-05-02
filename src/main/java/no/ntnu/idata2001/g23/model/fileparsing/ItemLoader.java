package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.itemhandling.ItemProvider;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.UsableItem;
import no.ntnu.idata2001.g23.model.items.Weapon;

/**
 * Loads items from a {@code .items}-file.
 */
public class ItemLoader {
    private ItemLoader() {
        throw new IllegalStateException("Utility class");
    }

    private static void hasParameters(Set<String> keySet, int lineNumber, String... parameters)
            throws CorruptFileException {
        for (String parameter : parameters) {
            if (!keySet.contains(parameter)) {
                throw new CorruptFileException(CorruptFileException.Type.ITEM_MISSING_PARAMETERS,
                        lineNumber, parameter);
            }
        }
    }

    /**
     * Parses an item
     *
     * @param name The name of the item to parse.
     * @param fileReader A {@link LineNumberReader} containing the item to parse
     * @param itemProvider An {@link ItemProvider} to provide items.
     *                     Used in cases where an item can create other items
     * @return The parsed item
     * @throws IOException If the file reader cannot be read
     * @throws CorruptFileException If the item cannot be parsed
     */
    private static Supplier<Item> parseItem(String name, LineNumberReader fileReader, ItemProvider itemProvider)
            throws IOException, CorruptFileException {
        String type = fileReader.readLine();
        if (!type.startsWith("-")) {
            throw new CorruptFileException(CorruptFileException.Type.ITEM_NO_TYPE,
                    fileReader.getLineNumber());
        }
        Map<String, String> itemParameterMap = new HashMap<>();
        String nextLine;
        //Goes through one item
        while ((nextLine = fileReader.readLine()) != null && !nextLine.isBlank()) {
            String[] splitItemData = nextLine.split(":", 2);
            if (splitItemData.length < 2) {
                throw new CorruptFileException(CorruptFileException.Type.ITEM_INVALID_FORMAT,
                        fileReader.getLineNumber(), nextLine);
            }
            itemParameterMap.put(splitItemData[0].toLowerCase()
                    .replace(" ", ""), splitItemData[1].trim());
        }
        Supplier<Item> itemSupplier;
        try {
            switch (type.substring(1).toLowerCase().replace(" ", "")) {
                case "meleeweapon" -> {
                    hasParameters(itemParameterMap.keySet(), fileReader.getLineNumber(),
                            Parameters.getWeaponParameters());
                    int damage = Integer.parseInt(itemParameterMap.get(Parameters.DAMAGE));
                    double critChance = Double.parseDouble(itemParameterMap.get(Parameters.CRIT_CHANCE));
                    int value = Integer.parseInt(itemParameterMap.get(Parameters.VALUE));
                    itemSupplier = () -> new Weapon(damage, critChance, value, name,
                            itemParameterMap.get(Parameters.DESCRIPTION));
                }
                case "usable" -> {
                    hasParameters(itemParameterMap.keySet(), fileReader.getLineNumber(),
                            Parameters.getUsableParameters());
                    int value = Integer.parseInt(itemParameterMap.get(Parameters.VALUE));
                    Action onUse = ActionParser.parseAction(
                            itemParameterMap
                                    .get(Parameters.ON_USE)
                                    .replaceAll("[{}]", "")
                                    .trim(),
                            fileReader.getLineNumber(), itemProvider);
                    itemSupplier = () -> new UsableItem(value, name,
                            itemParameterMap.get(Parameters.DESCRIPTION), onUse);
                }
                default -> throw new CorruptFileException(
                        CorruptFileException.Type.ITEM_INVALID_TYPE,
                        fileReader.getLineNumber());
            }
        } catch (NumberFormatException nfe) {
            throw new CorruptFileException(CorruptFileException.Type.ITEM_INVALID_PARAMETER_VALUE,
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
                        throw new CorruptFileException(CorruptFileException.Type.ITEM_NO_NAME,
                                fileReader.getLineNumber());
                    }
                    String itemName = nextLine.substring(1);
                    itemProvider.addProvidableItem(itemName, parseItem(itemName, fileReader, itemProvider));
                } else if (!nextLine.isBlank()) {
                    throw new CorruptFileException(CorruptFileException.Type.INVALID_ITEM,
                            fileReader.getLineNumber());
                }
            }
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN,
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
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN);
        }
        return loadedItems;
    }

    private static class Parameters {
        public static final String VALUE = "value";
        public static final String DESCRIPTION = "description";
        public static final String DAMAGE = "damage";
        public static final String CRIT_CHANCE = "critchance";
        public static final String ON_USE = "onuse";

        private Parameters() {
            throw new IllegalStateException("Utility class");
        }

        public static String[] getWeaponParameters() {
            return new String[]{VALUE, DESCRIPTION, DAMAGE, CRIT_CHANCE};
        }

        public static  String[] getUsableParameters() {
            return new String[]{VALUE, DESCRIPTION, ON_USE};
        }
    }
}
