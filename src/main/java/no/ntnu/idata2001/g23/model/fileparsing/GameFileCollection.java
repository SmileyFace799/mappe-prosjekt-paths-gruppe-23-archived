package no.ntnu.idata2001.g23.model.fileparsing;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

/**
 * Loads a game from a directory containing a full game.
 */
public class GameFileCollection {
    private final Path gamePath;
    private final Collection<Path> gameFiles;

    public GameFileCollection(String gameDirectory) throws CorruptFileException {
        this.gamePath = Path.of(gameDirectory);
        this.gameFiles = parseInfo();
    }

    private Optional<Path> getOptionalPath(String fileExtension) {
        return gameFiles
                .stream()
                .filter(p -> p.toString().endsWith(fileExtension))
                .findFirst();
    }

    public Path getPath(String fileExtension) {
        return getOptionalPath(fileExtension).orElse(null);
    }

    public Path getPathRequired(
            String fileExtension, CorruptFileException.Type exceptionType
    ) throws CorruptFileException {
        return getOptionalPath(fileExtension)
                .orElseThrow(() -> new CorruptFileException(exceptionType));
    }

    /**
     * Parses the game's {@code game.info}-file.
     *
     * @return A collection of {@link Path}s with all the files to load
     * @throws CorruptFileException If the {@code game.info}-file cannot be parsed
     */
    public Collection<Path> parseInfo()
            throws CorruptFileException {
        Collection<Path> filesToRead = new HashSet<>();
        String nextLine = "";
        int lineNumber = 0;
        try (LineNumberReader fileReader = new LineNumberReader(
                Files.newBufferedReader(gamePath.resolve("game.info"))
        )) {
            //Reads through entire file
            while ((nextLine = fileReader.readLine()) != null) {
                lineNumber = fileReader.getLineNumber();
                if (!nextLine.isBlank()) {
                    if (!nextLine.contains(".")) {
                        throw new CorruptFileException(CorruptFileException.Type.INFO_INVALID_PATH,
                                lineNumber, nextLine);
                    }
                    //Ignores multiple files with the same file extension
                    String fileExtension = nextLine.substring(nextLine.lastIndexOf("."));
                    if (filesToRead.stream().noneMatch(
                            path -> path.toString().endsWith(fileExtension))) {
                        filesToRead.add(gamePath.resolve(nextLine));
                    }
                }
            }
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN_INFO,
                    lineNumber);
        } catch (InvalidPathException ipe) {
            throw new CorruptFileException(CorruptFileException.Type.INFO_INVALID_PATH,
                    lineNumber, nextLine);
        }
        return filesToRead;
    }
}
