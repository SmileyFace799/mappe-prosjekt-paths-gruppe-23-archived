package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import no.ntnu.idata2001.g23.exceptions.unchecked.ElementNotFoundException;
import no.ntnu.idata2001.g23.model.actions.Action;
import no.ntnu.idata2001.g23.model.goals.Goal;
import no.ntnu.idata2001.g23.model.goals.GoldGoal;
import no.ntnu.idata2001.g23.model.goals.HealthGoal;
import no.ntnu.idata2001.g23.model.goals.InventoryGoal;
import no.ntnu.idata2001.g23.model.goals.ScoreGoal;
import no.ntnu.idata2001.g23.model.itemhandling.ItemFactory;
import no.ntnu.idata2001.g23.model.itemhandling.ItemProvider;
import no.ntnu.idata2001.g23.model.story.Link;
import no.ntnu.idata2001.g23.model.story.Passage;
import no.ntnu.idata2001.g23.model.story.Story;

/**
 * A class for loading stories from files.
 */
public class StoryLoader {
    private final ItemProvider itemProvider;

    public StoryLoader(ItemProvider itemProvider) {
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
        return new Link(linkText, linkReference, linkActions);
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

    private List<Goal> parseGoals(LineNumberReader fileReader)
            throws IOException, CorruptFileException {
        List<Goal> goals = new ArrayList<>();
        String nextLine;
        //Goes through one difficulty of goals
        while ((nextLine = fileReader.readLine()) != null && !nextLine.isBlank()) {
            String[] splitGoalData = nextLine.split(":", 2);
            if (splitGoalData.length < 2) {
                throw new CorruptFileException(CorruptFileException.Type.GOAL_INVALID_FORMAT,
                        fileReader.getLineNumber(), nextLine);
            }
            Goal goal;
            String goalType = splitGoalData[0].trim();
            String goalValue = splitGoalData[1].trim();
            try {
                switch (goalType) {
                    case "Gold" -> goal = new GoldGoal(Integer.parseInt(goalValue));
                    case "Health" -> goal = new HealthGoal(Integer.parseInt(goalValue));
                    case "Inventory" -> goal =
                            new InventoryGoal(ItemFactory.makeItem(goalValue));
                    case "Score" -> goal = new ScoreGoal(Integer.parseInt(goalValue));
                    default -> throw new CorruptFileException(
                            CorruptFileException.Type.GOAL_INVALID_TYPE,
                            fileReader.getLineNumber(), goalType);
                }
            } catch (NumberFormatException | ElementNotFoundException e) {
                throw new CorruptFileException(CorruptFileException.Type.GOAL_INVALID_VALUE,
                        fileReader.getLineNumber(), goalValue);
            }
            goals.add(goal);
        }
        if (goals.isEmpty()) {
            throw new CorruptFileException(CorruptFileException.Type.DIFFICULTY_NO_GOALS,
                    fileReader.getLineNumber());
        }
        return goals;
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
            Map<String, List<Goal>> goals = new LinkedHashMap<>(); //Keeps difficulty order
            String nextLine;
            while ((nextLine = fileReader.readLine()) != null) { //Goes through whole file

                if (nextLine.startsWith("::")) {
                    passages.add(parsePassage(nextLine.substring(2), fileReader));

                } else if (nextLine.startsWith("#")) {
                    if (nextLine.length() == 1) {
                        throw new CorruptFileException(CorruptFileException.Type.DIFFICULTY_NO_NAME,
                                fileReader.getLineNumber(), nextLine);
                    }
                    goals.put(nextLine.substring(1), parseGoals(fileReader));
                } else if (!nextLine.isBlank()) {
                    throw new CorruptFileException(CorruptFileException.Type.INVALID_GOAL_OR_PASSAGE,
                            fileReader.getLineNumber(), nextLine);
                }
            }
            if (passages.isEmpty()) {
                throw new CorruptFileException(CorruptFileException.Type.NO_PASSAGES,
                        fileReader.getLineNumber());
            }
            if (goals.isEmpty()) {
                throw new CorruptFileException(CorruptFileException.Type.NO_GOALS,
                        fileReader.getLineNumber());
            }
            loadedStory = new Story(storyTitle, passages.remove(0));
            passages.forEach(loadedStory::addPassage);
            goals.forEach(loadedStory::setGoals);
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN,
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
    public Story loadStory(String storyFilePath) throws CorruptFileException {
        Story loadedStory;
        try (LineNumberReader fileReader = new LineNumberReader(
                Files.newBufferedReader(
                        Path.of(storyFilePath)
                )
        )) {
            loadedStory = parseStory(fileReader);
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN);
        }
        return loadedStory;
    }
}
