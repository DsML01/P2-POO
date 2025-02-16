public class TV extends Product
{
    //private is encapsulation
    private int inches;

    public TV(String name, double price, int inches)
    {
        super(name, price); //calls the constructor of the father class
        this.inches = inches;
    }

    public int getInches() {
        return inches;
    }
}
