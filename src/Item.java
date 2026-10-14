public class Item {
    
    private String name;
    private String effect;
    private int amount;
    
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
    
    public String getName()
    {
        return name;
    }
    
    public String getEffect()
    {
        return effect;
    }
    
    public int getAmount()
    {
        return amount;
    }
    
    public void use()
    {
        if (amount > 0)
        {
            amount--;
        }
    }

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
    
    @Override
    public int hashCode()
    {
        return name.hashCode();
    }
}