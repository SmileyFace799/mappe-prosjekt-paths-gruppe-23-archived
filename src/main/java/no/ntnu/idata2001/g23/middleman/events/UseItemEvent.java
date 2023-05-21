package no.ntnu.idata2001.g23.middleman.events;

import no.ntnu.idata2001.g23.model.entities.Entity;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.items.UsableItem;

/**
 * Indicates that hte player's inventory has changed.
 *
 * @param usedItem The item which was used
 * @param entity The entity who used the item
 */
public record UseItemEvent(
        UsableItem usedItem, Entity entity
) implements GameUpdateEvent {
    @Override
    public String getDescriptiveText() {
        String message;
        if (entity instanceof Player) {
            message = String.format("You used %s, which %s",
                    usedItem.getName(),
                    usedItem.getUseAction().getDescriptiveText()
            );
        } else {
            message = null;
        }
        return message;
    }
}
