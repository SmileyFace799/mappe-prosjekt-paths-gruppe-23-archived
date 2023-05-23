package no.ntnu.idata2001.g23.intermediary.events;

import java.util.List;
import no.ntnu.idata2001.g23.model.goals.Goal;

/**
 * Indicates that the player has fulfilled all the goals. This should cause the game to end.
 *
 * @param goals The goals that the player has fulfilled
 */
public record AllGoalsFulfilledEvent(
        List<Goal> goals
) implements GameUpdateEvent {
    @Override
    public String getDescriptiveText() {
        return "You completed all the goals!\n\n" + String.join("\n",
                goals.stream().map(Goal::getDescriptiveText).toList());
    }
}
