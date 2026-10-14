import static org.junit.Assert.*;

import org.junit.Test;

public class PokemonTest {

	@Test
	public void testEmptyName() {
		try {
		Pokemon pika = new Pokemon(1, "", "Electric", 100, 100, 15, 25);
		fail("Expected IllegalArgumentException");
		}
		catch (IllegalArgumentException e)
		{
			
		}
	}
	
	public void testNullName() {
		try {
		Pokemon pika = new Pokemon(1, null, "Electric", 100, 100, 15, 25);
		fail("Expected IllegalArgumentException");
		}
		catch (IllegalArgumentException e)
		{
			
		}
	}
	
	@Test
	public void testEmptyType() {
		try {
			Pokemon pika = new Pokemon(1, "Pikachu", "", 100, 100, 15, 25);
			fail("Expected IllegalArgumentException");
			}
			catch (IllegalArgumentException e)
			{
				
			}
	}
	
	@Test
	public void testNullType() {
		try {
			Pokemon pika = new Pokemon(1, "Pikachu", null, 100, 100, 15, 25);
			fail("Expected IllegalArgumentException");
			}
			catch (IllegalArgumentException e)
			{
				
			}
	}
	
	@Test
	public void testNegativeHP() {
		try {
		Pokemon pika = new Pokemon(1, "Pikachu", "Electric", -5, 100, 15, 25);
		fail("Expected IllegalArgumentException");
		}
			catch (IllegalArgumentException e) {
				
			}
	}
	
	
	@Test
	public void testNegativeLevel() {
		try {
		Pokemon pika = new Pokemon(-1, "Pikachu", "Electric", 100, 100, 15, 25);
		fail("Expected IllegalArgumentException");
		}
			catch (IllegalArgumentException e) {
				
			}
	}
	
	@Test
	public void testNegativeAttack() {
		try {
			Pokemon pika = new Pokemon(1, "Pikachu", "Electric", 100, 100, -15, 25);
			fail("Expected IllegalArgumentException");
			}
				catch (IllegalArgumentException e) {
					
				}
	}
	
	@Test
	public void testNegativeSpecialAttack() {
		try {
			Pokemon pika = new Pokemon(1, "Pikachu", "Electric", 100, 100, 15, -25);
			fail("Expected IllegalArgumentException");
			}
				catch (IllegalArgumentException e) {
					
				}
	}
	
	@Test
	public void testGetMaxHealth() {
		int target = 100;
		Pokemon pika = new Pokemon(1, "Pikachu", "Electric", 100, 100, 15, 25);
		assertTrue(pika.getMaxHealth() == target);
	}
	
	@Test
	public void testGetCurrentHealth() {
		int target = 75;
		Pokemon pika = new Pokemon(1, "Pikachu", "Electric", 100, 75, 15, 25);
		assertTrue(pika.getCurrentHealth() == target);
	}
	
	@Test
	public void testGetName() {
		Pokemon pika = new Pokemon(1, "Pikachu", "Electric", 100, 100, 15, 25);
		assertTrue(pika.getName().equals("Pikachu"));
	}
	
	@Test
	public void testGetType() {
		Pokemon pika = new Pokemon(1, "Pikachu", "Electric", 100, 100, 15, 25);
		assertTrue(pika.getType().equals("Electric"));
	}
	
	@Test
	public void testGetAttack() {
		int target = 15;
		Pokemon pika = new Pokemon(1, "Pikachu", "Electric", 100, 100, 15, 25);
		assertTrue(pika.getAttack() == target);
	}
	
	@Test
	public void testGetSpecialAttack() {
		int target = 25;
		Pokemon pika = new Pokemon(1, "Pikachu", "Electric", 100, 100, 15, 25);
		assertTrue(pika.getSpecialAttack() == target);
	}
	
	@Test
	public void testGeLevel() {
		int target = 1;
		Pokemon pika = new Pokemon(1, "Pikachu", "Electric", 100, 100, 15, 25);
		assertTrue(pika.getLevel() == target);
	}
	
	@Test
	public void testUnfainted() {
		Pokemon pika = new Pokemon(1, "Pikachu", "Electric", 100, 100, 15, 25);
		assertFalse(pika.isFainted());
	}
	
	@Test
	public void testIsFainted() {
		Pokemon pika = new Pokemon(1, "Pikachu", "Electric", 100, 100, 15, 25);
		pika.takeDamage(100);
		assertTrue(pika.isFainted());

	}

}
