public class Refrigerator extends Product
{
    //private is encapsulation
    private int size;

    public Refrigerator(String name, double price, int size)
    {
        super(name, price); //calls the constructor of the father class
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
