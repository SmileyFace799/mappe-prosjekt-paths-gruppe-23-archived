package no.ntnu.idata2001.g23.story;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class StoryLoader {
    private StoryLoader() {
        throw new IllegalStateException("Utility class");
    }

    public Story loadStory(String storyName) throws FileNotFoundException {
        try (Scanner fileScanner = new Scanner(new File(storyName + ".paths"))) {
            //TODO: Finish this
            return null;
        }
    }
}
