public class Aircraft
{
    //attributes
    //private is used to encapsulate an attribute
    //encapsulate is the attribute ability to be changed only by methods and never by the object itself
    private String manufacturer;
    private int speed;
    private int numberOfEngines;

    //constructor method
    public Aircraft(String manufacturer, int speed, int numberOfEngines)
    {
        this.manufacturer = manufacturer;
        this.speed = speed;
        this.numberOfEngines = numberOfEngines;
    }
}

//we do not care how the methods are implemented, we care only if it works
//So, we hide the implementation and focus only on what it can do


