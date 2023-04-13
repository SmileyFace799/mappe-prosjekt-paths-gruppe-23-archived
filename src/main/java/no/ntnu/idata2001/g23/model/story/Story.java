package no.ntnu.idata2001.g23.model.story;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import no.ntnu.idata2001.g23.exceptions.unchecked.BlankStringException;
import no.ntnu.idata2001.g23.exceptions.unchecked.DuplicateElementException;
import no.ntnu.idata2001.g23.exceptions.unchecked.ElementNotFoundException;
import no.ntnu.idata2001.g23.exceptions.unchecked.NullValueException;

/**
 * A story containing passages for the player to navigate through.
 */
public class Story {
    private final String title;
    private final Map<String, Passage> passages;
    private final Passage openingPassage;

    /**
     * Makes a story.
     *
     * @param title The title of the story. Must not be null or blank.
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
        passages = new HashMap<>();
        this.openingPassage = openingPassage;
        addPassage(openingPassage);

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
        passages.put(passage.getTitle(), passage);
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
        String passageToRemoveTitle = link.getReference();
        for (Passage passage : getPassages()) {
            for (Link passageLink : passage.getLinks()) {
                if (passageLink.getReference().equals(passageToRemoveTitle)) {
                    throw new IllegalStateException(
                            "Other passages in the story link to this passage, cannot remove it");
                }
            }
        }
        passages.remove(passageToRemoveTitle);
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
        String passageName = link.getReference();
        if (!passages.containsKey(passageName)) {
            throw new ElementNotFoundException("No passage associated with Link \""
                    + link.getText() + "\" was found");
        }
        return passages.get(passageName);
    }

    /**
     * Finds and returns a list of dead links.
     * A link is considered "dead" if it refers to a passage that doesn't exist in the story.
     *
     * @return A list of all dead links in the story.
     */
    public List<Link> getBrokenLinks() {
        List<Link> brokenLinks = new ArrayList<>();
        for (Passage passage : getPassages()) {
            for (Link passageLink : passage.getLinks()) {
                if (!passages.containsKey(passageLink.getReference())) {
                    brokenLinks.add(passageLink);
                }
            }
        }
        return brokenLinks;
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
                && openingPassage.equals(story.openingPassage)
                && passages.equals(story.passages);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + title.hashCode();
        hash = 31 * hash + openingPassage.hashCode();
        hash = 31 * hash + passages.hashCode();
        return hash;
    }
}
