package no.ntnu.idata2001.g23.story;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import no.ntnu.idata2001.g23.actions.Action;
import no.ntnu.idata2001.g23.actions.GoldAction;
import no.ntnu.idata2001.g23.actions.HealthAction;
import no.ntnu.idata2001.g23.actions.InventoryAction;
import no.ntnu.idata2001.g23.actions.ScoreAction;
import no.ntnu.idata2001.g23.exceptions.unchecked.ElementNotFoundException;
import no.ntnu.idata2001.g23.itemhandling.ItemFactory;

/**
 * A utility class for loading stories from files.
 */
public class StoryLoader {
    private StoryLoader() {
        throw new IllegalStateException("Utility class");
    }

    private static Action parseAction(String rawActionData, int lineNumber)
            throws CorruptFileException {
        String[] splitActionData = rawActionData.split(":", 2);
        if (splitActionData.length < 2) {
            throw new CorruptFileException(CorruptFileExceptionType.ACTION_INVALID_FORMAT,
                    lineNumber, rawActionData);
        }
        Action returnAction;
        String actionType = splitActionData[0].trim();
        String actionValue = splitActionData[1].trim();
        try {
            switch (actionType) {
                case "Gold" -> returnAction = new GoldAction(Integer.parseInt(actionValue));
                case "Health" -> returnAction = new HealthAction(Integer.parseInt(actionValue));
                case "Inventory" -> returnAction =
                        new InventoryAction(ItemFactory.makeItem(actionValue));
                case "Score" -> returnAction = new ScoreAction(Integer.parseInt(actionValue));
                default -> throw new CorruptFileException(
                        CorruptFileExceptionType.ACTION_INVALID_TYPE, lineNumber, actionType);
            }
        } catch (NumberFormatException | ElementNotFoundException e) {
            throw new CorruptFileException(CorruptFileExceptionType.ACTION_INVALID_VALUE,
                    lineNumber, actionValue);
        }
        return returnAction;
    }

    private static Link parseLink(String rawLinkData, int lineNumber)
            throws CorruptFileException {
        String[] splitLinkData = rawLinkData.split("]", 2);
        if (!rawLinkData.startsWith("[") || splitLinkData.length < 2) {
            throw new CorruptFileException(CorruptFileExceptionType.LINK_NO_TEXT, lineNumber);
        }
        final String linkText = splitLinkData[0].substring(1);
        rawLinkData = splitLinkData[1].trim();

        splitLinkData = rawLinkData.split("\\)", 2);
        if (!rawLinkData.startsWith("(") || splitLinkData.length < 2) {
            throw new CorruptFileException(CorruptFileExceptionType.LINK_NO_REFERENCE, lineNumber);
        }
        String linkReference = splitLinkData[0].substring(1);
        rawLinkData = splitLinkData[1].trim();

        List<Action> linkActions = new ArrayList<>();
        while (!rawLinkData.isBlank()) {
            splitLinkData = rawLinkData.split("}", 2);
            if (!rawLinkData.startsWith("{") || splitLinkData.length < 2) {
                throw new CorruptFileException(CorruptFileExceptionType.LINK_INVALID_ACTION,
                        lineNumber, rawLinkData);
            }
            linkActions.add(parseAction(splitLinkData[0].substring(1), lineNumber));
            rawLinkData = splitLinkData[1].trim();
        }
        return new Link(linkText, linkReference, linkActions);
    }

    private static Passage parsePassage(String passageTitle, LineNumberReader fileReader)
            throws IOException, CorruptFileException {
        if (passageTitle.isBlank()) {
            throw new CorruptFileException(CorruptFileExceptionType.PASSAGE_NO_NAME,
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
            throw new CorruptFileException(CorruptFileExceptionType.PASSAGE_NO_CONTENT,
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
    public static Story parseStory(LineNumberReader fileReader) throws CorruptFileException {
        Story loadedStory = null;
        try {
            String storyTitle = fileReader.readLine();
            if (storyTitle == null) {
                throw new CorruptFileException(CorruptFileExceptionType.EMPTY_FILE);
            } else if (storyTitle.isBlank() || storyTitle.startsWith("::")) {
                throw new CorruptFileException(CorruptFileExceptionType.NO_TITLE,
                        fileReader.getLineNumber());
            }
            String nextLine;
            while ((nextLine = fileReader.readLine()) != null) { //Goes through whole file

                if (nextLine.startsWith("::")) {
                    Passage loadedPassage = parsePassage(nextLine.substring(2), fileReader);
                    if (loadedStory == null) {
                        loadedStory = new Story(storyTitle, loadedPassage);
                    } else {
                        loadedStory.addPassage(loadedPassage);
                    }
                } else if (!nextLine.isBlank()) {
                    throw new CorruptFileException(CorruptFileExceptionType.INVALID_PASSAGE,
                            fileReader.getLineNumber(), nextLine);
                }
            }
            if (loadedStory == null) {
                throw new CorruptFileException(CorruptFileExceptionType.NO_PASSAGES,
                        fileReader.getLineNumber());
            }
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileExceptionType.UNKNOWN,
                    fileReader.getLineNumber());
        }
        return loadedStory;
    }

    /**
     * Loads a {@link Story} from a {@code .paths}-file.
     *
     * @param storyFileName The file name of the story to load <b>without the file extension</b>
     * @return The loaded story
     * @throws CorruptFileException If the story could not be loaded
     */
    public static Story loadStory(String storyFileName) throws CorruptFileException {
        Story loadedStory;
        try (LineNumberReader fileReader = new LineNumberReader(
                Files.newBufferedReader(
                        Path.of(storyFileName + ".paths")
                )
        )) {
            loadedStory = parseStory(fileReader);
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileExceptionType.UNKNOWN);
        }
        return loadedStory;
    }
}
