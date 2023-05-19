package no.ntnu.idata2001.g23.middleman.events;

import no.ntnu.idata2001.g23.model.items.Weapon;

/**
 * Indicates that the player equipped a new weapon.
 *
 * @param equippedWeapon The newly equipped weapon
 */
public record EquipWeaponEvent(
        Weapon equippedWeapon
) implements GameUpdateEvent {}
