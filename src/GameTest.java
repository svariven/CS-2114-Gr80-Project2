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

    // Player 1 (Ash) explores and catches, which passes the turn to player 2
    private void ashCatches()
    {
        game.processCommandString("explore");
        game.processCommandString("catch");
    }

    // ---------- constructor ----------

    @Test
    public void testStartsWithStarterAndFiveBalls()
    {
        for (int number = 1; number <= 2; number++)
        {
            Player p = game.getPlayer(number);
            assertEquals(1, p.getPokemonCount());
            assertEquals(5, p.getItemAmount(Game.POKE_BALL));
            assertEquals(2, p.getItemAmount(Game.POTION));
            assertEquals(1, p.getItemAmount(Game.X_ATTACK));
        }
        assertNull(game.getWildPokemon());
        assertNull(game.getCurrentRoom());
        assertFalse(game.isInBattle());
        assertFalse(game.isGameOver());
        assertNull(game.getWinner());
    }

    @Test
    public void testPlayersAreDifferentAndPlayerOneGoesFirst()
    {
        assertNotSame(game.getPlayer(1), game.getPlayer(2));
        assertEquals("Ash", game.getPlayer(1).getName());
        assertEquals("Misty", game.getPlayer(2).getName());
        assertEquals(1, game.getCurrentPlayerNumber());
        assertSame(game.getPlayer(1), game.getCurrentPlayer());
    }

    @Test
    public void testGetPlayerRejectsBadNumber()
    {
        try
        {
            game.getPlayer(0);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {

        }
        try
        {
            game.getPlayer(3);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {

        }
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
    public void testExploreDoesNotEndTurn()
    {
        game.processCommandString("explore");
        assertEquals(1, game.getCurrentPlayerNumber());
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
        assertEquals(1, game.getCurrentPlayerNumber());
    }

    // ---------- processCommandString: catch ----------

    @Test
    public void testCatchAddsPokemonAndUsesBall()
    {
        game.processCommandString("explore");
        String name = game.getWildPokemon().getName();
        game.processCommandString("catch");

        assertEquals(2, game.getPlayer(1).getPokemonCount());
        assertEquals(4, game.getPlayer(1).getItemAmount(Game.POKE_BALL));
        assertNull(game.getWildPokemon());
        assertTrue(out().contains("You caught " + name + "!"));
    }

    @Test
    public void testCatchEndsTurn()
    {
        ashCatches();
        assertEquals(2, game.getCurrentPlayerNumber());
        assertTrue(out().contains("--- Misty's turn ---"));
    }

    @Test
    public void testCatchWithNothingToCatch()
    {
        game.processCommandString("catch");
        assertEquals(1, game.getPlayer(1).getPokemonCount());
        assertEquals(5, game.getPlayer(1).getItemAmount(Game.POKE_BALL));
        assertTrue(out().contains("Catch what? Explore to find a Pokemon first."));
        assertEquals(1, game.getCurrentPlayerNumber());
    }

    @Test
    public void testCatchTwiceDoesNotCatchForTheOtherPlayer()
    {
        ashCatches();
        game.processCommandString("catch");   // Misty has nothing to catch

        assertEquals(2, game.getPlayer(1).getPokemonCount());
        assertEquals(1, game.getPlayer(2).getPokemonCount());
        assertEquals(4, game.getPlayer(1).getItemAmount(Game.POKE_BALL));
        assertEquals(5, game.getPlayer(2).getItemAmount(Game.POKE_BALL));
        assertEquals(2, game.getCurrentPlayerNumber());
    }

    @Test
    public void testCatchWithNoBallsLeft()
    {
        for (int i = 0; i < Game.STARTING_BALLS; i++)
        {
            game.getPlayer(1).useItem(Game.POKE_BALL);
        }

        game.processCommandString("explore");
        game.processCommandString("catch");
        assertEquals(1, game.getPlayer(1).getPokemonCount());
        assertNotNull(game.getWildPokemon());
        assertTrue(out().contains("You're out of Poke Balls!"));
        assertEquals(1, game.getCurrentPlayerNumber());
    }

    // ---------- two players: turns, bags and collections ----------

    @Test
    public void testPlayerTwoTakesTurnThenPlayerOne()
    {
        ashCatches();
        game.processCommandString("explore");
        game.processCommandString("catch");

        assertEquals(2, game.getPlayer(2).getPokemonCount());
        assertEquals(4, game.getPlayer(2).getItemAmount(Game.POKE_BALL));
        assertEquals(1, game.getCurrentPlayerNumber());
        assertTrue(out().contains("--- Ash's turn ---"));
    }

    @Test
    public void testPlayersKeepSeparateBagsAndCollections()
    {
        ashCatches();
        assertEquals(2, game.getPlayer(1).getPokemonCount());
        assertEquals(1, game.getPlayer(2).getPokemonCount());
        assertEquals(4, game.getPlayer(1).getItemAmount(Game.POKE_BALL));
        assertEquals(5, game.getPlayer(2).getItemAmount(Game.POKE_BALL));
    }

    @Test
    public void testEachPlayerHasTheirOwnRoom()
    {
        game.processCommandString("explore");
        Room ashRoom = game.getCurrentRoom();
        game.processCommandString("catch");

        assertNull(game.getCurrentRoom());   // Misty has not explored yet
        game.processCommandString("explore");
        assertNotNull(game.getCurrentRoom());
        assertNotNull(ashRoom);
    }

    @Test
    public void testViewAndHelpDoNotEndTurn()
    {
        game.processCommandString("view");
        game.processCommandString("help");
        assertEquals(1, game.getCurrentPlayerNumber());
    }

    // ---------- processCommandString: view ----------

    @Test
    public void testViewShowsCollectionAndBalls()
    {
        game.processCommandString("view");
        assertTrue(out().contains("Ash's Pokemon:"));
        assertTrue(out().contains("1. Pikachu (Type: Electric, HP: 35, Attack: 55)"));
        assertTrue(out().contains("Poke Balls left: 5"));
        assertTrue(out().contains("Potions left: 2"));
        assertTrue(out().contains("X Attacks left: 1"));
    }

    @Test
    public void testViewShowsOpponentStanding()
    {
        game.processCommandString("view");
        assertTrue(out().contains("Misty has 1 of 1 Pokemon standing."));
    }

    @Test
    public void testViewShowsTheCurrentPlayersPokemon()
    {
        ashCatches();
        output.reset();
        game.processCommandString("view");   // Misty's turn now
        assertTrue(out().contains("Misty's Pokemon:"));
        assertTrue(out().contains("1. Charmander (Type: Fire, HP: 39, Attack: 52)"));
        assertTrue(out().contains("Ash has 2 of 2 Pokemon standing."));
    }

    @Test
    public void testViewAfterCatchShowsNewPokemon()
    {
        game.processCommandString("explore");
        String name = game.getWildPokemon().getName();
        game.processCommandString("catch");
        // back to Ash after Misty takes a turn
        game.processCommandString("explore");
        game.processCommandString("catch");
        output.reset();
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
    public void testQuitEndsGameWithNoWinner()
    {
        game.processCommandString("quit");
        assertTrue(game.isGameOver());
        assertNull(game.getWinner());
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

    // Finds a wild Pokemon and fights it with the current player's Pokemon 1
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
    public void testPlayerTwoFightsWithTheirOwnPokemon()
    {
        ashCatches();
        startFight();
        assertTrue(game.isInBattle());
        assertTrue(out().contains("Charmander, I choose you!"));
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
        game.getPlayer(1).getPokemon(0).takeDamage(100);
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

    // ---------- a fight is never interrupted by the other player ----------

    @Test
    public void testTurnStaysWithFighterThroughoutBattle()
    {
        startFight();
        game.processCommandString("buff");
        assertTrue(game.isInBattle());
        assertEquals(1, game.getCurrentPlayerNumber());

        game.processCommandString("heal");
        assertTrue(game.isInBattle());
        assertEquals(1, game.getCurrentPlayerNumber());

        game.processCommandString("view");
        assertEquals(1, game.getCurrentPlayerNumber());
    }

    @Test
    public void testEveryAttackInAFightStaysWithSamePlayer()
    {
        startFight();
        while (game.isInBattle())
        {
            assertEquals(1, game.getCurrentPlayerNumber());
            game.processCommandString("attack");
        }
    }

    @Test
    public void testRunEndsFightAndPassesTurn()
    {
        startFight();
        game.processCommandString("run");
        assertFalse(game.isInBattle());
        assertNull(game.getWildPokemon());
        assertEquals(2, game.getCurrentPlayerNumber());
        assertTrue(out().contains("You ran away from"));
    }

    @Test
    public void testCatchDuringFightEndsItAndPassesTurn()
    {
        startFight();
        game.processCommandString("catch");
        assertFalse(game.isInBattle());
        assertEquals(2, game.getPlayer(1).getPokemonCount());
        assertEquals(2, game.getCurrentPlayerNumber());
    }

    @Test
    public void testWinningFightPassesTurn()
    {
        game.processCommandString("explore");
        Pokemon wild = game.getWildPokemon();
        wild.takeDamage(wild.getHealth() - 1);
        game.processCommandString("fight 1");
        game.processCommandString("attack");

        assertTrue(wild.isFainted());
        assertFalse(game.isInBattle());
        assertNull(game.getWildPokemon());
        assertEquals(2, game.getCurrentPlayerNumber());
        assertFalse(game.isGameOver());
        assertTrue(out().contains("fainted! You won."));
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
        assertTrue(game.getPlayer(1).getPokemon(0).getHealth() < 35);
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
        if (game.getPlayer(1).getPokemon(0).isFainted())
        {
            // Ash only had one Pokemon, so Misty wins
            assertTrue(game.isGameOver());
            assertSame(game.getPlayer(2), game.getWinner());
        }
        else
        {
            assertNull(game.getWildPokemon());
            assertFalse(game.isGameOver());
            assertEquals(2, game.getCurrentPlayerNumber());
            assertTrue(out().contains("fainted! You won."));
        }
    }

    // ---------- losing: end of the game ----------

    @Test
    public void testPlayerTwoWinsWhenPlayerOneRunsOutOfPokemon()
    {
        game.getPlayer(1).getPokemon(0).takeDamage(34);
        startFight();
        game.processCommandString("attack");

        assertTrue(game.getPlayer(1).getPokemon(0).isFainted());
        assertFalse(game.isInBattle());
        assertTrue(game.isGameOver());
        assertSame(game.getPlayer(2), game.getWinner());
        assertTrue(out().contains("All of Ash's Pokemon have fainted. Misty wins!"));
    }

    @Test
    public void testPlayerOneWinsWhenPlayerTwoRunsOutOfPokemon()
    {
        ashCatches();
        game.getPlayer(2).getPokemon(0).takeDamage(38);   // 1 HP left
        startFight();
        game.processCommandString("attack");

        assertTrue(game.getPlayer(2).getPokemon(0).isFainted());
        assertTrue(game.isGameOver());
        assertSame(game.getPlayer(1), game.getWinner());
        assertTrue(out().contains("All of Misty's Pokemon have fainted. Ash wins!"));
    }

    @Test
    public void testFaintedWithOthersLeftKeepsGoingAndPassesTurn()
    {
        Pokemon pikachu = game.getPlayer(1).getPokemon(0);
        ashCatches();
        game.processCommandString("explore");   // Misty
        game.processCommandString("catch");
        pikachu.takeDamage(34);

        game.processCommandString("explore");   // Ash again
        game.processCommandString("fight 2");   // the wounded Pikachu
        game.processCommandString("attack");

        assertTrue(pikachu.isFainted());
        assertFalse(game.isGameOver());
        assertNull(game.getWinner());
        assertFalse(game.isInBattle());
        assertNull(game.getWildPokemon());
        assertEquals(2, game.getCurrentPlayerNumber());
        assertTrue(out().contains("You lost the fight."));
    }

    @Test
    public void testNoCommandsWorkAfterGameOver()
    {
        game.getPlayer(1).getPokemon(0).takeDamage(34);
        startFight();
        game.processCommandString("attack");
        assertTrue(game.isGameOver());

        game.processCommandString("explore");
        assertNull(game.getWildPokemon());
        assertTrue(out().contains("The game is over."));
    }

    // ---------- processCommandString: heal ----------

    @Test
    public void testHealUsesPotion()
    {
        startFight();
        game.processCommandString("heal");
        assertEquals(1, game.getPlayer(1).getItemAmount(Game.POTION));
        assertEquals(2, game.getPlayer(2).getItemAmount(Game.POTION));
        assertTrue(out().contains("Pikachu healed to 65 HP."));
    }

    @Test
    public void testHealWithNoPotionsLeft()
    {
        startFight();
        game.processCommandString("heal");
        game.processCommandString("heal");
        game.processCommandString("heal");
        assertEquals(0, game.getPlayer(1).getItemAmount(Game.POTION));
        assertTrue(out().contains("You're out of Potions!"));
    }

    @Test
    public void testHealOutsideFight()
    {
        game.processCommandString("heal");
        assertEquals(2, game.getPlayer(1).getItemAmount(Game.POTION));
        assertTrue(out().contains("You can only heal during a fight."));
    }

    // ---------- processCommandString: special ----------

    @Test
    public void testSpecialDealsDoubleDamage()
    {
        startFight();
        Pokemon wild = game.getWildPokemon();
        int wildHp = wild.getHealth();
        game.processCommandString("special");

        int damage = Battle.SPECIAL_MULTIPLIER * (50 / Battle.DAMAGE_DIVISOR);
        assertEquals(wildHp - damage, wild.getHealth());
        assertTrue(out().contains("Pikachu used its special attack for "
            + damage + " damage!"));
    }

    @Test
    public void testSpecialOnlyOncePerFight()
    {
        startFight();
        game.processCommandString("special");
        int wildHp = game.getWildPokemon().getHealth();
        game.processCommandString("special");

        assertEquals(wildHp, game.getWildPokemon().getHealth());
        assertTrue(out().contains(
            "You cannot use Pikachu's special attack again this fight."));
    }

    @Test
    public void testSpecialOutsideFight()
    {
        game.processCommandString("special");
        assertTrue(out().contains("Special attack what? Type fight and a number first."));
    }

    // ---------- processCommandString: buff ----------

    @Test
    public void testBuffMakesNextAttackStronger()
    {
        startFight();
        game.processCommandString("buff");
        assertTrue(out().contains("Pikachu powered up!"));

        Pokemon wild = game.getWildPokemon();
        int wildHp = wild.getHealth();
        game.processCommandString("attack");
        int boosted = (55 + Battle.BOOST_AMOUNT) / Battle.DAMAGE_DIVISOR;
        assertEquals(wildHp - boosted, wild.getHealth());
    }

    @Test
    public void testBuffTwiceIsRejected()
    {
        startFight();
        game.processCommandString("buff");
        game.processCommandString("buff");
        assertTrue(out().contains("You cannot buff Pikachu. It is already powered up."));
    }

    @Test
    public void testBuffOutsideFight()
    {
        game.processCommandString("buff");
        assertTrue(out().contains("You can only buff during a fight."));
    }

    // ---------- processCommandString: power ----------

    @Test
    public void testPowerUsesXAttack()
    {
        startFight();
        game.processCommandString("power");
        assertEquals(0, game.getPlayer(1).getItemAmount(Game.X_ATTACK));
        assertEquals(1, game.getPlayer(2).getItemAmount(Game.X_ATTACK));
        assertEquals(70, game.getPlayer(1).getPokemon(0).getAttack());
        assertTrue(out().contains("Pikachu's attack rose to 70!"));
    }

    @Test
    public void testPowerWithNoneLeft()
    {
        startFight();
        game.processCommandString("power");
        game.processCommandString("power");
        assertEquals(70, game.getPlayer(1).getPokemon(0).getAttack());
        assertTrue(out().contains("You're out of X Attacks!"));
    }

    @Test
    public void testPowerOutsideFight()
    {
        game.processCommandString("power");
        assertEquals(1, game.getPlayer(1).getItemAmount(Game.X_ATTACK));
        assertTrue(out().contains("You can only use an X Attack during a fight."));
    }

    // ---------- processCommandString: run ----------

    @Test
    public void testRunOutsideFight()
    {
        game.processCommandString("run");
        assertTrue(out().contains("Run from what? You are not in a fight."));
        assertEquals(1, game.getCurrentPlayerNumber());
    }

    @Test
    public void testHelpDuringFightShowsBattleCommands()
    {
        startFight();
        game.processCommandString("help");
        assertTrue(out().contains("Battle commands: attack, special, buff, heal, power, catch, run"));
    }

    // ---------- run ----------

    @Test
    public void testRunPlaysThroughCommands()
    {
        Game played = makeGame("explore\ncatch\nview\nquit\n");
        played.run();
        assertTrue(out().contains("Welcome to PokeQuest!"));
        assertTrue(out().contains("--- Ash's turn ---"));
        assertTrue(out().contains("Ash> "));
        assertTrue(out().contains("Misty> "));
        assertEquals(2, played.getPlayer(1).getPokemonCount());
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

    @Test
    public void testRunStopsWhenAPlayerLoses()
    {
        Game played = makeGame("explore\nfight 1\nattack\nattack\nattack\n"
            + "attack\nattack\nattack\nattack\nattack\nattack\nattack\n"
            + "attack\nattack\nattack\nattack\nattack\nattack\nattack\n");
        played.getPlayer(1).getPokemon(0).takeDamage(34);
        played.run();
        assertTrue(played.isGameOver());
        assertSame(played.getPlayer(2), played.getWinner());
        assertTrue(out().contains("Misty wins!"));
    }
}