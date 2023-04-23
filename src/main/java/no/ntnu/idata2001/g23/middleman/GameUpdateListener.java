package no.ntnu.idata2001.g23.middleman;

import no.ntnu.idata2001.g23.middleman.events.GameUpdateEvent;

/**
 * A subscriber that gets notified whenever the state of the model updates.
 */
public interface GameUpdateListener {
    void onUpdate(GameUpdateEvent event);
}
