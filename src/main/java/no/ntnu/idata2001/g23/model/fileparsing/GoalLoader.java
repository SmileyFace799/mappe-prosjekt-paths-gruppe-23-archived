package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import no.ntnu.idata2001.g23.model.goals.Goal;
import no.ntnu.idata2001.g23.model.goals.GoldGoal;
import no.ntnu.idata2001.g23.model.goals.HealthGoal;
import no.ntnu.idata2001.g23.model.goals.InventoryGoal;
import no.ntnu.idata2001.g23.model.goals.ScoreGoal;
import no.ntnu.idata2001.g23.model.items.Item;
import no.ntnu.idata2001.g23.model.misc.Provider;

/**
 * Loads goals from a {@code .goals}-file.
 */
public class GoalLoader {
    private final Provider<Item> itemProvider;
    private final String difficulty;

    /**
     * Makes a new goal loader.
     *
     * @param itemProvider The {@link Provider} to use to provide items
     * @param difficulty The difficulty to load goals for
     */
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
        } catch (IllegalArgumentException iae) {
            throw new CorruptFileException(CorruptFileException.Type.GOAL_INVALID_VALUE,
                    lineNumber, goalValue);
        }
        return goal;
    }

    /**
     * Parses goals from a {@link LineNumberReader},
     * and returns a {@link List} of every goal associated with the goal loader's difficulty.
     *
     * @param fileReader A {@link LineNumberReader} that contains some goals.
     * @return A {@link List} of every goal associated with the goal loader's difficulty
     * @throws CorruptFileException If the goals could not be parsed
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
                    throw new CorruptFileException(CorruptFileException.Type.NO_GOALS,
                            fileReader.getLineNumber(), difficulty);
                }
            }
            //Goes through one difficulty of goals
            while ((nextLine = fileReader.readLine()) != null && !nextLine.isBlank()) {
                goals.add(parseGoal(nextLine, fileReader.getLineNumber(), itemProvider));
            }
            if (goals.isEmpty()) {
                throw new CorruptFileException(CorruptFileException.Type.NO_GOALS,
                        fileReader.getLineNumber(), difficulty);
            }
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN_GOALS,
                    fileReader.getLineNumber());
        }
        return goals;
    }

    /**
     * Loads goals from a {@code .goals}-file,
     * and returns a {@link List} of every goal associated with the goal loader's difficulty.
     *
     * @param goalsFilePath The file path of the goals to load
     * @return A {@link List} of every goal associated with the goal loader's difficulty
     * @throws CorruptFileException If the goals could not be loaded
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
