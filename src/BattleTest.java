//As a Hokie, I will conduct myself with honor and integrity at all times.
//I will not lie, cheat, or steal, nor will I
//accept the actions of those who do.
//-- Ishaan Srichitturi (Ishaan07)

//LLM Statement:
//
//During the preparation of this assignment, I, Ishaan Srichitturi used claude
//for designing and programming the Battle tests.
//After using this tool, I reviewed and edited the content as needed
//to ensure its accuracy and take full responsibility for the content in
//relation to grading. I understand that I am responsible for being able to
//complete this work without the use of assistance.

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BattleTest
{
    private Pokemon pikachu;
    private Pokemon geodude;
    private Battle battle;

    @Before
    public void setUp()
    {
        pikachu = new Pokemon("common", "Pikachu", "Electric", 35, 55, 50);
        geodude = new Pokemon("common", "Geodude", "Rock", 40, 80, 80);
        battle = new Battle(pikachu, geodude);
    }

    // ---------- constructor ----------

    @Test
    public void testConstructorStoresPokemon()
    {
        assertSame(pikachu, battle.getFirst());
        assertSame(geodude, battle.getSecond());
        assertFalse(battle.isOver());
    }

    @Test
    public void testConstructorRejectsNull()
    {
        try
        {
            new Battle(pikachu, null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {

        }
    }

    // ---------- attack ----------

    @Test
    public void testAttackLowersDefenderHealth()
    {
        assertEquals(13, battle.attack(pikachu, geodude));
        assertEquals(27, geodude.getHealth());
        assertEquals(35, pikachu.getHealth());
    }

    @Test
    public void testFaintedAttackerIsRejected()
    {
        pikachu.takeDamage(100);
        assertEquals(0, battle.attack(pikachu, geodude));
        assertEquals(40, geodude.getHealth());
    }

    @Test
    public void testAttackWithPokemonNotInBattle()
    {
        Pokemon outsider = new Pokemon("common", "Pidgey", "Normal", 40, 45, 45);
        try
        {
            battle.attack(outsider, geodude);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {

        }
        assertEquals(40, geodude.getHealth());
    }

    @Test
    public void testAttackAlwaysDealsAtLeastOne()
    {
        Pokemon weak = new Pokemon("common", "Magikarp", "Water", 20, 0, 0);
        Battle weakBattle = new Battle(weak, geodude);
        assertEquals(1, weakBattle.attack(weak, geodude));
    }

    @Test
    public void testConstructorRejectsSamePokemonTwice()
    {
        try
        {
            new Battle(pikachu, pikachu);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {

        }
    }

    // ---------- specialAttack ----------

    @Test
    public void testSpecialAttackDoesDoubleDamage()
    {
        assertEquals(24, battle.specialAttack(pikachu, geodude));
        assertEquals(16, geodude.getHealth());
        assertTrue(battle.hasUsedSpecial(pikachu));
        assertFalse(battle.hasUsedSpecial(geodude));
    }

    @Test
    public void testSpecialAttackOnlyOnce()
    {
        battle.specialAttack(pikachu, geodude);
        assertEquals(0, battle.specialAttack(pikachu, geodude));
        assertEquals(16, geodude.getHealth());
    }

    @Test
    public void testFaintedPokemonCannotSpecialAttack()
    {
        pikachu.takeDamage(100);
        assertEquals(0, battle.specialAttack(pikachu, geodude));
        assertFalse(battle.hasUsedSpecial(pikachu));
    }

    // ---------- boost ----------

    @Test
    public void testBoostRaisesDamage()
    {
        assertTrue(battle.boost(pikachu));
        assertEquals(Battle.BOOST_TURNS, battle.getBoostTurns(pikachu));
        assertEquals(18, battle.attack(pikachu, geodude));
        assertEquals(Battle.BOOST_TURNS - 1, battle.getBoostTurns(pikachu));
    }

    @Test
    public void testBoostWearsOff()
    {
        Pokemon tank = new Pokemon("common", "Snorlax", "Normal", 500, 10, 10);
        Battle tankBattle = new Battle(pikachu, tank);
        tankBattle.boost(pikachu);
        for (int i = 0; i < Battle.BOOST_TURNS; i++)
        {
            assertEquals(18, tankBattle.attack(pikachu, tank));
        }
        assertEquals(0, tankBattle.getBoostTurns(pikachu));
        assertEquals(13, tankBattle.attack(pikachu, tank));
    }

    @Test
    public void testBoostCannotStack()
    {
        battle.boost(pikachu);
        assertFalse(battle.boost(pikachu));
        assertEquals(Battle.BOOST_TURNS, battle.getBoostTurns(pikachu));
    }

    @Test
    public void testFaintedPokemonCannotBoost()
    {
        pikachu.takeDamage(100);
        assertFalse(battle.boost(pikachu));
        assertEquals(0, battle.getBoostTurns(pikachu));
    }

    @Test
    public void testBoostPokemonNotInBattle()
    {
        Pokemon outsider = new Pokemon("common", "Pidgey", "Normal", 40, 45, 45);
        try
        {
            battle.boost(outsider);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {

        }
    }

    @Test
    public void testLookupsRejectPokemonNotInBattle()
    {
        Pokemon outsider = new Pokemon("common", "Pidgey", "Normal", 40, 45, 45);
        try
        {
            battle.getBoostTurns(outsider);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {

        }
        try
        {
            battle.hasUsedSpecial(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {

        }
    }

    // ---------- isOver ----------

    @Test
    public void testIsOverWhenOneFaints()
    {
        geodude.takeDamage(40);
        assertTrue(battle.isOver());
    }

    // ---------- runTurn ----------

    @Test
    public void testRunTurnBothAttack()
    {
        battle.runTurn();
        assertEquals(27, geodude.getHealth());
        assertEquals(15, pikachu.getHealth());
    }

    @Test
    public void testRunTurnStopsWhenSecondFaints()
    {
        geodude.takeDamage(30);
        battle.runTurn();
        assertTrue(geodude.isFainted());
        assertEquals(35, pikachu.getHealth());
    }

    @Test
    public void testRunTurnDoesNothingWhenOver()
    {
        pikachu.takeDamage(100);
        battle.runTurn();
        assertEquals(40, geodude.getHealth());
    }
}
