import static org.junit.Assert.*;

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

}
