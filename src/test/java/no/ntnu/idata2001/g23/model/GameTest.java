package no.ntnu.idata2001.g23.model;

import java.util.List;
import no.ntnu.idata2001.g23.model.entities.Player;
import no.ntnu.idata2001.g23.model.goals.Goal;
import no.ntnu.idata2001.g23.model.goals.HealthGoal;
import no.ntnu.idata2001.g23.model.goals.ScoreGoal;
import no.ntnu.idata2001.g23.model.story.Link;
import no.ntnu.idata2001.g23.model.story.Passage;
import no.ntnu.idata2001.g23.model.story.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Player player;
    private Story story;
    private Passage openingPassage;
    private Passage anotherPassage;
    private List<Goal> goals;
    private Game validGame;

    @BeforeEach
    void before() {
        player = new Player.PlayerBuilder("Test player", 20).build();
        openingPassage = new Passage("Opening passage", "Test content");
        anotherPassage = new Passage("Another passage", "More content");
        story = new Story("Test story", openingPassage);
        story.addPassage(anotherPassage);
        goals = List.of(new HealthGoal(5), new ScoreGoal(10));

        validGame = new Game(player, story, goals);
    }

    @Test
    void testCreationOfGameWithNoPlayer() {
        assertThrows(IllegalArgumentException.class, () -> new Game(null, story, goals));
    }

    @Test
    void testCreationOfGameWithNoStory() {
        assertThrows(IllegalArgumentException.class, () -> new Game(player, null, goals));
    }

    @Test
    void testCreationOfGameWithNoGoals() {
        assertThrows(IllegalArgumentException.class, () -> new Game(player, story, null));
        List<Goal> emptyGoalList = List.of();
        assertThrows(IllegalArgumentException.class, () -> new Game(player, story, emptyGoalList));
    }

    @Test
    void testCreationOfGameWithValidParameters() {
        assertEquals(player, validGame.getPlayer());
        assertEquals(story, validGame.getStory());
        assertEquals(goals, validGame.getGoals());
    }

    @Test
    void testGameBegin() {
        assertEquals(openingPassage, validGame.begin());
    }

    @Test
    void testGameGo() {
        assertThrows(IllegalArgumentException.class, () -> validGame.go(null));
        assertEquals(anotherPassage, validGame.go(
                new Link("Test link", "Another passage")));
    }
}
