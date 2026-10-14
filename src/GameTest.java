//As a Hokie, I will conduct myself with honor and integrity at all times.
//I will not lie, cheat, or steal, nor will I
//accept the actions of those who do.
//-- Ishaan Srichitturi (Ishaan07)

//LLM Statement:
//
//During the preparation of this assignment, I, Ishaan Srichitturi used claude
//for designing and programming the Game tests.
//After using this tool, I reviewed and edited the content as needed
//to ensure its accuracy and take full responsibility for the content in
//relation to grading. I understand that I am responsible for being able to
//complete this work without the use of assistance.

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GameTest
{
    private ByteArrayOutputStream output;
    private PrintStream originalOut;
    private Game game;

    @Before
    public void setUp()
    {
        output = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(output));
        game = makeGame("");
    }

    @After
    public void tearDown()
    {
        System.setOut(originalOut);
    }

    // Makes a game that reads the given text as the user's input
    private Game makeGame(String typed)
    {
        return new Game(new Scanner(typed), new Random(1));
    }

    private String out()
    {
        return output.toString();
    }

    // ---------- constructor ----------

    @Test
    public void testStartsWithStarterAndFiveBalls()
    {
        assertEquals(1, game.getPlayer().getPokemonCount());
        assertEquals(5, game.getPlayer().getItemAmount(Game.POKE_BALL));
        assertNull(game.getWildPokemon());
        assertFalse(game.isGameOver());
    }

    @Test
    public void testNullInputIsRejected()
    {
        try
        {
            new Game(null, new Random());
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {

        }
    }

    // ---------- processCommandString: explore ----------

    @Test
    public void testExploreShowsWildPokemon()
    {
        game.processCommandString("explore");
        assertNotNull(game.getWildPokemon());
        assertTrue(out().contains("A wild " + game.getWildPokemon().getName()
            + " appeared!"));
    }

    @Test
    public void testCommandIgnoresCaseAndSpaces()
    {
        game.processCommandString("  EXPLORE  ");
        assertNotNull(game.getWildPokemon());
    }

    @Test
    public void testMisspelledCommandIsRejected()
    {
        game.processCommandString("explor");
        assertNull(game.getWildPokemon());
        assertTrue(out().contains("I don't know \"explor\". Type help for commands."));
    }

    // ---------- processCommandString: catch ----------

    @Test
    public void testCatchAddsPokemonAndUsesBall()
    {
        game.processCommandString("explore");
        String name = game.getWildPokemon().getName();
        game.processCommandString("catch");

        assertEquals(2, game.getPlayer().getPokemonCount());
        assertEquals(4, game.getPlayer().getItemAmount(Game.POKE_BALL));
        assertNull(game.getWildPokemon());
        assertTrue(out().contains("You caught " + name + "!"));
    }

    @Test
    public void testCatchWithNothingToCatch()
    {
        game.processCommandString("catch");
        assertEquals(1, game.getPlayer().getPokemonCount());
        assertEquals(5, game.getPlayer().getItemAmount(Game.POKE_BALL));
        assertTrue(out().contains("Catch what? Explore to find a Pokemon first."));
    }

    @Test
    public void testCatchTwiceOnlyCatchesOnce()
    {
        game.processCommandString("explore");
        game.processCommandString("catch");
        game.processCommandString("catch");
        assertEquals(2, game.getPlayer().getPokemonCount());
        assertEquals(4, game.getPlayer().getItemAmount(Game.POKE_BALL));
    }

    @Test
    public void testCatchWithNoBallsLeft()
    {
        for (int i = 0; i < Game.STARTING_BALLS; i++)
        {
            game.processCommandString("explore");
            game.processCommandString("catch");
        }
        assertEquals(6, game.getPlayer().getPokemonCount());
        assertEquals(0, game.getPlayer().getItemAmount(Game.POKE_BALL));

        game.processCommandString("explore");
        game.processCommandString("catch");
        assertEquals(6, game.getPlayer().getPokemonCount());
        assertNotNull(game.getWildPokemon());
        assertTrue(out().contains("You're out of Poke Balls!"));
    }

    // ---------- processCommandString: view ----------

    @Test
    public void testViewShowsCollectionAndBalls()
    {
        game.processCommandString("view");
        assertTrue(out().contains("Pikachu (Type: Electric, HP: 35, Attack: 55)"));
        assertTrue(out().contains("Poke Balls left: 5"));
    }

    @Test
    public void testViewAfterCatchShowsNewPokemon()
    {
        game.processCommandString("explore");
        String name = game.getWildPokemon().getName();
        game.processCommandString("catch");
        game.processCommandString("view");
        assertTrue(out().contains("  " + name + " (Type: "));
        assertTrue(out().contains("Poke Balls left: 4"));
    }

    // ---------- processCommandString: fight, help, quit ----------

    @Test
    public void testFightIsNotReadyYet()
    {
        game.processCommandString("fight");
        assertTrue(out().contains("Battling isn't ready yet."));
        assertFalse(game.isGameOver());
    }

    @Test
    public void testHelpListsCommands()
    {
        game.processCommandString("help");
        assertTrue(out().contains("Commands: explore, catch, view, fight, help, quit"));
    }

    @Test
    public void testQuitEndsGame()
    {
        game.processCommandString("quit");
        assertTrue(game.isGameOver());
    }

    @Test
    public void testEmptyAndNullCommands()
    {
        game.processCommandString("");
        game.processCommandString("   ");
        game.processCommandString(null);
        assertFalse(game.isGameOver());
        assertTrue(out().contains("What do you want to do? Type help for commands."));
    }

    // ---------- run ----------

    @Test
    public void testRunPlaysThroughCommands()
    {
        Game played = makeGame("explore\ncatch\nview\nquit\n");
        played.run();
        assertTrue(out().contains("Welcome to PokeQuest!"));
        assertEquals(2, played.getPlayer().getPokemonCount());
        assertTrue(played.isGameOver());
        assertTrue(out().contains("Thanks for playing!"));
    }

    @Test
    public void testRunKeepsGoingAfterBadInput()
    {
        Game played = makeGame("asdf\n\ncatch\nview\n");
        played.run();
        assertTrue(out().contains("I don't know \"asdf\"."));
        assertTrue(out().contains("Poke Balls left: 5"));
        assertTrue(out().contains("Thanks for playing!"));
    }

    @Test
    public void testRunStopsAtQuit()
    {
        Game played = makeGame("quit\nexplore\n");
        played.run();
        assertNull(played.getWildPokemon());
    }
}
