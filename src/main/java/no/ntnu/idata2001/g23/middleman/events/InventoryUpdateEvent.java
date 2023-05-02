package no.ntnu.idata2001.g23.middleman.events;

import no.ntnu.idata2001.g23.model.itemhandling.Inventory;

/**
 * Indicates that hte player's inventory has changed
 */
public record InventoryUpdateEvent(
        Inventory inventory
) implements GameUpdateEvent {}
