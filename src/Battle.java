//As a Hokie, I will conduct myself with honor and integrity at all times.
//I will not lie, cheat, or steal, nor will I
//accept the actions of those who do.
//-- Ishaan Srichitturi (Ishaan07)

//LLM Statement:
//
//During the preparation of this assignment, I, Ishaan Srichitturi used claude
//for designing and programming the Battle class.
//After using this tool, I reviewed and edited the content as needed
//to ensure its accuracy and take full responsibility for the content in
//relation to grading. I understand that I am responsible for being able to
//complete this work without the use of assistance.

/**
 * Runs one turn-based fight between two Pokemon until one reaches 0 HP.
 * Battle only changes the two Pokemon's health; Game decides what happens
 * when the fight is over.
 */
public class Battle
{
    // Damage dealt is the attacker's attack divided by this, so fights last
    // a few turns instead of ending in one hit
    public static final int DAMAGE_DIVISOR = 4;

    private Pokemon first;
    private Pokemon second;

    /**
     * Creates a battle. The first Pokemon attacks first each turn. O(1).
     *
     * @param first the Pokemon that attacks first
     * @param second the Pokemon that attacks second
     * @throws IllegalArgumentException if either Pokemon is null
     */
    public Battle(Pokemon first, Pokemon second)
    {
        if (first == null || second == null)
        {
            throw new IllegalArgumentException("A battle needs two Pokemon");
        }

        this.first = first;
        this.second = second;
    }

    /**
     * Has the attacker hit the defender once. A fainted attacker cannot
     * attack. O(1).
     *
     * @param attacker the Pokemon attacking
     * @param defender the Pokemon taking damage
     * @return the damage dealt, or 0 if the attacker has fainted
     * @throws IllegalArgumentException if either Pokemon is not in this
     *         battle or both are the same Pokemon
     */
    public int attack(Pokemon attacker, Pokemon defender)
    {
        if (!isInBattle(attacker) || !isInBattle(defender)
            || attacker == defender)
        {
            throw new IllegalArgumentException(
                "Both Pokemon must be on opposite sides of this battle");
        }

        if (attacker.isFainted())
        {
            return 0;
        }

        int damage = Math.max(1, attacker.getAttack() / DAMAGE_DIVISOR);
        defender.takeDamage(damage);
        return damage;
    }

    /**
     * Tells whether either Pokemon has fainted. O(1).
     *
     * @return true if the battle is over
     */
    public boolean isOver()
    {
        return first.isFainted() || second.isFainted();
    }

    /**
     * Runs one turn: the first Pokemon attacks, then the second attacks
     * back if it is still standing. Does nothing once the battle is over.
     * O(1).
     */
    public void runTurn()
    {
        if (isOver())
        {
            return;
        }

        attack(first, second);
        if (!isOver())
        {
            attack(second, first);
        }
    }

    public Pokemon getFirst()
    {
        return first;
    }

    public Pokemon getSecond()
    {
        return second;
    }

    private boolean isInBattle(Pokemon pokemon)
    {
        return pokemon != null && (pokemon == first || pokemon == second);
    }
}
