package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DifficultyLoaderTest {
    @Test
    void testLoadingOfValidDifficulties() {
        String validDifficultiesString = """
                Easy
                Normal
                Hard
                Expert
                
                Super Expert
                """;

        List<String> parsedDifficulties = assertDoesNotThrow(() ->
                DifficultyLoader.parseDifficulties(
                        new LineNumberReader(new StringReader(validDifficultiesString))));

        List<String> validDifficulties =
                List.of("Easy", "Normal", "Hard", "Expert", "Super Expert");

        assertEquals(validDifficulties, parsedDifficulties);
    }

    @Test
    void testLoadingDifficultiesFromEmptyFile() {
        CorruptFileException exception = assertThrows(CorruptFileException.class, () ->
                DifficultyLoader.parseDifficulties(new LineNumberReader(new StringReader(" "))));
        assertEquals(CorruptFileException.Type.NO_DIFFICULTIES, exception.getType());
    }
}
