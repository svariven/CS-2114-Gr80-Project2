public class Item {
    
    private String name;
    private String effect;
    private int amount;
    
    /**
     * Creates an item with a name, effect, and amount
     * Time Complexity: O(n), where n is the length of the name. 
     */
    public Item(String name, String effect, int amount)
    {
        if (amount < 0)
        {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Item name cannot be empty");
        }
        
        this.name = name;
        this.effect = effect;
        this.amount = amount;
    }
    
    /**
     * Returns the item's name
     * Time Complexity: O(1)
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Returns the item's effect
     * Time Complexity: O(1)
     */
    public String getEffect()
    {
        return effect;
    }
    
    /**
     * Returns the quantity of the item
     * Time Complexity: O(1)
     */
    public int getAmount()
    {
        return amount;
    }
    
    /**
     * Uses one item if the amount is greater than zero
     * Time Complexity: O(1)
     */
    public void use()
    {
        if (amount > 0)
        {
            amount--;
        }
    }

    /**
     * Checks if two items have the same name.
     * Time Complexity: O(n), where n is the length of the name. 
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        
        if (!(obj instanceof Item))
        {
            return false;
        }
        
        Item other = (Item)obj;
        return name.equals(other.name);
    }
    /**
     * Returns a hash code based on the item's name
     * Time Complexity: O(n), where n is the length of the name
     */
    @Override
    public int hashCode()
    {
        return name.hashCode();
    }
    
    /**
     * Returns a string describing the item's properties
     * Time Complexity: O(n), where n is the total length of the name and 
     * effect strings
     */
    @Override
    public String toString() {
        return name + " (Effect: " + effect
            + ", Quantity: " + amount + ")";
    }
}