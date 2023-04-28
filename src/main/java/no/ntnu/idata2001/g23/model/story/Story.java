package no.ntnu.idata2001.g23.model.story;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.ntnu.idata2001.g23.exceptions.unchecked.BlankStringException;
import no.ntnu.idata2001.g23.exceptions.unchecked.DuplicateElementException;
import no.ntnu.idata2001.g23.exceptions.unchecked.ElementNotFoundException;
import no.ntnu.idata2001.g23.exceptions.unchecked.EmptyArrayException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NullValueException;
import no.ntnu.idata2001.g23.model.goals.Goal;

/**
 * A story containing passages for the player to navigate through,
 * and goals for the player to achieve.
 */
public class Story {
    private final String title;
    private final Map<Link, Passage> passages;
    private final Passage openingPassage;
    private final Map<String, List<Goal>> diffucultyGoalMap;

    /**
     * Makes a story.
     *
     * @param title          The title of the story. Must not be null or blank.
     * @param openingPassage THe opening passage of the story. Must not be null.
     */
    public Story(String title, Passage openingPassage) {
        if (title == null || title.isBlank()) {
            throw new BlankStringException("String \"title\" cannot be null or blank");
        }
        if (openingPassage == null) {
            throw new NullValueException("Passage \"openingPassage\" cannot be null");
        }
        this.title = title;
        this.openingPassage = openingPassage;
        this.passages = new HashMap<>();
        addPassage(openingPassage);
        this.diffucultyGoalMap = new HashMap<>();

    }

    public String getTitle() {
        return title;
    }

    public Passage getOpeningPassage() {
        return openingPassage;
    }

    public Collection<Passage> getPassages() {
        return passages.values();
    }

    /**
     * Gets the goals for a specified difficulty.
     *
     * @param difficulty The difficulty to get the goals for
     * @return The goals for the specified difficulty
     * @throws ElementNotFoundException If the specified difficulty does not exist
     */
    public List<Goal> getGoals(String difficulty) {
        if (!diffucultyGoalMap.containsKey(difficulty)) {
            throw new ElementNotFoundException("Story has no \"" + difficulty + "\" difficulty");
        }
        return diffucultyGoalMap.get(difficulty);
    }

    /**
     * Adds a passage to the story.
     *
     * @param passage The passage to add to the story.
     */
    public void addPassage(Passage passage) {
        if (passage == null) {
            throw new NullValueException("\"passage\" cannot be null");
        }
        if (getPassages().contains(passage)) {
            throw new DuplicateElementException("Passage \"" + passage.getTitle()
                    + "\" is already added to the story");
        }
        passages.put(new Link(passage.getTitle(), passage.getTitle(), null), passage);
    }

    /**
     * Removes a passage from the story.
     *
     * @param link A link to the passage that should be removed.
     */
    public void removePassage(Link link) {
        if (link == null) {
            throw new NullValueException("\"link\" cannot be null");
        }
        if (getPassages()
                .stream()
                .flatMap(passage -> passage.getLinks().stream())
                .anyMatch(passageLink -> passageLink.equals(link))
        ) {
            throw new IllegalStateException(
                    "Other passages in the story link to this passage, cannot remove it");
        }
        passages.remove(link);
    }

    /**
     * Gets a passage in the story.
     *
     * @param link The link associated with the passage.
     * @return The passage associated with the provided link.
     */
    public Passage getPassage(Link link) {
        if (link == null) {
            throw new NullValueException("\"link\" cannot be null");
        }
        if (!passages.containsKey(link)) {
            throw new ElementNotFoundException("No passage found for reference \""
                    + link.getReference() + "\"");
        }
        return passages.get(link);
    }

    /**
     * Finds and returns a list of dead links.
     * A link is considered "dead" if it refers to a passage that doesn't exist in the story.
     *
     * @return A list of all dead links in the story.
     */
    public List<Link> getBrokenLinks() {
        return getPassages()
                .stream()
                .flatMap(passage -> passage.getLinks().stream())
                .filter(link -> !passages.containsKey(link))
                .toList();
    }

    /**
     * Sets the goals for a specified difficulty.
     *
     * @param difficulty The difficulty to set the goals for
     * @param goals The goals to set
     * @throws BlankStringException If difficulty is {@code null} or blank
     * @throws EmptyArrayException If goals is {@code null} or empty
     */
    public void setGoals(String difficulty, List<Goal> goals) {
        if (difficulty == null || difficulty.isBlank()) {
            throw new BlankStringException("String \"difficulty\" cannot be blank");
        }
        if (goals == null || goals.isEmpty()) {
            throw new EmptyArrayException("\"goals\" cannot be null or empty");
        }
        diffucultyGoalMap.put(difficulty, goals);
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

        Story story = (Story) obj;
        return title.equals(story.title)
                && getOpeningPassage().equals(story.getOpeningPassage())
                && passages.equals(story.passages)
                && diffucultyGoalMap.equals(story.diffucultyGoalMap);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + title.hashCode();
        hash = 31 * hash + openingPassage.hashCode();
        hash = 31 * hash + passages.hashCode();
        hash = 31 * hash + diffucultyGoalMap.hashCode();
        return hash;
    }
}
