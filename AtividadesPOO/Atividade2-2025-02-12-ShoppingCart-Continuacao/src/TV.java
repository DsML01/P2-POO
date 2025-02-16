public class TV extends Product
{
    //private is encapsulation
    private int inches;

    public TV(String brand, double price, int inches)
    {
        super(brand, price); //calls the constructor of the father class
        this.inches = inches;
    }

    public int getInches() {
        return inches;
    }
}
