//As a Hokie, I will conduct myself with honor and integrity at all times.
//I will not lie, cheat, or steal, nor will I
//accept the actions of those who do.
//-- Richard Samson (skye25)

//LLM Statement:
//
//During the preparation of this assignment, I, Richard Samson used Chat GPT
//for designing and programming the Room class.
//After using this tool, I reviewed and edited the content as needed
//to ensure its accuracy and take full responsibility for the content in
//relation to grading. I understand that I am responsible for being able to
//complete this work without the use of assistance.

/**
 * The Room class should keep track of the linked chain of items and pokemon in any given
 * room as well as allow the proper removale of such elements. This will allow the
 * players to be able to explore a map that has both items and pokemon to collect and battle.
 */
public class Room {
	
	private String name;
	private String description;
	
	private LinkedChain<Item> items;
	private LinkedChain<Pokemon> pokemon;
	
	public Room(String name, String description) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Room name cannot be empty");
		}
		if (description == null || description.trim().isEmpty()){
			throw new IllegalArgumentException("Room desc cannot be null");
			}
		this.name = name;
		this.description = description;
		this.items = new LinkedChain<Item>();
		this.pokemon = new LinkedChain<Pokemon>();
	}
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setName(String newName)
	{
		if (newName == null || newName.trim().isEmpty()){
			throw new IllegalArgumentException("Room name cannot be empty");
		}
		this.name = newName;
	}
	
	public void setDescription(String newDesc) {
		if (newDesc == null || newDesc.trim().isEmpty()){
			throw new IllegalArgumentException("Room Description cannot be empty");
		}
		this.description = newDesc;
	}
	
	public void addItem(Item item) {
		if (item == null) {
			throw new IllegalArgumentException("Cannot add null item");
		}
		items.add(item);
	}
	
	public void addPokemon(Pokemon newPokemon) {
		if (newPokemon == null) {
			throw new IllegalArgumentException("Cannot add null pokemon");
		}
		pokemon.add(newPokemon);
	}
	
	public Item removeItem(Item item) {
		if (item == null) {
			return item;
		}
		return items.remove(item) ? item : null;
	}
	
	public Pokemon removePokemon(Pokemon target) {
		if (target == null) {
			return null;
		}
		return pokemon.remove(target) ? target : null;
	}
	
	public String getLongDescription() {
		return name + "\n" 
				+ description + "\n"
				+ "Items: " + items.toString() + "\n" 
				+ "Pokemon: " + pokemon.toString();
	}
}
