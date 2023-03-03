package no.ntnu.idata2001.g23.story;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
    private final Map<Link, Passage> passages;
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
        addPassage(new Link("Opening passage", openingPassage.getTitle(), null), openingPassage);

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
     * Adds a passage to the story. When adding a passage,
     * a {@code Link} must be provided as a way to get to the passage.
     *
     * @param link THe link that leads to the passage.
     * @param passage The passage to add to the story.
     * @see Link
     */
    public void addPassage(Link link, Passage passage) {
        if (link == null) {
            throw new NullValueException("\"link\" cannot be null");
        }
        if (passages.containsKey(link)) {
            throw new DuplicateElementException("Link \"" + link
                    + "\" already links to a passage in the story");
        }
        passages.put(link, passage);
    }

    /**
     * Gets a passage in the story.
     *
     * @param link The link associated with the passage.
     * @return The passage associated with the provided link.
     */
    public Passage getPassage(Link link) {
        if (!passages.containsKey(link)) {
            throw new ElementNotFoundException("No passage associated with Link \""
                    + link.getText() + "\" was found");
        }
        return passages.get(link);
    }
}
