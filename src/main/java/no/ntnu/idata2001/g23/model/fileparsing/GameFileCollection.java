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
 * A collection of files that make up an entire game.
 */
public class GameFileCollection {
    private final Collection<Path> gameFiles;

    /**
     * Creates a new collection of game files based on the
     * {@code game.info}-file located inside a specified directory.
     *
     * @param gameDirectory The directory containing all the game files, including {@code game.info}
     * @throws CorruptFileException If {@code game.info} is missing or cannot be parsed
     */
    public GameFileCollection(String gameDirectory) throws CorruptFileException {
        Path gamePath = Path.of(gameDirectory);
        try (LineNumberReader fileReader = new LineNumberReader(
                Files.newBufferedReader(gamePath.resolve("game.info"))
        )) {
            this.gameFiles = parseInfo(fileReader, gamePath);
        } catch (IOException ioe) {
            throw new CorruptFileException(CorruptFileException.Type.UNKNOWN_ITEMS);
        }
    }

    /**
     * Parses the game's {@code game.info}-file.
     * This method is static so that unit tests can access it to validate the parsing logic,
     * without needing to actually load any files
     *
     * @param fileReader The {@link LineNumberReader} containing the info file to read
     * @param gamePath   The path to the directory the game is located inside
     * @return A collection of {@link Path}s with all the files to load
     * @throws CorruptFileException If the {@code game.info}-file cannot be parsed
     */
    public static Collection<Path> parseInfo(LineNumberReader fileReader, Path gamePath)
            throws CorruptFileException {
        Collection<Path> filesToRead = new HashSet<>();
        String nextLine = "";
        try {
            //Reads through entire file
            while ((nextLine = fileReader.readLine()) != null) {
                if (!nextLine.isBlank()) {
                    if (!nextLine.contains(".")) {
                        throw new CorruptFileException(CorruptFileException.Type.INFO_INVALID_PATH,
                                fileReader.getLineNumber(), nextLine);
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
                    fileReader.getLineNumber());
        } catch (InvalidPathException ipe) {
            throw new CorruptFileException(CorruptFileException.Type.INFO_INVALID_PATH,
                    fileReader.getLineNumber(), nextLine);
        }
        return filesToRead;
    }

    private Optional<Path> getOptionalPath(String fileExtension) {
        return gameFiles
                .stream()
                .filter(p -> p.toString().endsWith(fileExtension))
                .findFirst();
    }

    /**
     * Gets a path to a game file based its file extension.
     *
     * @param fileExtension The file extension of the game file to get
     * @return The game file with the specified file extension, or {@code null} if it doesn't exist
     */
    public Path getPath(String fileExtension) {
        return getOptionalPath(fileExtension).orElse(null);
    }

    /**
     * Gets a path to a game file based its file extension.
     *
     * @param fileExtension The file extension of the game file to get
     * @param exceptionType The {@link CorruptFileException.Type Type} of
     *                      {@link CorruptFileException} to throw if the game file is not found
     * @return The game file with the specified file extension
     * @throws CorruptFileException If the game file is not found
     */
    public Path getPathRequired(
            String fileExtension, CorruptFileException.Type exceptionType
    ) throws CorruptFileException {
        return getOptionalPath(fileExtension)
                .orElseThrow(() -> new CorruptFileException(exceptionType));
    }
}
