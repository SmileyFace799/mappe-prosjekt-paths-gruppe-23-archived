package no.ntnu.idata2001.g23.middleman.events;

import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.entities.enemies.Enemy;

/**
 * Indicates that the player has died. This should cause the game to end.
 *
 * @param player   The player that died
 * @param killedBy The enemy the player was killed by.
 *                 Can be {@code null} if the player died of other reasons
 */
public record PlayerDeathEvent(
        Player player,
        Enemy killedBy
) implements GameUpdateEvent {
    @Override
    public String getDescriptiveText() {
        return killedBy == null
                ? "You died"
                : String.format("You were killed by %s", killedBy.getName());
    }
}
