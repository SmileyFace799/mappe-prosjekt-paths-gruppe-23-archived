package no.ntnu.idata2001.g23.middleman.events;

import java.util.List;
import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.story.Passage;

/**
 * Indicates that the game's current passage was changed.
 *
 * @param oldPassage The old passage the player moved from
 * @param newPassage THe new passage the player moved to
 * @param linkActions The actions executed on the player upon moving
 * @param player The player that changed the passage
 */
public record ChangePassageEvent(
        Passage oldPassage,
        Passage newPassage,
        List<Action> linkActions,
        Player player
) implements GameUpdateEvent {
    @Override
    public String getDescriptiveText() {
        String message = String.format("You moved from %s to %s",
                oldPassage.getTitle(),
                newPassage.getTitle()
        );
        if (linkActions != null && !linkActions.isEmpty()) {
            String actionsString = String.join(", ",
                    linkActions.stream().map(Action::getExecutedText).toList());
            if (actionsString.contains(",")) { //Replaces the last comma with &
                int lastCommaIndex = actionsString.lastIndexOf(",");
                actionsString = new StringBuilder(actionsString)
                        .replace(lastCommaIndex, lastCommaIndex + 1, " &")
                        .toString();
            }
            message += ". On the way, you " + actionsString;
        }
        return message;
    }
}
