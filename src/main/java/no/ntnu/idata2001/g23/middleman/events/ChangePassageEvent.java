package no.ntnu.idata2001.g23.middleman.events;

import no.ntnu.idata2001.g23.model.story.Passage;

/**
 * Indicates that the game's current passage was changed.
 */
public record ChangePassageEvent(
        Passage oldPassage,
        Passage newPassage
) implements GameUpdateEvent {
    @Override
    public String getDescriptiveText() {
        return String.format("You moved from %s to %s",
                oldPassage.getTitle(),
                newPassage.getTitle()
        );
    }
}
