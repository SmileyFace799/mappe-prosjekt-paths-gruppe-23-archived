package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Supplier;
import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.items.UsableItem;
import no.ntnu.idata2001.g23.model.items.Weapon;
import no.ntnu.idata2001.g23.model.misc.Provider;

/**
 * Loads items from a {@code .items}-file.
 */
public class ItemLoader {
    private ItemLoader() {
        throw new IllegalStateException("Do not instantiate this class pls :)");
    }

    /**
     * Parses an item.
     *
     * @param name The name of the item to parse.
     * @param fileReader A {@link LineNumberReader} containing the item to parse
     * @param itemProvider An {@link Provider} to provide items.
     *                     Used in cases where an item can create other items
     * @return The parsed item
     * @throws IOException If the file reader cannot be read
     * @throws CorruptFileException If the item cannot be parsed
     */
    private static Supplier<Item> parseItem(
            String name, LineNumberReader fileReader, Provider<Item> itemProvider)
            throws IOException, CorruptFileException {
        String type = fileReader.readLine();
        if (!type.startsWith("-")) {
            throw new CorruptFileException(CorruptFileException.Type.ITEM_NO_TYPE,
                    fileReader.getLineNumber());
        }

        Supplier<Item> itemSupplier;
        try {
            switch (type.substring(1).toLowerCase().replace(" ", "")) {
                case "basic" -> {
                    Map<String, String> itemParameterMap = CollectionParserUtil
                            .parseMap(fileReader, true, Parameters.getBasicRequired());
                    itemSupplier = () -> new Item(name,
                            itemParameterMap.get(Parameters.DESCRIPTION));
                }
                case "weapon" -> {
                    Map<String, String> itemParameterMap = CollectionParserUtil
                            .parseMap(fileReader, true, Parameters.getWeaponRequired());
                    int damage = Integer.parseInt(itemParameterMap.get(Parameters.DAMAGE));
                    itemSupplier = () -> new Weapon(damage, name,
                            itemParameterMap.get(Parameters.DESCRIPTION));
                }
                case "usable" -> {
                    Map<String, String> itemParameterMap = CollectionParserUtil
                            .parseMap(fileReader, true, Parameters.getUsableRequired());
                    Action onUse = ActionParser.parseAction(
                            itemParameterMap
                                    .get(Parameters.ON_USE)
                                    .replaceAll("[{}]", "")
                                    .trim(),
                            fileReader.getLineNumber(), itemProvider);
                    itemSupplier = () -> new UsableItem(name,
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
     * and returns a {@link Provider} that can provide all the items.
     *
     * @param fileReader A {@link LineNumberReader} that contains some items.
     * @return A {@link Provider} that can provide all the parsed items
     * @throws CorruptFileException If the items could not be parsed
     */
    public static Provider<Item> parseItems(LineNumberReader fileReader)
            throws CorruptFileException {
        Provider<Item> provider = new Provider<>();
        try {
            String nextLine;
            //Goes through entire file
            while ((nextLine = fileReader.readLine()) != null) {
                if (nextLine.startsWith("?")) {
                    if (nextLine.length() == 1) {
                        throw new CorruptFileException(CorruptFileException.Type.ITEM_NO_NAME,
                                fileReader.getLineNumber());
                    }
                    String itemName = nextLine.substring(1).trim();
                    provider.addProvidable(itemName, parseItem(itemName, fileReader, provider));
                } else if (!nextLine.isBlank()) {
                    throw new CorruptFileException(CorruptFileException.Type.INVALID_ITEM,
                            fileReader.getLineNumber());
                }
            }
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN_ITEMS,
                    fileReader.getLineNumber());
        }
        return provider;
    }

    /**
     * Loads items from a {@code .items}-file,
     * and returns a {@link Provider} that can provide all the items.
     *
     * @param itemsFilePath The file path of the items to load
     * @return A {@link Provider} that can provide all the loaded items
     * @throws CorruptFileException If the items could not be loaded
     */
    public static Provider<Item> loadItems(Path itemsFilePath) throws CorruptFileException {
        Provider<Item> itemProvider;
        try (LineNumberReader fileReader = new LineNumberReader(
                Files.newBufferedReader(itemsFilePath)
        )) {
            itemProvider = parseItems(fileReader);
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN_ITEMS);
        }
        return itemProvider;
    }

    private static class Parameters {
        public static final String DESCRIPTION = "Description";
        public static final String DAMAGE = "Damage";
        public static final String ON_USE = "On Use";

        private Parameters() {
            throw new IllegalStateException("Do not instantiate this class pls :)");
        }

        public static String[] getBasicRequired() {
            return new String[]{DESCRIPTION};
        }

        public static String[] getWeaponRequired() {
            return new String[]{DESCRIPTION, DAMAGE};
        }

        public static  String[] getUsableRequired() {
            return new String[]{DESCRIPTION, ON_USE};
        }
    }
}
