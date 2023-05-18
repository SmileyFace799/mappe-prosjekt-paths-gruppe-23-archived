package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads difficulties from a {@code .difficulties}-file.
 */
public class DifficultyLoader {
    private DifficultyLoader() {
        throw new IllegalStateException("Do not instantiate this class pls :)");
    }

    /**
     * Parses difficulties from a {@link LineNumberReader},
     * and returns a {@link List} of every difficulty in the game.
     *
     * @param fileReader A {@link LineNumberReader} that contains some difficulties.
     * @return A {@link List} of every difficulty in the game
     * @throws CorruptFileException If the difficulties could not be parsed
     */
    public static List<String> parseDifficulties(LineNumberReader fileReader)
            throws CorruptFileException {
        List<String> difficulties = new ArrayList<>();
        String nextLine;
        try {
            while ((nextLine = fileReader.readLine()) != null) {
                if (!nextLine.isBlank()) {
                    difficulties.add(nextLine);
                }
            }
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN_DIFFICULTIES,
                    fileReader.getLineNumber());
        }
        if (difficulties.isEmpty()) {
            throw new CorruptFileException(CorruptFileException.Type.NO_DIFFICULTIES);
        }
        return difficulties;
    }

    /**
     * Loads difficulties from a {@code .difficulties}-file,
     * and returns a {@link List} of every difficulty in the game.
     *
     * @param difficultiesFilePath The file path of the difficulties to load
     * @return A {@link List} of every difficulty in the game
     * @throws CorruptFileException If the difficulties could not be loaded
     */
    public static List<String> loadDifficulties(Path difficultiesFilePath)
            throws CorruptFileException {
        List<String> difficulties;
        try (LineNumberReader fileReader = new LineNumberReader(
                Files.newBufferedReader(difficultiesFilePath)
        )) {
            difficulties = parseDifficulties(fileReader);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN_DIFFICULTIES);
        }
        return difficulties;
    }
}
