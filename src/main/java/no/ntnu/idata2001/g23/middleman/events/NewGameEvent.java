package no.ntnu.idata2001.g23.middleman.events;

import no.ntnu.idata2001.g23.model.Game;
import no.ntnu.idata2001.g23.model.story.Passage;

/**
 * Indicates that a new game was started.
 *
 * @param game The game that was started
 * @param startPassage The passage it was started at
 */
public record NewGameEvent(
        Game game,
        Passage startPassage
) implements GameUpdateEvent {}
