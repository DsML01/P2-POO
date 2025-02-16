import java.util.ArrayList;

public class Product
{
    //private is encapsulation
    private String name;
    private double price;

    public Product(String name, double price)
    {
        this.name = name;
        this.price = price;
    }

    public String getName()
    {
        return name;
    }

    public double getPrice()
    {
        return price;
    }
}
