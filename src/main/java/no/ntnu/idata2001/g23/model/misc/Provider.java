package no.ntnu.idata2001.g23.model.misc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Stores {@link Supplier}s with instructions on how to make objects,
 * and provides copies of these objects given an identifier.
 * This is similar to the factory design pattern,
 * although without the list of providable items being hard coded.
 *
 * @param <T> The object type that this provider can provide
 */
public class Provider<T> {
    private final Map<String, Supplier<T>> supplierMap;

    /**
     * Makes an empty provider.
     */
    public Provider() {
        this.supplierMap = new HashMap<>();
    }

    /**
     * Gets all identifiers associated with any providable object.
     *
     * @return A collection of all identifiers associated with any providable object.
     */
    public Collection<String> getIdentifiers() {
        return supplierMap.keySet();
    }

    /**
     * Adds an object that the provider can provide.
     *
     * @param identifier The unique identifier for the object to be provided
     * @param supplier A supplier that makes the object to be provided
     * @throws IllegalArgumentException If identifier is {@code null} or blank
     * @throws IllegalArgumentException If supplier is {@code null}
     */
    public void addProvidable(String identifier, Supplier<T> supplier) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("String \"identifier\" cannot be blank");
        }
        if (supplier == null) {
            throw new IllegalArgumentException("\"supplier\" cannot be null");
        }
        supplierMap.put(identifier, supplier);
    }

    /**
     * Provide an object given it's identifier.
     *
     * @param identifier The unique identifier for the object to provide
     * @return The provided object
     * @throws IllegalArgumentException If the identifier is not associated with any object
     */
    public T provide(String identifier) {
        if (identifier == null || identifier.isBlank() || !supplierMap.containsKey(identifier)) {
            throw new IllegalArgumentException(
                    "No object associated with the specified identifier");
        }
        return supplierMap.get(identifier).get();
    }
}
