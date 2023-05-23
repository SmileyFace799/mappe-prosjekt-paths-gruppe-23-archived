package no.ntnu.idata2001.g23.intermediary.events;

import java.util.Map;
import no.ntnu.idata2001.g23.model.Game;
import no.ntnu.idata2001.g23.model.story.Passage;

/**
 * Indicates that a new game was started.
 *
 * @param game The game that was started
 * @param startPassage The passage it was started at
 * @param spritePaths A map of sprite paths for every sprite used in the game
 */
public record NewGameEvent(
        Game game,
        Passage startPassage,
        Map<String, String> spritePaths
) implements GameUpdateEvent {
    @Override
    public String getDescriptiveText() {
        return String.format("%s set out on a brand new adventure through the story of %s",
                game.getPlayer().getName(),
                game.getStory().getTitle()
        );
    }
}
