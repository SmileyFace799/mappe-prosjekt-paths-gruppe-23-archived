package no.ntnu.idata2001.g23.middleman.events;

import java.util.List;
import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.entities.Entity;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.entities.enemies.Enemy;

/**
 * Indicates that an enemy died.
 *
 * @param enemyKilled The enemy that has been killed
 * @param killer The dead enemy's killer. Can be null if the enemy died of other reasons
 * @param droppedLoot A list of actions that give any dropped loot
 */
public record EnemyDeathEvent(
        Enemy enemyKilled,
        Entity killer,
        List<Action> droppedLoot
) implements GameUpdateEvent {
    @Override
    public String getDescriptiveText() {
        String message;
        if (killer == null) {
            message = enemyKilled.getName() + " died";
        } else {
            String droppedLootString = String.join(", ",
                    droppedLoot.stream().map(Action::getExecutedText).toList());
            if (droppedLootString.contains(",")) { //Replaces the last comma with &
                int lastCommaIndex = droppedLootString.lastIndexOf(",");
                droppedLootString = new StringBuilder(droppedLootString)
                        .replace(lastCommaIndex, lastCommaIndex + 1, " &")
                        .toString();
            }
            message = String.format("%s killed %s, and %s",
                    killer instanceof Player ? "You" : killer.getName(),
                    enemyKilled.getName(),
                    droppedLootString
            );
        }
        return message;
    }
}
