package no.ntnu.idata2001.g23.middleman.events;

import no.ntnu.idata2001.g23.middleman.GameUpdateListener;

/**
 * A generic event that will be passed to {@link GameUpdateListener#onUpdate(GameUpdateEvent)} )}
 * whenever the current game is updated.
 */
public interface GameUpdateEvent {
    /**
     * Returns a descriptive text string that describes the event.
     *
     * @return A string with descriptive information about the event
     */
    String getDescriptiveText();
}
