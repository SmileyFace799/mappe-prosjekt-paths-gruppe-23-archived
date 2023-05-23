package no.ntnu.idata2001.g23.model.misc;

import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProviderTest {
    private Provider<String> validProvider;
    private Supplier<String> testSupplier;

    @BeforeEach
    void before() {
        this.validProvider = new Provider<>();
        this.testSupplier = () -> "TEST";
        validProvider.addProvidable("test", testSupplier);
    }

    @Test
    void testProvideWithInvalidIdentifier() {
        assertThrows(IllegalArgumentException.class, () -> validProvider.provide(null));
        assertThrows(IllegalArgumentException.class, () -> validProvider.provide("  "));
        assertThrows(IllegalArgumentException.class, () -> validProvider.provide("Invalid"));
    }

    @Test
    void testProvideWithValidParameters() {
        assertEquals(testSupplier.get(), validProvider.provide("test"));
    }

    @Test
    void testAddProvidableWithInvalidIdentifier() {
        assertThrows(IllegalArgumentException.class, () ->
                validProvider.addProvidable(null, testSupplier));
        assertThrows(IllegalArgumentException.class, () ->
                validProvider.addProvidable("  ", testSupplier));
    }

    @Test
    void testAddProvidableWithInvalidSupplier() {
        assertThrows(IllegalArgumentException.class, () ->
                validProvider.addProvidable("valid", null));
    }

    @Test
    void testAddProvidableWithValidParameters() {
        assertDoesNotThrow(() -> validProvider.addProvidable("valid", () -> "VALID"));
    }
}
