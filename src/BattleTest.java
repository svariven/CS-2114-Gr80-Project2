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
