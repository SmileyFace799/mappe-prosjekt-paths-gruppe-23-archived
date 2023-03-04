package no.ntnu.idata2001.g23.story;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StoryLoaderTest {

    @Test
    void TestLoadingOfValidStory() {
        assertDoesNotThrow(() -> StoryLoader.loadStory("testStory"));
    }
}
