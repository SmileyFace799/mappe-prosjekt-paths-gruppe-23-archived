package no.ntnu.idata2001.g23.middleman.events;

import no.ntnu.idata2001.g23.model.story.Passage;

/**
 * Indicates that the game's current passage was changed.
 */
public record ChangePassageEvent(Passage newPassage) implements GameUpdateEvent {}
