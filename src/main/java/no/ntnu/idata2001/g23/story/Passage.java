package no.ntnu.idata2001.g23.story;

import no.ntnu.idata2001.g23.exceptions.BlankStringException;
import no.ntnu.idata2001.g23.exceptions.DuplicateLinkException;
import java.util.ArrayList;
import java.util.List;

/**
 * A smaller part/segment of a story.
 */
public class Passage {
    private final String title;
    private final String content;
    private final ArrayList<Link> links = new ArrayList<>();

    public Passage(String title, String content) {
        if (title == null || title.isBlank()) {
            throw new BlankStringException("title");
        }
        if (content == null || content.isBlank()) {
            throw new BlankStringException("content");
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
        if (!links.contains(link)) {
            links.add(link);
        } else {
            throw new DuplicateLinkException("\"" + link.getText() + "\" is already added to the passage");
        }
    }

    public List<Link> getLinks() {
        return links;
    }

    /**
     * Checks if this passage has any links or not
     *
     * @return A boolean representing if the passage has any links:
     * <ul>
     *     <li>{@code true}: The passage has links.</li>
     *     <li>{@code false}: The passage has no links.</li>
     * </ul>
     */
    public boolean hasLinks() {
        return !links.isEmpty();
    }

    @Override
    public String toString() {
        return "Title: " + title
                + "Content: " + content
                + "Links: " + links;
    }

    /**
     * Checks if another object equals this passage.
     *
     * @param obj The object to check if equals this passage.
     * @return True if checked object is an instance of passage,
     * with matching title, content & links.
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
        return title.equals(passage.getTitle())
                && content.equals(passage.getContent())
                && links.equals(passage.getLinks());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + title.hashCode();
        hash = 31 * hash + content.hashCode();
        hash = 31 * hash + links.hashCode();
        return hash;
    }
}
