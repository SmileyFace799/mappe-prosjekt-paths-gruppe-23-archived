package no.ntnu.idata2001.g23.goals;

import java.util.List;
import no.ntnu.idata2001.g23.actions.Action;
import no.ntnu.idata2001.g23.entities.Player;

/**
 * Represents an expected inventory of items.
 */
public class InventoryGoal implements Goal {
    private List<Action> mandatoryItems;

    public InventoryGoal(List<Action> mandatoryItems) {
        this.mandatoryItems = mandatoryItems;
    }

    @Override
    public boolean isFulfilled(Player player) {
        //TODO: add ifFulfilled InventoryGoal
    }
}