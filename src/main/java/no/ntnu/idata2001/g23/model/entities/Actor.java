package no.ntnu.idata2001.g23.model.entities;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import no.ntnu.idata2001.g23.model.actions.Action;

/**
 * An interface for the AI of anything that attacks on its own, without player input.
 */
public interface Actor {

    /**
     * Determines what the actor should do, given a list of possible targets.
     *
     * @param possibleTargets The list of targets that can possibly be affected
     *                        by the actor
     * @return A map of every action to do, and a list of every target to be affected by the action
     */
    Map<Action, List<Entity>> act(Collection<Entity> possibleTargets);
}
