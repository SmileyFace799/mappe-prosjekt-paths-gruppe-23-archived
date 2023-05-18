package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.misc.Provider;
import no.ntnu.idata2001.g23.model.story.Link;
import no.ntnu.idata2001.g23.model.story.Passage;
import no.ntnu.idata2001.g23.model.story.Story;

/**
 * Loads a story from a {@code .paths}-file.
 */
public class StoryLoader {
    private final Provider<Item> itemProvider;

    public StoryLoader(Provider<Item> itemProvider) {
        this.itemProvider = itemProvider;
    }

    private Link parseLink(String rawLinkData, int lineNumber)
            throws CorruptFileException {
        String[] splitLinkData = rawLinkData.split("]", 2);
        if (!rawLinkData.startsWith("[") || splitLinkData.length < 2) {
            throw new CorruptFileException(CorruptFileException.Type.LINK_NO_TEXT, lineNumber);
        }
        final String linkText = splitLinkData[0].substring(1);
        rawLinkData = splitLinkData[1].trim();

        splitLinkData = rawLinkData.split("\\)", 2);
        if (!rawLinkData.startsWith("(") || splitLinkData.length < 2) {
            throw new CorruptFileException(CorruptFileException.Type.LINK_NO_REFERENCE, lineNumber);
        }
        String linkReference = splitLinkData[0].substring(1);
        rawLinkData = splitLinkData[1].trim();

        List<Action> linkActions = new ArrayList<>();
        while (!rawLinkData.isBlank()) {
            splitLinkData = rawLinkData.split("}", 2);
            if (!rawLinkData.startsWith("{") || splitLinkData.length < 2) {
                throw new CorruptFileException(CorruptFileException.Type.LINK_INVALID_ACTION,
                        lineNumber, rawLinkData);
            }
            linkActions.add(ActionParser.parseAction(
                    splitLinkData[0].substring(1), lineNumber, itemProvider));
            rawLinkData = splitLinkData[1].trim();
        }
        Link link = new Link(linkText, linkReference);
        for (Action linkAction : linkActions) {
            link.addAction(linkAction);
        }
        return link;
    }

    private Passage parsePassage(String passageTitle, LineNumberReader fileReader)
            throws IOException, CorruptFileException {
        if (passageTitle.isBlank()) {
            throw new CorruptFileException(CorruptFileException.Type.PASSAGE_NO_NAME,
                    fileReader.getLineNumber());
        }
        StringBuilder passageContent = new StringBuilder();
        List<Link> passageLinks = new ArrayList<>();
        String nextLine;
        //Goes through one passage
        while ((nextLine = fileReader.readLine()) != null && !nextLine.isBlank()) {
            if ((nextLine.contains("[") && nextLine.contains("]"))
                    || (nextLine.contains("(") && nextLine.contains(")"))) {
                passageLinks.add(parseLink(nextLine, fileReader.getLineNumber()));
            } else {
                if (!passageContent.isEmpty()) {
                    passageContent.append("\n");
                }
                passageContent.append(nextLine);
            }
        }
        if (passageContent.isEmpty()) {
            throw new CorruptFileException(CorruptFileException.Type.PASSAGE_NO_CONTENT,
                    fileReader.getLineNumber());
        }
        Passage returnPassage = new Passage(passageTitle, passageContent.toString());
        for (Link passageLink : passageLinks) {
            returnPassage.addLink(passageLink);
        }
        return returnPassage;
    }

    /**
     * Parses a story from a {@link LineNumberReader}.
     *
     * @param fileReader A {@link LineNumberReader} that contains a story
     * @return The parsed story
     * @throws CorruptFileException If the story could not be parsed
     */
    public Story parseStory(LineNumberReader fileReader) throws CorruptFileException {
        Story loadedStory;
        try {
            String storyTitle = fileReader.readLine();
            if (storyTitle == null) {
                throw new CorruptFileException(CorruptFileException.Type.EMPTY_FILE);
            } else if (storyTitle.isBlank()
                    || storyTitle.startsWith("::")
                    || storyTitle.startsWith("#")) {
                throw new CorruptFileException(CorruptFileException.Type.NO_TITLE,
                        fileReader.getLineNumber());
            }
            List<Passage> passages = new ArrayList<>();
            String nextLine;
            while ((nextLine = fileReader.readLine()) != null) { //Goes through whole file

                if (nextLine.startsWith("::")) {
                    passages.add(parsePassage(nextLine.substring(2), fileReader));
                } else if (!nextLine.isBlank()) {
                    throw new CorruptFileException(CorruptFileException.Type.INVALID_PASSAGE,
                            fileReader.getLineNumber(), nextLine);
                }
            }
            if (passages.isEmpty()) {
                throw new CorruptFileException(CorruptFileException.Type.NO_PASSAGES,
                        fileReader.getLineNumber());
            }
            loadedStory = new Story(storyTitle, passages.remove(0));
            passages.forEach(loadedStory::addPassage);
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN_STORY,
                    fileReader.getLineNumber());
        }
        return loadedStory;
    }

    /**
     * Loads a {@link Story} from a {@code .paths}-file.
     *
     * @param storyFilePath The file path of the story to load
     * @return The loaded story
     * @throws CorruptFileException If the story could not be loaded
     */
    public Story loadStory(Path storyFilePath) throws CorruptFileException {
        Story story;
        try (LineNumberReader fileReader = new LineNumberReader(
                Files.newBufferedReader(storyFilePath)
        )) {
            story = parseStory(fileReader);
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN_STORY);
        }
        return story;
    }
}
