public class Stove extends Product
{
    //private is encapsulation
    private int burners;

    public Stove(String name, double price, int burners)
    {
        super(name, price);
        this.burners = burners;
    }

    public int getBurners()
    {
        return burners;
    }
}
