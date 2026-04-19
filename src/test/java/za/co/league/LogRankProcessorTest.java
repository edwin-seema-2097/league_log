package za.co.league;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LogRankProcessorTest {
    private LogRankProcessor logRankProcessor;
    private final String RAW_INPUT =
                    "Lions 3, Snakes 3\n" +
                    "Tarantulas 1, FC Awesome 0\n" +
                    "Lions 1, FC Awesome 1\n" +
                    "Tarantulas 3, Snakes 1\n" +
                    "Lions 4, Grouches 0";

    @BeforeAll
    public void init() {
        logRankProcessor = new LogRankProcessor();
    }

    @Test
    @Order(1)
    public void testCacheInputLoadsAllMatches() {
        Inputs inputs = new Inputs("stdin");
        logRankProcessor.cacheInput(inputs, RAW_INPUT);
        assertEquals(5, inputs.getGames().size());
    }

    @Test
    @Order(2)
    public void testProcessTeamPointCalculatesCorrectPoints() {

        logRankProcessor.processTeamPoint();

        assertEquals(5, logRankProcessor.getLog().get("Lions").getLogPoints());
        assertEquals(1, logRankProcessor.getLog().get("Snakes").getLogPoints());
        assertEquals(6, logRankProcessor.getLog().get("Tarantulas").getLogPoints());
        assertEquals(1, logRankProcessor.getLog().get("FC Awesome").getLogPoints());
        assertEquals(0, logRankProcessor.getLog().get("Grouches").getLogPoints());
    }

    @Test
    @Order(3)
    public void testRankTeamAssignsCorrectPositions() {
        logRankProcessor.rankTeam();

        assertEquals(1, logRankProcessor.getLog().get("Tarantulas").getLogPosition());
        assertEquals(2, logRankProcessor.getLog().get("Lions").getLogPosition());
        assertEquals(3, logRankProcessor.getLog().get("FC Awesome").getLogPosition());
        assertEquals(3, logRankProcessor.getLog().get("Snakes").getLogPosition());
        assertEquals(5, logRankProcessor.getLog().get("Grouches").getLogPosition());
    }

    @Test
    @Order(4)
    public void testPublishLogRankingsPrintsCorrectly() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        logRankProcessor.publishLogRankings();

        String output = out.toString();

        assertTrue(output.contains("1. Tarantulas, 6 pts"));
        assertTrue(output.contains("2. Lions, 5 pts"));
        assertTrue(output.contains("3. FC Awesome, 1 pt"));
        assertTrue(output.contains("3. Snakes, 1 pt"));
        assertTrue(output.contains("5. Grouches, 0 pts"));
    }

}
