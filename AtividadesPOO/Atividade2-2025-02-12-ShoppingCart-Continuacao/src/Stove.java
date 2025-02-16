public class Stove extends Product
{
    //private is encapsulation
    private int burners;

    public Stove(String brand, double price, int burners)
    {
        super(brand, price);
        this.burners = burners;
    }

    public int getBurners()
    {
        return burners;
    }
}
