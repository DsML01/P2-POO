abstract class being {
    //since these attributes are not private, subclasses inherit them directly.
    String name;
    double annualIncome;

    public being(String name, double annualIncome) {
        this.name = name;
        this.annualIncome = annualIncome;
    }

    public abstract double getTaxes(); //It has no body cuz it must be implemented in subclasses.
}
