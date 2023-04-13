package no.ntnu.idata2001.g23.model.actions;

import no.ntnu.idata2001.g23.model.entities.Player;

/**
 * Changes the amount of gold the player has.
 */
public class GoldAction implements Action {
    private final int gold;

    public GoldAction(int gold) {
        this.gold = gold;
    }

    @Override
    public void execute(Player player) {
        player.changeGold(gold);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GoldAction goldAction = (GoldAction) obj;
        return gold == goldAction.gold;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Integer.hashCode(gold);
        return hash;
    }
}
