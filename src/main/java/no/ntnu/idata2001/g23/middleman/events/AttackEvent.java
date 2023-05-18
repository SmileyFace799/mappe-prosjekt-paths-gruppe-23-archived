package no.ntnu.idata2001.g23.middleman.events;

import java.util.List;
import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.entities.Entity;
import no.ntnu.idata2001.g23.model.entities.enemies.Enemy;

/**
 * Indicates that an enemy has attacked.
 *
 * @param attacker The attacking enemy
 * @param attack The attack done by the attacker
 * @param targets Targets affected by the attack
 */
public record AttackEvent(
        Enemy attacker, Action attack, List<Entity> targets
) implements GameUpdateEvent {}
