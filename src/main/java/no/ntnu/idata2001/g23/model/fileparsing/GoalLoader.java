package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import no.ntnu.idata2001.g23.exceptions.unchecked.ElementNotFoundException;
import no.ntnu.idata2001.g23.model.goals.Goal;
import no.ntnu.idata2001.g23.model.goals.GoldGoal;
import no.ntnu.idata2001.g23.model.goals.HealthGoal;
import no.ntnu.idata2001.g23.model.goals.InventoryGoal;
import no.ntnu.idata2001.g23.model.goals.ScoreGoal;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.misc.Provider;

/**
 * A class for loading & parsing goals from files.
 */
public class GoalLoader {
    private final Provider<Item> itemProvider;
    private final String difficulty;

    public GoalLoader(Provider<Item> itemProvider, String difficulty) {
        this.itemProvider = itemProvider;
        this.difficulty = difficulty;
    }

    private Goal parseGoal(String goalData, int lineNumber, Provider<Item> itemProvider)
            throws CorruptFileException {
        String[] splitGoalData = goalData.split(":", 2);
        if (splitGoalData.length < 2) {
            throw new CorruptFileException(CorruptFileException.Type.GOAL_INVALID_FORMAT,
                    lineNumber, goalData);
        }
        Goal goal;
        String goalType = splitGoalData[0].trim();
        String goalValue = splitGoalData[1].trim();
        try {
            switch (goalType) {
                case "Gold" -> goal = new GoldGoal(Integer.parseInt(goalValue));
                case "Health" -> goal = new HealthGoal(Integer.parseInt(goalValue));
                case "Inventory" -> goal =
                        new InventoryGoal(itemProvider.provide(goalValue));
                case "Score" -> goal = new ScoreGoal(Integer.parseInt(goalValue));
                default -> throw new CorruptFileException(
                        CorruptFileException.Type.GOAL_INVALID_TYPE,
                        lineNumber, goalType);
            }
        } catch (NumberFormatException | ElementNotFoundException e) {
            throw new CorruptFileException(CorruptFileException.Type.GOAL_INVALID_VALUE,
                    lineNumber, goalValue);
        }
        return goal;
    }

    /**
     * TODO: javadoc, unit testing of this
     *
     * @param fileReader
     * @return
     * @throws CorruptFileException
     */
    public List<Goal> parseGoals(LineNumberReader fileReader)
            throws CorruptFileException {
        List<Goal> goals = new ArrayList<>();
        try {
            String nextLine = "";
            //Moves the reader forward to the list of goals for the specified difficulty.
            while (!nextLine.startsWith("#" + difficulty)) {
                nextLine = fileReader.readLine();
                if (nextLine == null) {
                    throw new CorruptFileException(CorruptFileException.Type.DIFFICULTY_NO_GOALS,
                            fileReader.getLineNumber(), difficulty);
                }
            }
            //Goes through one difficulty of goals
            while ((nextLine = fileReader.readLine()) != null && !nextLine.isBlank()) {
                goals.add(parseGoal(nextLine, fileReader.getLineNumber(), itemProvider));
            }
            if (goals.isEmpty()) {
                throw new CorruptFileException(CorruptFileException.Type.DIFFICULTY_NO_GOALS,
                        fileReader.getLineNumber(), difficulty);
            }
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN_GOALS,
                    fileReader.getLineNumber());
        }
        return goals;
    }

    /**
     * TODO: javadoc
     *
     * @param goalsFilePath
     * @return
     * @throws CorruptFileException
     */
    public List<Goal> loadGoals(Path goalsFilePath)
            throws CorruptFileException {
        List<Goal> goals;
        try (LineNumberReader fileReader = new LineNumberReader(
                Files.newBufferedReader(goalsFilePath)
        )) {
            goals = parseGoals(fileReader);
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN_GOALS);
        }
        return goals;
    }
}
