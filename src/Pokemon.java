/**
 * The Pokemon class represents the entitites in action during the battle phase
 * and the players' collection. They signal progression.
 * 
 */
public class Pokemon {

	private int level;
	private String name;
	private String type;
	private boolean isFainted;
	private int currentHealth;
	private int maxHealth;
	private int attack;
	private int specialAttack;

	public Pokemon(int level, String name, String type, int maxHealth, int currentHealth, int attack, int specialAttack) {
		if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Pokemon name cannot be empty");
        }
		if (type == null || type.trim().isEmpty())
        {
            throw new IllegalArgumentException("Pokemon type cannot be empty");
        }
		if (level <= 0) {
			throw new IllegalArgumentException("Pokemon have a minimul level of 1");
		}
		if (maxHealth <= 0) {
			throw new IllegalArgumentException("Pokemon cannot have 0 as max health");
		}
		if (attack < 0 || specialAttack < 0) {
			throw new IllegalArgumentException("All attack values must be non-negative");
		}
		
		this.level = level;
		this.name = name;
		this.type = type;
		this.isFainted = false;
		this.maxHealth = maxHealth;
		this.currentHealth = currentHealth;
		this.attack = attack;
		this.specialAttack = specialAttack;
	}

	// Setters

	public void setLevel(int newLevel) {
		this.level = newLevel;
	}

	public void setMaxHealth(int newMax) {
		this.maxHealth = newMax;
	}
	public void setCurrentHealth(int newCurrent) {
		this.currentHealth = newCurrent;
	}

	public void setAttack(int newAttack) {
		this.attack = newAttack;
	}

	public void setEXAttack(int newSpecial) {
		this.specialAttack = newSpecial;
	}

	// Getters

	public int getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public boolean isFainted() {
		return isFainted;
	}
	
	public int getCurrentHealth() {
		return currentHealth;
	}
	public int getAttack() {
		return attack;
	}

	public int getSpecialAttack() {
		return specialAttack;
	}

	// In game action
	
	//Later method
	public void levelUp() {
		this.level += 1;
	}

	//Later method
	public void takeDamage(int damage) {
		if( damage < 0) {
			throw new IllegalArgumentException("Damage cannot be negative");
		}
		
		currentHealth = Math.max(0,  currentHealth - damage);
		isFainted = currentHealth == 0;
	}

	public void itemHeal(Item item) {
		if (item.getEffect().equals("heal")) {
			currentHealth = Math.min(maxHealth, currentHealth +30);
			isFainted = currentHealth == 0;
		}
	}
	public void itemBuff(Item item) {
		if (item.getEffect().equals("buff")) {
			this.attack += 15;
			this.specialAttack += 10;
		}
	}
}