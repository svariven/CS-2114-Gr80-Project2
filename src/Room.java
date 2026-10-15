
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
		this.name = newName;
	}
	
	public void setDescription(String newDesc) {
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
