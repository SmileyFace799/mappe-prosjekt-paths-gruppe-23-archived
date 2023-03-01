package no.ntnu.idata2001.g23.actions;

import no.ntnu.idata2001.g23.entities.Player;

/**
 * Changes the amount of gold the player has.
 */
public class GoldAction implements Action {
    private int gold;

    GoldAction(int gold) {
        this.gold = gold;
    }
    @Override
    public void execute(Player player) {
        player.changeGold(gold);
    }
}
