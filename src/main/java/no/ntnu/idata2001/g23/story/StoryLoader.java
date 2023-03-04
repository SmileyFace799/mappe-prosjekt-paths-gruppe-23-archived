package no.ntnu.idata2001.g23.story;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import no.ntnu.idata2001.g23.actions.Action;
import no.ntnu.idata2001.g23.actions.GoldAction;
import no.ntnu.idata2001.g23.actions.HealthAction;
import no.ntnu.idata2001.g23.actions.InventoryAction;
import no.ntnu.idata2001.g23.actions.ScoreAction;
import no.ntnu.idata2001.g23.exceptions.checked.CorruptFileException;
import no.ntnu.idata2001.g23.exceptions.unchecked.ElementNotFoundException;
import no.ntnu.idata2001.g23.itemhandling.ItemFactory;

/**
 * A utility class for loading stories from files.
 */
public class StoryLoader {
    private StoryLoader() {
        throw new IllegalStateException("Utility class");
    }

    private static String lineText(int lineNumber) {
        return "(Line " + lineNumber + ")";
    }

    private static Action makeAction(String rawActionData, int lineNumber)
            throws CorruptFileException {
        String[] splitActionData = rawActionData.split(":", 2);
        if (splitActionData.length < 2) {
            throw new CorruptFileException("Couldn't find the \":\"-separator that separates the "
                    + "action type & value " + lineText(lineNumber));
        }
        Action returnAction;
        String actionType = splitActionData[0].trim();
        String actionValue = splitActionData[1].trim();
        switch (actionType) {
            case "Gold" -> {
                try {
                    returnAction = new GoldAction(Integer.parseInt(actionValue));
                } catch (NumberFormatException nfe) {
                    throw new CorruptFileException("Invalid value for a GoldAction: \""
                            + actionValue + "\" " + lineText(lineNumber));
                }
            }
            case "Health" -> {
                try {
                    returnAction = new HealthAction(Integer.parseInt(actionValue));
                } catch (NumberFormatException nfe) {
                    throw new CorruptFileException("Invalid value for a HealthAction: \""
                            + actionValue + "\" " + lineText(lineNumber));
                }
            }
            case "Inventory" -> {
                try {
                    returnAction = new InventoryAction(ItemFactory.makeItem(actionValue));
                } catch (ElementNotFoundException e) {
                    throw new CorruptFileException("Invalid value for a InventoryAction: "
                            + "No item with name\"" + actionValue + "\" was found. "
                            + lineText(lineNumber));
                }
            }
            case "Score" -> {
                try {
                    returnAction = new ScoreAction(Integer.parseInt(actionValue));
                } catch (NumberFormatException nfe) {
                    throw new CorruptFileException("Invalid value for a ScoreAction: \""
                            + actionValue + "\" " + lineText(lineNumber));
                }
            }
            default -> throw new CorruptFileException("Invalid action type: \"" + actionType
                    + "\" " + lineText(lineNumber));
        }
        return returnAction;
    }

    private static Link makeLink(String rawLinkData, int lineNumber)
            throws CorruptFileException {
        String[] splitLinkData = rawLinkData.split("]", 2);
        if (!rawLinkData.startsWith("[") || splitLinkData.length < 2) {
            throw new CorruptFileException("Cannot find link text " + lineText(lineNumber));
        }
        String linkText = splitLinkData[0].substring(1);
        rawLinkData = splitLinkData[1].trim();

        splitLinkData = rawLinkData.split("\\)", 2);
        if (!rawLinkData.startsWith("(") || splitLinkData.length < 2) {
            throw new CorruptFileException("Cannot find link reference " + lineText(lineNumber));
        }
        String linkReference = splitLinkData[0].substring(1);
        rawLinkData = splitLinkData[1].trim();

        List<Action> linkActions = new ArrayList<>();
        while (!rawLinkData.isBlank()) {
            splitLinkData = rawLinkData.split("}", 2);
            if (!rawLinkData.startsWith("{") || splitLinkData.length < 2) {
                throw new CorruptFileException("Error in trying to load actions for Link \""
                        + linkText + "\": Found text not enclosed in matching curly brackets "
                        + lineText(lineNumber));
            }
            linkActions.add(makeAction(splitLinkData[0].substring(1), lineNumber));
            rawLinkData = splitLinkData[1].trim();
        }
        return new Link(linkText, linkReference, linkActions);
    }

    private static Passage makePassage(String passageTitle, LineNumberReader fileReader)
            throws IOException, CorruptFileException {
        if (passageTitle.isBlank()) {
            throw new CorruptFileException("Found passage with no name "
                    + lineText(fileReader.getLineNumber()));
        }
        StringBuilder passageContent = new StringBuilder();
        List<Link> passageLinks = new ArrayList<>();
        String nextLine;
        //Goes through one passage
        while ((nextLine = fileReader.readLine()) != null && !nextLine.isBlank()) {
            if (nextLine.contains("[") && nextLine.contains("]")
                    && nextLine.contains("(") && nextLine.contains(")")) {
                if (passageContent.isEmpty()) {
                    throw new CorruptFileException("Passage \"" + passageTitle
                            + "\" has no content");
                } else {
                    passageLinks.add(makeLink(nextLine, fileReader.getLineNumber()));
                }
            } else {
                if (!passageContent.isEmpty()) {
                    passageContent.append("\n");
                }
                passageContent.append(nextLine);
            }
        }
        Passage returnPassage = new Passage(passageTitle, passageContent.toString());
        for (Link passageLink : passageLinks) {
            returnPassage.addLink(passageLink);
        }
        return returnPassage;
    }

    /**
     * Loads a story from a {@code .paths}-file.
     *
     * @param storyFileName The file name of the story.<br/>
     *                      <b>NB: Should not contain the file extension.</b>
     * @return The loaded story.
     * @throws CorruptFileException If the story could not be loaded.
     */
    public static Story loadStory(String storyFileName)
            throws FileNotFoundException, CorruptFileException {
        Story loadedStory = null;
        InputStream is = StoryLoader.class.getResourceAsStream(storyFileName + ".paths");
        if (is == null) {
            throw new FileNotFoundException(
                    "Could not find resource \"" + storyFileName + ".path\"");
        }
        try (LineNumberReader fileReader = new LineNumberReader(new InputStreamReader(is))) {
            String storyTitle = fileReader.readLine();
            if (storyTitle == null || storyTitle.isBlank()) {
                throw new CorruptFileException("File is empty");
            }
            String nextLine;
            while ((nextLine = fileReader.readLine()) != null) { //Goes through whole file

                if (nextLine.startsWith("::")) {
                    Passage loadedPassage = makePassage(nextLine.substring(2), fileReader);
                    if (loadedStory == null) {
                        loadedStory = new Story(storyTitle, loadedPassage);
                    } else {
                        loadedStory.addPassage(loadedPassage);
                    }
                } else if (!nextLine.isBlank()) {
                    throw new CorruptFileException("Expected to find passage, "
                            + "found non-empty line that isn't a passage "
                            + lineText(fileReader.getLineNumber()));
                }
            }
            if (loadedStory == null) {
                throw new CorruptFileException("Reached end of file without finding any passages "
                        + "for the story " + lineText(fileReader.getLineNumber()));
            }
        } catch (IOException ioe) {
            throw new CorruptFileException("Resource could not be read");
        }
        return loadedStory;
    }
}
