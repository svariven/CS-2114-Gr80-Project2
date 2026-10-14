import static org.junit.Assert.*;
import org.junit.Test;

public class ItemTest
{
    
    @Test
    public void testGetName()
    {
        Item ball = new Item("Poke Ball", "catch", 5);
        assertEquals("Poke Ball", ball.getName());
    }
    
    @Test
    public void testGetEffect()
    {
        Item potion = new Item("Potion", "heal", 3);
        assertEquals("heal", potion.getEffect());
    }
    
    @Test
    public void testGetAmount()
    {
        Item ball = new Item("Poke Ball", "catch", 5);
        assertEquals(5, ball.getAmount());
    }
    
    @Test
    public void testUse()
    {
        Item ball = new Item("Poke Ball", "catch", 5);
        ball.use();
        assertEquals(4, ball.getAmount());
    }
    
    @Test
    public void testUseWhenEmpty()
    {
        Item ball = new Item("Poke Ball", "catch", 0);
        ball.use();
        assertEquals(0, ball.getAmount());

    }
    
    @Test
    public void testNegativeAmount()
    {
        try
        {
            Item ball = new Item("Poke Ball", "catch", -5);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            
        }
    }
    
    @Test
    public void testUseLastItem()
    {
        Item ball = new Item("Poke Ball", "catch", 1);
        ball.use();
        assertEquals(0, ball.getAmount());
        ball.use();
        assertEquals(0, ball.getAmount());
        
    }
    
    @Test
    public void testEmptyName()
    {
        try
        {
            new Item("", "catch", 5);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {

        }
    }
    
    @Test
    public void testNullName()
    {
        try
        {
            new Item(null, "catch", 5);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
        
        }
    }
    
    @Test
    public void testEquals()
    {
        Item ball1 = new Item("Poke Ball", "catch", 5);
        Item ball2 = new Item("Poke Ball", "catch", 3);
        assertTrue(ball1.equals(ball2));
    }
    
    @Test
    public void testNotEquals()
    {
        Item ball = new Item("Poke Ball", "catch", 5);
        Item potion = new Item("Potion", "heal", 3);

        assertFalse(ball.equals(potion));
    }
    
    @Test
    public void testHashCode()
    {
        Item ball1 = new Item("Poke Ball", "catch", 5);
        Item ball2 = new Item("Poke Ball", "catch", 3);

        assertEquals(ball1.hashCode(), ball2.hashCode());
    }
}