import static org.junit.Assert.*;

import org.junit.Test;
//As a Hokie, I will conduct myself with honor and integrity at all times.
//I will not lie, cheat, or steal, nor will I
//accept the actions of those who do.
//-- Richard Samson (skye25)

//LLM Statement:
//
//During the preparation of this assignment, I, Richard Samson used Chat GPT
//for designing and programming the Pokemon test class.
//After using this tool, I reviewed and edited the content as needed
//to ensure its accuracy and take full responsibility for the content in
//relation to grading. I understand that I am responsible for being able to
//complete this work without the use of assistance.

public class PokemonTest {

	@Test
	public void testEmptyName() {
		try {
		Pokemon pika = new Pokemon("common", "", "Electric", 100, 15, 25);
		fail("Expected IllegalArgumentException");
		}
		catch (IllegalArgumentException e)
		{
			
		}
	}
	
	@Test
	public void testNullName() {
		try {
		Pokemon pika = new Pokemon("common", null, "Electric", 100, 15, 25);
		fail("Expected IllegalArgumentException");
		}
		catch (IllegalArgumentException e)
		{
			
		}
	}
	
	@Test
	public void testEmptyType() {
		try {
			Pokemon pika = new Pokemon("common", "Pikachu", "", 100, 15, 25);
			fail("Expected IllegalArgumentException");
			}
			catch (IllegalArgumentException e)
			{
				
			}
	}
	
	@Test
	public void testNullType() {
		try {
			Pokemon pika = new Pokemon("common", "Pikachu", null, 100, 15, 25);
			fail("Expected IllegalArgumentException");
			}
			catch (IllegalArgumentException e)
			{
				
			}
	}
	
	@Test
	public void testNegativeHP() {
		try {
		Pokemon pika = new Pokemon("common", "Pikachu", "Electric", -5, 15, 25);
		fail("Expected IllegalArgumentException");
		}
			catch (IllegalArgumentException e) {
				
			}
	}
	
	
	@Test
	public void testEmptyRarity() {
		try {
		Pokemon pika = new Pokemon("", "Pikachu", "Electric", 100, 15, 25);
		fail("Expected IllegalArgumentException");
		}
			catch (IllegalArgumentException e) {
				
			}
	}
	
	@Test
	public void testNullRarity() {
		try {
		Pokemon pika = new Pokemon(null, "Pikachu", "Electric", 100, 15, 25);
		fail("Expected IllegalArgumentException");
		}
			catch (IllegalArgumentException e) {
				
			}
	}
	
	@Test
	public void testNegativeAttack() {
		try {
			Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, -15, 25);
			fail("Expected IllegalArgumentException");
			}
				catch (IllegalArgumentException e) {
					
				}
	}
	
	@Test
	public void testNegativeSpecialAttack() {
		try {
			Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, -25);
			fail("Expected IllegalArgumentException");
			}
				catch (IllegalArgumentException e) {
					
				}
	}
	
	@Test
	public void testGetCurrentHealth() {
		int target = 75;
		Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 75, 15, 25);
		assertTrue(pika.getHealth() == target);
	}
	
	@Test
	public void testGetName() {
		Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, 25);
		assertTrue(pika.getName().equals("Pikachu"));
	}
	
	@Test
	public void testGetType() {
		Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, 25);
		assertTrue(pika.getType().equals("Electric"));
	}
	
	@Test
	public void testGetAttack() {
		int target = 15;
		Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, 25);
		assertTrue(pika.getAttack() == target);
	}
	
	@Test
	public void testGetSpecialAttack() {
		int target = 25;
		Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, 25);
		assertTrue(pika.getSpecialAttack() == target);
	}
	
	@Test
	public void testGetRarity() {
		int target = 1;
		Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, 25);
		assertTrue(pika.getRarity().equals("common"));
	}
	
	@Test
	public void testUnfainted() {
		Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, 25);
		assertFalse(pika.isFainted());
	}
	
	@Test
	public void testIsFainted() {
		Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, 25);
		pika.takeDamage(100);
		assertTrue(pika.isFainted());
	}

	@Test
	public void testPokemonToString() {
		Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, 25);
		assertTrue(pika.toString().contains("Pikachu"));
	}
	
	// ---------- takeDamage() tests ----------

    @Test
    public void testTakeDamageNormally() {
    	Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, 25);
        pika.takeDamage(30);

        assertEquals(70, pika.getHealth());
        assertFalse(pika.isFainted());
    }

    @Test
    public void testTakeDamageReducesHealthToZero() {
    	Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, 25);
    	pika.takeDamage(100);

        assertEquals(0, pika.getHealth());
        assertTrue(pika.isFainted());
    }

    @Test
    public void testTakeDamageCannotReduceHealthBelowZero() {
    	Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, 25);
    	pika.takeDamage(150);

        assertEquals(0, pika.getHealth());
        assertTrue(pika.isFainted());
    }

    @Test
    public void testTakeZeroDamage() {
    	Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, 25);
    	pika.takeDamage(0);

        assertEquals(100, pika.getHealth());
        assertFalse(pika.isFainted());
    }

    @Test
    public void testNegativeDamageThrowsException() {
    	Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, 25);
        assertThrows(
            IllegalArgumentException.class,
            () -> pika.takeDamage(-10)
        );

        // Invalid damage must not change the Pokemon.
        assertEquals(100, pika.getHealth());
        assertFalse(pika.isFainted());
    }

    // ---------- itemHeal() tests ----------

    @Test
    public void testItemHealRestoresThirtyHealth() {
    	Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, 25);
        Item potion = new Item("Potion", "heal", 1);

        pika.takeDamage(50);
        pika.itemHeal(potion);

        assertEquals(80, pika.getHealth());
        assertFalse(pika.isFainted());
    }

    @Test
    public void testItemHealDoesNothingWithWrongEffect() {
    	Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, 25);
        Item buffItem = new Item("Attack Boost", "buff", 1);

        pika.itemHeal(buffItem);

        assertEquals(100, pika.getHealth());
        assertFalse(pika.isFainted());
    }

    @Test
    public void testItemHealCanReviveFaintedPokemon() {
    	Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, 25);
        Item potion = new Item("Potion", "heal", 1);

        pika.takeDamage(100);
        assertTrue(pika.isFainted());

        pika.itemHeal(potion);

        assertEquals(30, pika.getHealth());
        assertFalse(pika.isFainted());
    }

    // ---------- itemBuff() tests ----------

    @Test
    public void testItemBuffIncreasesAttackValues() {
    	Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, 25);
        Item buff = new Item("Attack Boost", "buff", 1);

        pika.itemBuff(buff);

        assertEquals(30, pika.getAttack());
        assertEquals(35, pika.getSpecialAttack());
    }

    @Test
    public void testItemBuffDoesNothingWithWrongEffect() {
    	Pokemon pika = new Pokemon("common", "Pikachu", "Electric", 100, 15, 25);
        Item potion = new Item("Potion", "heal", 1);

        pika.itemBuff(potion);

        assertEquals(15, pika.getAttack());
        assertEquals(25, pika.getSpecialAttack());
    }
}
