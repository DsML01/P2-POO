public class PF extends being{
    double healthExpenses;

    public PF(String name, double annualIncome, double healthExpenses)
    {
        super(name, annualIncome);
        this.healthExpenses = healthExpenses;
    }

    public double getTaxes()
    {
        double tax = annualIncome < 20000 ? 0.15: 0.25;

        return (annualIncome * tax) - (healthExpenses * 0.5);
    }
}
