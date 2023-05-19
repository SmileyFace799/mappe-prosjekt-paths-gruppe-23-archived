package no.ntnu.idata2001.g23.model.story;

import java.util.ArrayList;
import java.util.List;

/**
 * A smaller part/segment of a story.
 */
public class Passage {
    private final String title;
    private final String content;
    private final ArrayList<Link> links = new ArrayList<>();

    /**
     * Makes a passage.
     *
     * @param title   The title of the passage. Must not be null or blank.
     * @param content The content of the passage. Must not be null or blank.
     */
    public Passage(String title, String content) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("String \"title\" cannot be null or blank");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("String \"Content\" cannot be null or blank");
        }
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    /**
     * Adds a link to this passage.
     *
     * @param link The link to add.
     */
    public void addLink(Link link) {
        if (link == null) {
            throw new IllegalArgumentException("\"link\" cannot be null");
        }
        if (!links.contains(link)) {
            links.add(link);
        } else {
            throw new IllegalArgumentException("Link \"" + link.getText()
                    + "\" is already added to the passage");
        }
    }

    public List<Link> getLinks() {
        return links;
    }

    /**
     * Checks if this passage has any links or not.
     *
     * @return A boolean representing if the passage has any links:
     *         <ul>
     *             <li>{@code true}: The passage has links.</li>
     *             <li>{@code false}: The passage has no links.</li>
     *         </ul>
     */
    public boolean hasLinks() {
        return !links.isEmpty();
    }

    @Override
    public String toString() {
        return "Title: " + title
                + "\nContent: " + content
                + "\nLinks: " + links;
    }

    /**
     * Test for content equality between two objects.
     *
     * @param obj The object to compare to this one
     * @return True if the argument object is a passage with matching parameters
     */
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
        Passage passage = (Passage) obj;
        return title.equals(passage.title)
                && content.equals(passage.content)
                && links.equals(passage.links);
    }

    /**
     * Compute a hashCode using the rules found in "Effective java" by Joshua Bloch.
     *
     * @return A hashCode for the passage, using all its parameters
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + title.hashCode();
        hash = 31 * hash + content.hashCode();
        hash = 31 * hash + links.hashCode();
        return hash;
    }
}
