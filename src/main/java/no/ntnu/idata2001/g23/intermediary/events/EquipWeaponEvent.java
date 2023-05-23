package no.ntnu.idata2001.g23.intermediary.events;

import no.ntnu.idata2001.g23.model.items.Weapon;

/**
 * Indicates that the player equipped a new weapon.
 *
 * @param equippedWeapon The newly equipped weapon
 */
public record EquipWeaponEvent(
        Weapon equippedWeapon
) implements GameUpdateEvent {
    @Override
    public String getDescriptiveText() {
        return equippedWeapon != null
                ? String.format("You equipped %s as your weapon",
                equippedWeapon.getName()
        ) : "You un-equipped your weapon";
    }
}
