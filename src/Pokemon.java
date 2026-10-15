//As a Hokie, I will conduct myself with honor and integrity at all times.
//I will not lie, cheat, or steal, nor will I
//accept the actions of those who do.
//-- Richard Samson (skye25)

//LLM Statement:
//
//During the preparation of this assignment, I, Richard Samson used Chat GPT
//for designing and programming the Pokemon class.
//After using this tool, I reviewed and edited the content as needed
//to ensure its accuracy and take full responsibility for the content in
//relation to grading. I understand that I am responsible for being able to
//complete this work without the use of assistance.

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
	
	
	/**
     * Constructs a Pokemon with the specified rarity, name, type,
     * health, attack, and special attack values.
     *
     * @param rarity the Pokemon's rarity; must not be null or blank
     * @param name the Pokemon's name; must not be null or blank
     * @param type the Pokemon's type; must not be null or blank
     * @param health the Pokemon's initial health; must be non-negative
     * @param attack the Pokemon's physical attack value; must be non-negative
     * @param specialAttack the Pokemon's special attack value;
     *                      must be non-negative
     * @throws IllegalArgumentException if a string argument is null or blank,
     *         or if health or either attack value is negative
     *
     * Time complexity: O(1).
     */
	public Pokemon(String rarity, String name, String type, int health, int attack, int specialAttack) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Pokemon name cannot be empty");
		}
		if (type == null || type.trim().isEmpty()) {
			throw new IllegalArgumentException("Pokemon type cannot be empty");
		}
		if (rarity == null || rarity.trim().isEmpty()) {
			throw new IllegalArgumentException("Pokemon rarity cannot be empty");
		}
		if (health < 0) {
			throw new IllegalArgumentException("Pokemon initial health cannot be <0");
		}
		if (attack < 0 || specialAttack < 0) {
			throw new IllegalArgumentException("All attack values must be non-negative");
		}

		this.rarity = rarity;
		this.name = name;
		this.type = type;
		this.isFainted = health == 0;
		this.health = health;
		this.attack = attack;
		this.specialAttack = specialAttack;
	}

	// Setters

	/**
     * Updates this Pokemon's rarity.
     *
     * @param newRarity the new rarity value
     *
     * Time complexity: O(1).
     */
	public void setRarity(String newRarity) {
		if (newRarity == null || newRarity.trim().isEmpty()){
			throw new IllegalArgumentException("Pokemon name cannot be empty");
		}
		this.rarity = newRarity;
	}

	/**
     * Updates this Pokemon's health.
     *
     * @param newHealth the new health value
     *
     * Time complexity: O(1).
     */
	public void setHealth(int newHealth) {
		if (newHealth < 0) {
			throw new IllegalArgumentException("Pokemon initial health cannot be negative");
		}

		this.health = newHealth;
		this.isFainted = (newHealth == 0);
	}
	
	/**
     * Updates this Pokemon's physical attack value.
     *
     * @param newAttack the new attack value
     *
     * Time complexity: O(1).
     */
	public void setAttack(int newAttack) {
		if (newAttack < 0) {
			throw new IllegalArgumentException("All attack values must be non-negative");
		}
		
		this.attack = newAttack;
	}

	/**
     * Updates this Pokemon's special attack value.
     *
     * @param newSpecial the new special attack value
     *
     * Time complexity: O(1).
     */
	public void setSpecialAttack(int newSpecial) {
		if (newSpecial < 0) {
			throw new IllegalArgumentException("All attack values must be non-negative");
		}
		this.specialAttack = newSpecial;
	}

	// Getters

	/**
     * Returns this Pokemon's rarity.
     *
     * @return the Pokemon's rarity
     *
     * Time complexity: O(1).
     */
	public String getRarity() {
		return rarity;
	}

	/**
     * Returns this Pokemon's name.
     *
     * @return the Pokemon's name
     *
     * Time complexity: O(1).
     */
	public String getName() {
		return name;
	}
	
	/**
     * Returns this Pokemon's type.
     *
     * @return the Pokemon's type
     *
     * Time complexity: O(1).
     */
	public String getType() {
		return type;
	}

	/**
     * Checks whether this Pokemon is fainted.
     *
     * @return true if the Pokemon is fainted; false otherwise
     *
     * Time complexity: O(1).
     */
	public boolean isFainted() {
		return isFainted;
	}

	/**
     * Returns this Pokemon's current health.
     *
     * @return the Pokemon's current health
     *
     * Time complexity: O(1).
     */
	public int getHealth() {
		return health;
	}

	/**
     * Returns this Pokemon's physical attack value.
     *
     * @return the Pokemon's attack value
     *
     * Time complexity: O(1).
     */
	public int getAttack() {
		return attack;
	}

	/**
     * Returns this Pokemon's special attack value.
     *
     * @return the Pokemon's special attack value
     *
     * Time complexity: O(1).
     */
	public int getSpecialAttack() {
		return specialAttack;
	}

	// In game action

	// Later method
	// public void levelUp()

	
	// Later method
	/**
     * Applies damage to this Pokemon. Health cannot fall below zero.
     * Updates the fainted status based on the resulting health.
     *
     * @param damage the amount of damage to apply; must be non-negative
     * @throws IllegalArgumentException if damage is negative
     *
     * Time complexity: O(1).
     */
	public void takeDamage(int damage) {
		if (damage < 0) {
			throw new IllegalArgumentException("Damage cannot be negative");
		}

		health = Math.max(0, health - damage);
		if (health <= 0) {
			health = 0;
			isFainted = health == 0;
		}
	}

	/**
     * Heals this Pokemon by 30 health points if the item's effect
     * is "heal". Health is not capped at a maximum value.
     *
     * @param item the item used to heal this Pokemon
     * @throws NullPointerException if item or its effect is null
     *
     * Time complexity: O(1).
     */
	public void itemHeal(Item item) {
		if (item == null || item.getEffect() == null) {
			throw new NullPointerException("Item or effect cannot be null");
		}
		if (item.getEffect().equals("heal")) {
			// health = Math.min(maxHealth, currentHealth +30);
			health += 30;
			isFainted = health == 0;
		}
	}

	/**
     * Increases this Pokemon's physical attack by 15 and special
     * attack by 10 if the item's effect is "buff".
     *
     * @param item the item used to buff this Pokemon
     * @throws NullPointerException if item or its effect is null
     *
     * Time complexity: O(1).
     */
	public void itemBuff(Item item) {
		if (item == null || item.getEffect() == null) {
			throw new NullPointerException("Item or effect cannot be null");
		}
		if (item.getEffect().equals("buff")) {
			this.attack += 15;
			this.specialAttack += 10;
		}
	}
	
	/**
     * Returns a readable description of this Pokemon, including
     * its name, rarity, type, and current health.
     *
     * @return a formatted string describing this Pokemon
     *
     * Time complexity: O(L), where L is the total length of the
     * string values included in the result.
     */
	@Override
	public String toString()
	{
		return name + "[Rarity: " + rarity + "]" + type + " type, HP: " + health;
	}
}