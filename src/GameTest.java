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
        assertEquals(2, game.getPlayer().getItemAmount(Game.POTION));
        assertNull(game.getWildPokemon());
        assertNull(game.getCurrentRoom());
        assertFalse(game.isInBattle());
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
    public void testExploreMovesToARoom()
    {
        game.processCommandString("explore");
        Room room = game.getCurrentRoom();
        assertNotNull(room);
        assertTrue(out().contains("You walk into the " + room.getName() + "."));
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
        assertTrue(out().contains("1. Pikachu (Type: Electric, HP: 35, Attack: 55)"));
        assertTrue(out().contains("Poke Balls left: 5"));
        assertTrue(out().contains("Potions left: 2"));
    }

    @Test
    public void testViewAfterCatchShowsNewPokemon()
    {
        game.processCommandString("explore");
        String name = game.getWildPokemon().getName();
        game.processCommandString("catch");
        game.processCommandString("view");
        assertTrue(out().contains("1. " + name + " (Type: "));
        assertTrue(out().contains("2. Pikachu (Type: "));
        assertTrue(out().contains("Poke Balls left: 4"));
    }

    // ---------- processCommandString: help, quit ----------

    @Test
    public void testHelpListsCommands()
    {
        game.processCommandString("help");
        assertTrue(out().contains("Commands: explore, catch, view, fight <number>, help, quit"));
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

    // ---------- processCommandString: fight ----------

    // Finds a wild Pokemon and fights it with Pokemon number 1
    private void startFight()
    {
        game.processCommandString("explore");
        game.processCommandString("fight 1");
    }

    @Test
    public void testFightStartsBattle()
    {
        startFight();
        assertTrue(game.isInBattle());
        assertTrue(out().contains("Pikachu, I choose you!"));
    }

    @Test
    public void testFightWithNothingToFight()
    {
        game.processCommandString("fight 1");
        assertFalse(game.isInBattle());
        assertTrue(out().contains("Fight what? Explore to find a Pokemon first."));
    }

    @Test
    public void testFightWithoutNumber()
    {
        game.processCommandString("explore");
        game.processCommandString("fight");
        assertFalse(game.isInBattle());
        assertTrue(out().contains("Fight with which Pokemon?"));
    }

    @Test
    public void testFightWithBadNumber()
    {
        game.processCommandString("explore");
        game.processCommandString("fight abc");
        game.processCommandString("fight 0");
        game.processCommandString("fight 5");
        assertFalse(game.isInBattle());
        assertTrue(out().contains("You cannot fight with \"abc\"."));
        assertTrue(out().contains("You cannot fight with \"0\"."));
        assertTrue(out().contains("You cannot fight with \"5\"."));
    }

    @Test
    public void testFaintedPokemonCannotFight()
    {
        game.getPlayer().getPokemon(0).takeDamage(100);
        startFight();
        assertFalse(game.isInBattle());
        assertTrue(out().contains("You cannot fight with Pikachu. It has fainted."));
    }

    @Test
    public void testFightWhileAlreadyFighting()
    {
        startFight();
        game.processCommandString("fight 1");
        assertTrue(game.isInBattle());
        assertTrue(out().contains("You are already fighting."));
    }

    @Test
    public void testCannotExploreDuringFight()
    {
        startFight();
        Pokemon wild = game.getWildPokemon();
        game.processCommandString("explore");
        assertSame(wild, game.getWildPokemon());
        assertTrue(out().contains("You cannot explore during a fight."));
    }

    // ---------- processCommandString: attack ----------

    @Test
    public void testAttackHurtsBothSides()
    {
        startFight();
        Pokemon wild = game.getWildPokemon();
        int wildHp = wild.getHealth();
        game.processCommandString("attack");

        assertEquals(wildHp - 55 / Battle.DAMAGE_DIVISOR, wild.getHealth());
        assertTrue(game.getPlayer().getPokemon(0).getHealth() < 35);
        assertTrue(game.isInBattle());
    }

    @Test
    public void testAttackOutsideFight()
    {
        game.processCommandString("attack");
        assertTrue(out().contains("Attack what? Type fight and a number first."));
    }

    @Test
    public void testAttackUntilFightEnds()
    {
        startFight();
        for (int i = 0; i < 50 && game.isInBattle(); i++)
        {
            game.processCommandString("attack");
        }

        assertFalse(game.isInBattle());
        if (game.getPlayer().getPokemon(0).isFainted())
        {
            assertTrue(game.isGameOver());
        }
        else
        {
            assertNull(game.getWildPokemon());
            assertTrue(out().contains("fainted! You won."));
        }
    }

    @Test
    public void testGameOverWhenAllPokemonFaint()
    {
        game.getPlayer().getPokemon(0).takeDamage(34);
        startFight();
        game.processCommandString("attack");

        assertTrue(game.getPlayer().getPokemon(0).isFainted());
        assertFalse(game.isInBattle());
        assertTrue(game.isGameOver());
        assertTrue(out().contains("All your Pokemon have fainted. Game over!"));
    }

    @Test
    public void testFaintedWithOthersLeftKeepsGoing()
    {
        Pokemon pikachu = game.getPlayer().getPokemon(0);
        game.processCommandString("explore");
        game.processCommandString("catch");
        pikachu.takeDamage(34);

        game.processCommandString("explore");
        game.processCommandString("fight 2");
        game.processCommandString("attack");

        assertTrue(pikachu.isFainted());
        assertFalse(game.isGameOver());
        assertNotNull(game.getWildPokemon());
        assertTrue(out().contains("Pick another Pokemon"));
    }

    // ---------- processCommandString: heal ----------

    @Test
    public void testHealUsesPotion()
    {
        startFight();
        game.processCommandString("heal");
        assertEquals(1, game.getPlayer().getItemAmount(Game.POTION));
        assertTrue(out().contains("Pikachu healed to 65 HP."));
    }

    @Test
    public void testHealWithNoPotionsLeft()
    {
        startFight();
        game.processCommandString("heal");
        game.processCommandString("heal");
        game.processCommandString("heal");
        assertEquals(0, game.getPlayer().getItemAmount(Game.POTION));
        assertTrue(out().contains("You're out of Potions!"));
    }

    @Test
    public void testHealOutsideFight()
    {
        game.processCommandString("heal");
        assertEquals(2, game.getPlayer().getItemAmount(Game.POTION));
        assertTrue(out().contains("You can only heal during a fight."));
    }

    // ---------- processCommandString: run, catch during a fight ----------

    @Test
    public void testRunEndsFight()
    {
        startFight();
        game.processCommandString("run");
        assertFalse(game.isInBattle());
        assertNull(game.getWildPokemon());
        assertTrue(out().contains("You ran away from"));
    }

    @Test
    public void testRunOutsideFight()
    {
        game.processCommandString("run");
        assertTrue(out().contains("Run from what? You are not in a fight."));
    }

    @Test
    public void testCatchDuringFightEndsIt()
    {
        startFight();
        game.processCommandString("catch");
        assertFalse(game.isInBattle());
        assertEquals(2, game.getPlayer().getPokemonCount());
    }

    @Test
    public void testHelpDuringFightShowsBattleCommands()
    {
        startFight();
        game.processCommandString("help");
        assertTrue(out().contains("Battle commands: attack, heal, catch, run"));
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
