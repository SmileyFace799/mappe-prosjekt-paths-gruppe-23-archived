package no.ntnu.idata2001.g23.intermediary.events;

import java.util.List;
import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.entities.Entity;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.entities.enemies.Enemy;

/**
 * Indicates that an enemy has attacked.
 *
 * @param attacker         The attacking enemy
 * @param attack           The attack done by the attacker
 * @param targets          Targets affected by the attack
 * @param remainingEnemies Every enemy remaining after the attack
 */
public record EnemyAttackEvent(
        Enemy attacker,
        Action attack,
        List<Entity> targets,
        List<Enemy> remainingEnemies
) implements GameUpdateEvent {
    @Override
    public String getDescriptiveText() {
        String message;
        String attackExecutedText = attack.getExecutedText();
        if (attackExecutedText != null) {
            String targetsString = String.join(", ", targets.stream().map(entity ->
                    entity instanceof Player ? "you" : entity.getName()).toList());
            if (targetsString.contains(",")) { //Replaces the last comma with &
                int lastCommaIndex = targetsString.lastIndexOf(",");
                targetsString = new StringBuilder(targetsString)
                        .replace(lastCommaIndex, lastCommaIndex + 1, " &")
                        .toString();
            }
            message = String.format("%s targeted %s, and %s %s",
                    attacker.getName(),
                    targetsString,
                    targetsString.equals("you") ? targetsString : "they",
                    attack.getExecutedText()
            );
        } else {
            message = null;
        }
        return message;
    }
}
