//As a Hokie, I will conduct myself with honor and integrity at all times.
//I will not lie, cheat, or steal, nor will I
//accept the actions of those who do.
//-- Rushil Mishra (rushilm)

/**
 * Owns one player's Pokemon collection and item bag. Player is the only
 * class that touches the two LinkedChains; it holds no game rules, only
 * storage and lookup. Bad input is rejected here (null is ignored, a missing
 * Pokemon or item returns false/0) so the chains never see it.
 */
public class Player
{
    private String name;
    private LinkedChain<Pokemon> party;
    private LinkedChain<Item> bag;

    /**
     * Creates a player with empty collections. A null or blank name
     * becomes "Player".
     *
     * @param name the player's name
     */
    public Player(String name)
    {
        if (name == null || name.trim().isEmpty())
        {
            this.name = "Player";
        }
        else
        {
            this.name = name.trim();
        }

        party = new LinkedChain<Pokemon>();
        bag = new LinkedChain<Item>();
    }

    public String getName()
    {
        return name;
    }

    /**
     * Adds a Pokemon to the collection. A null Pokemon is rejected.
     * Duplicates are allowed. O(1).
     *
     * @param pokemon the Pokemon to add
     */
    public void addPokemon(Pokemon pokemon)
    {
        if (pokemon == null)
        {
            return;
        }

        party.add(pokemon);
    }

    /**
     * Removes one matching Pokemon from the collection. O(n).
     *
     * @param pokemon the Pokemon to remove
     * @return true if one was removed, false if it is null or not owned
     */
    public boolean removePokemon(Pokemon pokemon)
    {
        if (pokemon == null)
        {
            return false;
        }

        return party.remove(pokemon);
    }

    /**
     * Tells whether the collection holds the Pokemon. O(n).
     *
     * @param pokemon the Pokemon to look for
     * @return true if owned, false if it is null or not owned
     */
    public boolean hasPokemon(Pokemon pokemon)
    {
        if (pokemon == null)
        {
            return false;
        }

        return party.contains(pokemon);
    }

    /**
     * Gets the Pokemon at the given position in the collection.
     *
     * @param index position of the Pokemon
     * @return the Pokemon at that position
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Pokemon getPokemon(int index)
    {
        return party.get(index);
    }

    /**
     * Returns how many Pokemon the player owns. O(1).
     *
     * @return the number of Pokemon
     */
    public int getPokemonCount()
    {
        return party.size();
    }

    /**
     * Adds an item to the bag. If a stack with the same name is already
     * there, the amounts are combined into one stack. A null item or an
     * item with an amount of 0 is rejected.
     *
     * @param item the item (stack) to add
     */
    public void addItem(Item item)
    {
        if (item == null || item.getAmount() <= 0)
        {
            return;
        }

        Item existing = findItem(item.getName());
        if (existing == null)
        {
            bag.add(item);
        }
        else
        {
            // Item has no way to raise its amount, so swap in a combined stack
            bag.remove(existing);
            bag.add(new Item(existing.getName(), existing.getEffect(),
                existing.getAmount() + item.getAmount()));
        }
    }

    /**
     * Uses one of the named item. A stack that reaches 0 is removed from
     * the bag.
     *
     * @param itemName the name of the item to use
     * @return true if one was used, false if the name is null or the bag
     *         has none of that item
     */
    public boolean useItem(String itemName)
    {
        Item item = findItem(itemName);
        if (item == null || item.getAmount() <= 0)
        {
            return false;
        }

        item.use();
        if (item.getAmount() == 0)
        {
            bag.remove(item);
        }
        return true;
    }

    /**
     * Returns how many of the named item the player has.
     *
     * @param itemName the name of the item
     * @return the amount, or 0 if the name is null or the item is not in
     *         the bag
     */
    public int getItemAmount(String itemName)
    {
        Item item = findItem(itemName);
        if (item == null)
        {
            return 0;
        }

        return item.getAmount();
    }

    /**
     * Returns how many different item stacks are in the bag. O(1).
     *
     * @return the number of stacks (not the total of all amounts)
     */
    public int getItemCount()
    {
        return bag.size();
    }

    // Finds the stack with this exact name, or null. Uses only size() and
    // get(i), and get(i) is itself O(n), so this is O(n^2) in the worst case.
    private Item findItem(String itemName)
    {
        if (itemName == null)
        {
            return null;
        }

        for (int i = 0; i < bag.size(); i++)
        {
            Item candidate = bag.get(i);
            if (candidate.getName().equals(itemName))
            {
                return candidate;
            }
        }
        return null;
    }
}