package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DifficultyLoader {
    private DifficultyLoader() {
        throw new IllegalStateException("Do not instantiate this class pls :)");
    }

    /**
     * TODO: javadoc & unit testing
     * @param fileReader
     * @return
     * @throws CorruptFileException
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
        return difficulties;
    }

    /**
     * TODO: javadoc
     *
     * @param difficultiesFilePath
     * @return
     * @throws CorruptFileException
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
