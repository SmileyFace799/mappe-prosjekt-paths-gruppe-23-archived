package no.ntnu.idata2001.g23.middleman.events;

import java.util.List;
import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.entities.enemies.Enemy;

/**
 * Indicates that the player attacked.
 *
 * @param player The attacking player
 * @param attack The attack done by the player
 * @param target The enemy affected by the attack
 * @param remainingEnemies Every enemy remaining after the attack
 */
public record PlayerAttackEvent(
        Player player, Action attack, Enemy target, List<Enemy> remainingEnemies
) implements GameUpdateEvent {}
