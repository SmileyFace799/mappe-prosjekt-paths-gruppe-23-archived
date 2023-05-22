package no.ntnu.idata2001.g23.middleman.events;

import no.ntnu.idata2001.g23.model.entities.Player;

/**
 * Indicates that the player has died. This should cause the game to end.
 *
 * @param player   The player that died
 * @param cause The event that's the of death
 */
public record PlayerDeathEvent(
        Player player,
        GameUpdateEvent cause
) implements GameUpdateEvent {
    @Override
    public String getDescriptiveText() {
        return "You died\n\nCause of death:\n" + cause.getDescriptiveText();
    }
}
