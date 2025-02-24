public class Product
{
    //private is encapsulation
    private String brand;
    private double price;

    //Constructor: Method used to initialize an object
    public Product(String brand, double price)
    {
        this.brand = brand;
        this.price = price;
    }

    public String getBrand()
    {
        return brand;
    }

    public double getPrice()
    {
        return price;
    }
}
