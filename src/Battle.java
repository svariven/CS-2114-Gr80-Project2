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
 * Battle only changes the two Pokemon's health and keeps track of
 * fight-only effects (boosts and special attacks); Game decides what
 * happens when the fight is over.
 */
public class Battle
{
    // Damage dealt is the attacker's attack divided by this, so fights last
    // a few turns instead of ending in one hit
    public static final int DAMAGE_DIVISOR = 4;
    // A special attack does this many times normal damage
    public static final int SPECIAL_MULTIPLIER = 2;
    // A boost adds this much attack for the next BOOST_TURNS attacks
    public static final int BOOST_AMOUNT = 20;
    public static final int BOOST_TURNS = 3;

    private Pokemon first;
    private Pokemon second;
    private int firstBoostTurns;
    private int secondBoostTurns;
    private boolean firstUsedSpecial;
    private boolean secondUsedSpecial;

    /**
     * Creates a battle. The first Pokemon attacks first each turn.
     * Time Complexity: O(1)
     *
     * @param first the Pokemon that attacks first
     * @param second the Pokemon that attacks second
     * @throws IllegalArgumentException if either Pokemon is null or both
     *         are the same Pokemon
     */
    public Battle(Pokemon first, Pokemon second)
    {
        if (first == null || second == null)
        {
            throw new IllegalArgumentException("A battle needs two Pokemon");
        }
        if (first == second)
        {
            throw new IllegalArgumentException("A Pokemon cannot fight itself");
        }

        this.first = first;
        this.second = second;
    }

    /**
     * Has the attacker hit the defender once with its attack stat. A
     * fainted attacker cannot attack. Uses up one turn of the attacker's
     * boost.
     * Time Complexity: O(1)
     *
     * @param attacker the Pokemon attacking
     * @param defender the Pokemon taking damage
     * @return the damage dealt, or 0 if the attacker has fainted
     * @throws IllegalArgumentException if the two are not on opposite sides
     *         of this battle
     */
    public int attack(Pokemon attacker, Pokemon defender)
    {
        checkSides(attacker, defender);
        if (attacker.isFainted())
        {
            return 0;
        }

        return hit(attacker, defender, damageFrom(attacker, attacker.getAttack()));
    }

    /**
     * Has the attacker hit the defender with its special attack stat, for
     * SPECIAL_MULTIPLIER times normal damage. Each Pokemon can do this once
     * per battle.
     * Time Complexity: O(1)
     *
     * @param attacker the Pokemon attacking
     * @param defender the Pokemon taking damage
     * @return the damage dealt, or 0 if the attacker has fainted or already
     *         used its special attack
     * @throws IllegalArgumentException if the two are not on opposite sides
     *         of this battle
     */
    public int specialAttack(Pokemon attacker, Pokemon defender)
    {
        checkSides(attacker, defender);
        if (attacker.isFainted() || hasUsedSpecial(attacker))
        {
            return 0;
        }

        if (attacker == first)
        {
            firstUsedSpecial = true;
        }
        else
        {
            secondUsedSpecial = true;
        }
        int damage = SPECIAL_MULTIPLIER
            * damageFrom(attacker, attacker.getSpecialAttack());
        return hit(attacker, defender, damage);
    }

    /**
     * Support move: raises the Pokemon's attack by BOOST_AMOUNT for its
     * next BOOST_TURNS attacks. A boost cannot be stacked.
     * Time Complexity: O(1)
     *
     * @param pokemon the Pokemon to boost
     * @return true if boosted, false if it has fainted or is already boosted
     * @throws IllegalArgumentException if the Pokemon is not in this battle
     */
    public boolean boost(Pokemon pokemon)
    {
        checkInBattle(pokemon);
        if (pokemon.isFainted() || getBoostTurns(pokemon) > 0)
        {
            return false;
        }

        if (pokemon == first)
        {
            firstBoostTurns = BOOST_TURNS;
        }
        else
        {
            secondBoostTurns = BOOST_TURNS;
        }
        return true;
    }

    /**
     * Returns how many more attacks the Pokemon's boost lasts.
     * Time Complexity: O(1)
     *
     * @param pokemon a Pokemon in this battle
     * @return the turns left, 0 if not boosted
     * @throws IllegalArgumentException if the Pokemon is not in this battle
     */
    public int getBoostTurns(Pokemon pokemon)
    {
        checkInBattle(pokemon);
        if (pokemon == first)
        {
            return firstBoostTurns;
        }
        return secondBoostTurns;
    }

    /**
     * Tells whether the Pokemon already used its special attack.
     * Time Complexity: O(1)
     *
     * @param pokemon a Pokemon in this battle
     * @return true if it has been used this battle
     * @throws IllegalArgumentException if the Pokemon is not in this battle
     */
    public boolean hasUsedSpecial(Pokemon pokemon)
    {
        checkInBattle(pokemon);
        if (pokemon == first)
        {
            return firstUsedSpecial;
        }
        return secondUsedSpecial;
    }

    /**
     * Tells whether either Pokemon has fainted.
     * Time Complexity: O(1)
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
     *
     * Time Complexity: O(1)
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

    /**
     * Returns the Pokemon that attacks first.
     * Time Complexity: O(1)
     */
    public Pokemon getFirst()
    {
        return first;
    }

    /**
     * Returns the Pokemon that attacks second.
     * Time Complexity: O(1)
     */
    public Pokemon getSecond()
    {
        return second;
    }

    // Damage from a stat plus any boost, at least 1
    // Time Complexity: O(1)
    private int damageFrom(Pokemon attacker, int stat)
    {
        if (getBoostTurns(attacker) > 0)
        {
            stat += BOOST_AMOUNT;
        }
        return Math.max(1, stat / DAMAGE_DIVISOR);
    }

    // Deals the damage and uses up one turn of the attacker's boost
    // Time Complexity: O(1)
    private int hit(Pokemon attacker, Pokemon defender, int damage)
    {
        defender.takeDamage(damage);
        if (attacker == first && firstBoostTurns > 0)
        {
            firstBoostTurns--;
        }
        else if (attacker == second && secondBoostTurns > 0)
        {
            secondBoostTurns--;
        }
        return damage;
    }

    // Time Complexity: O(1)
    private void checkSides(Pokemon attacker, Pokemon defender)
    {
        checkInBattle(attacker);
        checkInBattle(defender);
        if (attacker == defender)
        {
            throw new IllegalArgumentException(
                "Attacker and defender must be on opposite sides");
        }
    }

    // Time Complexity: O(1)
    private void checkInBattle(Pokemon pokemon)
    {
        if (pokemon == null || (pokemon != first && pokemon != second))
        {
            throw new IllegalArgumentException("That Pokemon is not in this battle");
        }
    }
}
