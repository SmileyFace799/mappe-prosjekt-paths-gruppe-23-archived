package no.ntnu.idata2001.g23.intermediary.events;

import no.ntnu.idata2001.g23.model.entities.enemies.Enemy;
import no.ntnu.idata2001.g23.model.story.Passage;

/**
 * Indicates that the player tried changing passage, but was blocked by an enemy.
 *
 * @param oldPassage The old passage the player attempted to move from
 * @param newPassage THe new passage the player attempted to move to
 * @param blockedBy  The enemy that blocked the player from moving
 */
public record ChangePassageFailedEvent(
        Passage oldPassage,
        Passage newPassage,
        Enemy blockedBy
) implements GameUpdateEvent {
    @Override
    public String getDescriptiveText() {
        return String.format("You tried moving from %s to %s, but you were stopped by %s",
                oldPassage.getTitle(),
                newPassage.getTitle(),
                blockedBy.getName()
        );
    }
}
