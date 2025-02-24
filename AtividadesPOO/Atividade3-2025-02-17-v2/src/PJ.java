public class PJ extends being {
    int staffAmount;

    public PJ(String name, double annualIncome, int staffAmount)
    {
        super(name, annualIncome);
        this.staffAmount = staffAmount;
    }

    public double getTaxes()
    {
        double tax = staffAmount >  10 ? 0.14 : 0.16;

        return this.annualIncome * tax;
    }
}