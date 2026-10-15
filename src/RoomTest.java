import static org.junit.Assert.*;
//As a Hokie, I will conduct myself with honor and integrity at all times.
//I will not lie, cheat, or steal, nor will I
//accept the actions of those who do.
//-- Richard Samson (skye25)

//LLM Statement:
//
//During the preparation of this assignment, I, Richard Samson used Chat GPT
//for designing and programming the Room test class.
//After using this tool, I reviewed and edited the content as needed
//to ensure its accuracy and take full responsibility for the content in
//relation to grading. I understand that I am responsible for being able to
//complete this work without the use of assistance.

import org.junit.Before;
import org.junit.Test;

public class RoomTest {

	private Room room;
    private Item potion;
    private Pokemon pikachu;

    @Before
    public void setUp() {
        room = new Room(
            "Forest",
            "A quiet forest filled with tall trees."
        );

        potion = new Item("Potion", "heal", 1);

        pikachu = new Pokemon(
            "common", "Pikachu", "Electric", 100, 15, 25
        );
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals("Forest", room.getName());
        assertEquals(
            "A quiet forest filled with tall trees.",
            room.getDescription()
        );
    }

    @Test
    public void testAddAndRemoveItem() {
        room.addItem(potion);

        // The first removal should succeed.
        assertSame(potion, room.removeItem(potion));

        // Removing it again should fail.
        assertNull(room.removeItem(potion));
    }

    @Test
    public void testAddAndRemovePokemon() {
        room.addPokemon(pikachu);
        
        assertSame(pikachu, room.removePokemon(pikachu));
        assertNull(room.removePokemon(pikachu));
    }

    @Test
    public void testRemoveItemNotInRoom() {
        assertNull(room.removeItem(potion));
    }

    @Test
    public void testRemovePokemonNotInRoom() {
        assertNull(room.removePokemon(pikachu));
    }

    @Test
    public void testRejectNullItem() {
        assertThrows(
            IllegalArgumentException.class,
            () -> room.addItem(null)
        );
    }

    @Test
    public void testRejectNullPokemon() {
        assertThrows(
            IllegalArgumentException.class,
            () -> room.addPokemon(null)
        );
    }

    @Test
    public void testSetters() {
        room.setName("Mountain");
        room.setDescription("A rocky mountain path.");

        assertEquals("Mountain", room.getName());
        assertEquals(
            "A rocky mountain path.",
            room.getDescription()
        );
    }
    
    @Test
    public void testGetLongDescription() {
        room.addItem(potion);
        room.addPokemon(pikachu);

        String result = room.getLongDescription();
        
        System.out.println(result);

        assertTrue(result.contains("Forest"));
        assertTrue(result.contains("quiet forest"));
        assertTrue(result.contains("Potion"));
        assertTrue(result.contains("Pikachu"));
    }

    @Test
    public void testnullRoomName() {
    	try {
    	room.setName(null);
    	fail("Expected Illegal Argument Exception");
    	}
    	catch(IllegalArgumentException e) {
    	}
    }
    
    @Test
    public void testEmptyRoomName() {
    	try {
    	room.setName("");
    	fail("Expected Illegal Argument Exception");
    	}
    	catch(IllegalArgumentException e) {
    	}
    }
    @Test
    public void testnullRoomDesc() {
    	try {
    	room.setDescription(null);
    	fail("Expected Illegal Argument Exception");
    	}
    	catch(IllegalArgumentException e) {
    	}
    }
    
    @Test
    public void testEmptyRoomDesc() {
    	try {
    	room.setDescription("");
    	fail("Expected Illegal Argument Exception");
    	}
    	catch(IllegalArgumentException e) {
    	}
    }
}
