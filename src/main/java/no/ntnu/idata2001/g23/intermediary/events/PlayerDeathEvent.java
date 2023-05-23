package no.ntnu.idata2001.g23.intermediary.events;

/**
 * Indicates that the player has died. This should cause the game to end.
 *
 * @param cause The event that's the of death
 */
public record PlayerDeathEvent(
        GameUpdateEvent cause
) implements GameUpdateEvent {
    @Override
    public String getDescriptiveText() {
        return "You died\n\nCause of death:\n" + cause.getDescriptiveText();
    }
}
