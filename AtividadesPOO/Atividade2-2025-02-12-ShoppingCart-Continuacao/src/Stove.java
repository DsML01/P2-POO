//extends is the inheritance that a class get from a father class
public class Stove extends Product
{
    //private is encapsulation
    private int burners;

    public Stove(String brand, double price, int burners)
    {
        //Method to initialize the father class
        super(brand, price);
        this.burners = burners;
    }

    public int getBurners()
    {
        return burners;
    }
}
