/**
 * The Pokemon class represents the entitites in action during the battle phase
 * and the players' collection. They signal progression.
 * 
 */
public class Pokemon {

	private String rarity;
	private String name;
	private String type;
	private boolean isFainted;
	private int health;
	private int attack;
	private int specialAttack;

	public Pokemon(String rarity, String name, String type, int health, int attack, int specialAttack) {
		if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Pokemon name cannot be empty");
        }
		if (type == null || type.trim().isEmpty())
        {
            throw new IllegalArgumentException("Pokemon type cannot be empty");
        }
		if (rarity == null || rarity.trim().isEmpty()) {
			throw new IllegalArgumentException("Pokemon rarity cannot be empty");
		}
		if (health < 0)
		{
			throw new IllegalArgumentException("Pokemon initial health cannot be <0");
		}
		if (attack < 0 || specialAttack < 0) {
			throw new IllegalArgumentException("All attack values must be non-negative");
		}
		
		this.rarity = rarity;
		this.name = name;
		this.type = type;
		this.isFainted = false;
		this.health = health;
		this.attack = attack;
		this.specialAttack = specialAttack;
	}

	// Setters

	public void setRarity(String newRarity) {
		this.rarity = newRarity;
	}

	public void setHealth(int newHealth) {
		this.health = newHealth;
	}

	public void setAttack(int newAttack) {
		this.attack = newAttack;
	}

	public void setEXAttack(int newSpecial) {
		this.specialAttack = newSpecial;
	}

	// Getters

	public String getRarity() {
		return rarity;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public boolean isFainted() {
		return isFainted;
	}
	
	public int getHealth() {
		return health;
	}
	public int getAttack() {
		return attack;
	}

	public int getSpecialAttack() {
		return specialAttack;
	}

	// In game action
	
	//Later method
	//public void levelUp()

	//Later method
	public void takeDamage(int damage) {
		if( damage < 0) {
			throw new IllegalArgumentException("Damage cannot be negative");
		}
		
		health = Math.max(0,  health - damage);
		isFainted = health == 0;
	}

	public void itemHeal(Item item) {
		if (item.getEffect().equals("heal")) {
			// health = Math.min(maxHealth, currentHealth +30);
			health += 30;
			isFainted = health == 0;
		}
	}
	public void itemBuff(Item item) {
		if (item.getEffect().equals("buff")) {
			this.attack += 15;
			this.specialAttack += 10;
		}
	}
}