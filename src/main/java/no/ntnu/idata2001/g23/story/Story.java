package no.ntnu.idata2001.g23.story;

import java.util.Collection;
import java.util.HashMap;
import no.ntnu.idata2001.g23.exceptions.BlankStringException;
import no.ntnu.idata2001.g23.exceptions.DuplicateElementException;
import no.ntnu.idata2001.g23.exceptions.ElementNotFoundException;
import no.ntnu.idata2001.g23.exceptions.NullValueException;

/**
 * A story containing passages for the player to navigate through.
 */
public class Story {
    private final String title;
    private final HashMap<Link, Passage> passages;
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
        addPassage(openingPassage, null);

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
     * a {@code Link} is automatically created as a reference to the passage,
     * and then added to the {@code after}-passage's links.
     *
     * @param passage The passage to add to the story.
     * @param after   Another passage in the story that the added passage comes after.
     *                This should only be {@code null} when adding the opening passage.
     * @return The created link associated with the passage added.
     * @see Link
     */
    public Link addPassage(Passage passage, Passage after) {
        String passageTitle = passage.getTitle();
        if (getPassages().contains(passage)) {
            throw new DuplicateElementException("Passage \"" + passageTitle
                    + "\" is already added to the story");
        }

        Link link = new Link(passageTitle, passageTitle);
        if (after != null) {
            if (!getPassages().contains(after)) {
                throw new ElementNotFoundException("Passage \""
                        + after.getTitle() + "\" does not exist in this story");
            }
            after.addLink(link);
        } else if (!openingPassage.equals(passage)) {
            throw new NullValueException("The \"after\"-passage should only be null "
                    + "in the case of the opening passage");
        }
        passages.put(link, passage);
        return link;
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
