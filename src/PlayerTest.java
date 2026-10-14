//As a Hokie, I will conduct myself with honor and integrity at all times.
//I will not lie, cheat, or steal, nor will I
//accept the actions of those who do.
//-- Rushil Mishra (rushilm)


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest
{
    private Player player;
    private Pokemon pikachu;
    private Pokemon charmander;

    @Before
    public void setUp()
    {
        player = new Player("Ash");
        pikachu = new Pokemon("common", "Pikachu", "Electric", 35, 55, 50);
        charmander = new Pokemon("common", "Charmander", "Fire", 39, 52, 52);
    }

    // ---------- constructor and getName ----------

    @Test
    public void testNewPlayerIsEmpty()
    {
        assertEquals("Ash", player.getName());
        assertEquals(0, player.getPokemonCount());
        assertEquals(0, player.getItemCount());
    }

    @Test
    public void testNullOrBlankNameGetsDefault()
    {
        assertEquals("Player", new Player(null).getName());
        assertEquals("Player", new Player("").getName());
        assertEquals("Player", new Player("   ").getName());
    }

    @Test
    public void testNameIsTrimmed()
    {
        assertEquals("Misty", new Player("  Misty ").getName());
    }

    // ---------- addPokemon ----------

    @Test
    public void testAddPokemon()
    {
        player.addPokemon(pikachu);
        assertEquals(1, player.getPokemonCount());
        assertTrue(player.hasPokemon(pikachu));
    }

    @Test
    public void testAddNullPokemonIsRejected()
    {
        player.addPokemon(null);
        assertEquals(0, player.getPokemonCount());
    }

    @Test
    public void testAddPokemonAllowsDuplicates()
    {
        player.addPokemon(pikachu);
        player.addPokemon(pikachu);
        assertEquals(2, player.getPokemonCount());
    }

    // ---------- removePokemon ----------

    @Test
    public void testRemoveOwnedPokemon()
    {
        player.addPokemon(pikachu);
        assertTrue(player.removePokemon(pikachu));
        assertEquals(0, player.getPokemonCount());
        assertFalse(player.hasPokemon(pikachu));
    }

    @Test
    public void testRemovePokemonNotOwned()
    {
        player.addPokemon(pikachu);
        assertFalse(player.removePokemon(charmander));
        assertEquals(1, player.getPokemonCount());
    }

    @Test
    public void testRemoveNullPokemon()
    {
        player.addPokemon(pikachu);
        assertFalse(player.removePokemon(null));
        assertEquals(1, player.getPokemonCount());
    }

    @Test
    public void testRemovePokemonFromEmptyCollection()
    {
        assertFalse(player.removePokemon(pikachu));
        assertEquals(0, player.getPokemonCount());
    }

    @Test
    public void testRemovePokemonRemovesOnlyOneDuplicate()
    {
        player.addPokemon(pikachu);
        player.addPokemon(pikachu);
        assertTrue(player.removePokemon(pikachu));
        assertEquals(1, player.getPokemonCount());
        assertTrue(player.hasPokemon(pikachu));
    }

    // ---------- hasPokemon ----------

    @Test
    public void testHasPokemonOwned()
    {
        player.addPokemon(pikachu);
        assertTrue(player.hasPokemon(pikachu));
    }

    @Test
    public void testHasPokemonNotOwned()
    {
        player.addPokemon(pikachu);
        assertFalse(player.hasPokemon(charmander));
    }

    @Test
    public void testHasNullPokemon()
    {
        player.addPokemon(pikachu);
        assertFalse(player.hasPokemon(null));
    }

    @Test
    public void testHasPokemonOnEmptyCollection()
    {
        assertFalse(player.hasPokemon(pikachu));
    }

    // ---------- getPokemon ----------

    @Test
    public void testGetPokemon()
    {
        player.addPokemon(pikachu);
        assertSame(pikachu, player.getPokemon(0));
    }

    @Test
    public void testGetPokemonReachesEveryOne()
    {
        player.addPokemon(pikachu);
        player.addPokemon(charmander);
        // the chain adds at the head, so the order is not part of the contract
        assertTrue(player.getPokemon(0) == pikachu
            || player.getPokemon(1) == pikachu);
        assertTrue(player.getPokemon(0) == charmander
            || player.getPokemon(1) == charmander);
    }

    @Test
    public void testGetPokemonBadIndex()
    {
        player.addPokemon(pikachu);
        try
        {
            player.getPokemon(1);
            fail("Expected IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException e)
        {

        }
        try
        {
            player.getPokemon(-1);
            fail("Expected IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException e)
        {

        }
    }

    @Test
    public void testGetPokemonOnEmptyCollection()
    {
        try
        {
            player.getPokemon(0);
            fail("Expected IndexOutOfBoundsException");
        }
        catch (IndexOutOfBoundsException e)
        {

        }
    }

    // ---------- getPokemonCount ----------

    @Test
    public void testPokemonCount()
    {
        player.addPokemon(pikachu);
        player.addPokemon(charmander);
        player.addPokemon(pikachu);
        assertEquals(3, player.getPokemonCount());
    }

    @Test
    public void testPokemonCountWhenEmpty()
    {
        assertEquals(0, player.getPokemonCount());
    }

    // ---------- addItem ----------

    @Test
    public void testAddItem()
    {
        player.addItem(new Item("Potion", "heal", 2));
        assertEquals(1, player.getItemCount());
        assertEquals(2, player.getItemAmount("Potion"));
    }

    @Test
    public void testAddSameItemCombinesIntoOneStack()
    {
        player.addItem(new Item("Potion", "heal", 2));
        player.addItem(new Item("Potion", "heal", 3));
        assertEquals(1, player.getItemCount());
        assertEquals(5, player.getItemAmount("Potion"));
    }

    @Test
    public void testAddDifferentItemsMakesSeparateStacks()
    {
        player.addItem(new Item("Potion", "heal", 2));
        player.addItem(new Item("Poke Ball", "catch", 5));
        assertEquals(2, player.getItemCount());
        assertEquals(2, player.getItemAmount("Potion"));
        assertEquals(5, player.getItemAmount("Poke Ball"));
    }

    @Test
    public void testAddNullItemIsRejected()
    {
        player.addItem(null);
        assertEquals(0, player.getItemCount());
    }

    @Test
    public void testAddItemWithZeroAmountIsRejected()
    {
        player.addItem(new Item("Potion", "heal", 0));
        assertEquals(0, player.getItemCount());
        assertEquals(0, player.getItemAmount("Potion"));
    }

    // ---------- useItem ----------

    @Test
    public void testUseItemLowersAmount()
    {
        player.addItem(new Item("Potion", "heal", 2));
        assertTrue(player.useItem("Potion"));
        assertEquals(1, player.getItemAmount("Potion"));
        assertEquals(1, player.getItemCount());
    }

    @Test
    public void testUseLastItemRemovesStack()
    {
        player.addItem(new Item("Potion", "heal", 1));
        assertTrue(player.useItem("Potion"));
        assertEquals(0, player.getItemAmount("Potion"));
        assertEquals(0, player.getItemCount());
        assertFalse(player.useItem("Potion"));
    }

    @Test
    public void testUseMissingItem()
    {
        player.addItem(new Item("Potion", "heal", 2));
        assertFalse(player.useItem("Elixir"));
        assertEquals(2, player.getItemAmount("Potion"));
    }

    @Test
    public void testUseNullItemName()
    {
        player.addItem(new Item("Potion", "heal", 2));
        assertFalse(player.useItem(null));
        assertEquals(2, player.getItemAmount("Potion"));
    }

    @Test
    public void testUseItemFromEmptyBag()
    {
        assertFalse(player.useItem("Potion"));
        assertEquals(0, player.getItemCount());
    }

    @Test
    public void testUseItemOnlyAffectsThatStack()
    {
        player.addItem(new Item("Potion", "heal", 2));
        player.addItem(new Item("Poke Ball", "catch", 5));
        assertTrue(player.useItem("Poke Ball"));
        assertEquals(4, player.getItemAmount("Poke Ball"));
        assertEquals(2, player.getItemAmount("Potion"));
    }

    // ---------- getItemAmount ----------

    @Test
    public void testGetItemAmount()
    {
        player.addItem(new Item("Poke Ball", "catch", 5));
        assertEquals(5, player.getItemAmount("Poke Ball"));
    }

    @Test
    public void testGetItemAmountMissingOrNull()
    {
        player.addItem(new Item("Poke Ball", "catch", 5));
        assertEquals(0, player.getItemAmount("Potion"));
        assertEquals(0, player.getItemAmount(null));
    }

    @Test
    public void testGetItemAmountIsCaseSensitive()
    {
        player.addItem(new Item("Poke Ball", "catch", 5));
        assertEquals(0, player.getItemAmount("poke ball"));
    }

    // ---------- getItemCount ----------

    @Test
    public void testItemCountCountsStacks()
    {
        player.addItem(new Item("Potion", "heal", 2));
        player.addItem(new Item("Poke Ball", "catch", 5));
        player.addItem(new Item("Potion", "heal", 1));
        assertEquals(2, player.getItemCount());
    }

    @Test
    public void testItemCountWhenEmpty()
    {
        assertEquals(0, player.getItemCount());
    }
}