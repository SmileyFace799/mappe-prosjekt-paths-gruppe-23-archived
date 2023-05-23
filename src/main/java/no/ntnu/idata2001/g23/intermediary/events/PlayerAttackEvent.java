package no.ntnu.idata2001.g23.intermediary.events;

import java.util.List;
import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.entities.enemies.Enemy;

/**
 * Indicates that the player attacked.
 *
 * @param attack           The attack done by the player
 * @param target           The enemy affected by the attack
 * @param remainingEnemies Every enemy remaining after the attack
 */
public record PlayerAttackEvent(
        Action attack, Enemy target, List<Enemy> remainingEnemies
) implements GameUpdateEvent {
    @Override
    public String getDescriptiveText() {
        return String.format("You targeted %s, and they %s",
                target.getName(),
                attack.getExecutedText()
        );
    }
}
