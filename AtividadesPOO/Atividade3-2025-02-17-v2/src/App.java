import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double total = 0;

        System.out.println("Enter the number of tax payers: ");

        int amountTaxPayers = sc.nextInt();

        List<being> people = new ArrayList<>(amountTaxPayers);

        System.out.println("\n");

        for(int i = 0; i < amountTaxPayers; i++)
        {
            System.out.printf("Tax payer #%d data:\n", i + 1);

            System.out.println("Individual or company (i/c)?");
            String beingType = sc.next();

            System.out.println("Name: ");
            String name = sc.next();

            System.out.println("Annual income: ");
            double annualIncome = sc.nextDouble();

            if(beingType.equals("i"))
            {
                System.out.println("Health expenditures: ");
                double healthExpenditures = sc.nextDouble();

                people.add(new PF(name, annualIncome, healthExpenditures));
            }
            else if(beingType.equals("c"))
            {
                System.out.println("Number of employees: ");
                int numOfEmployees = sc.nextInt();

                people.add(new PJ(name, annualIncome, numOfEmployees));
            }
            else
            {
                System.out.println("Invalid being type, try again.");
                i -= 1;
            }
        }
        sc.close();

        System.out.println("TAXES PAID: ");
        for(being being : people)
        {
            System.out.printf("%s: $ %.2f\n", being.name, being.getTaxes());

            total += being.getTaxes();
        }
        System.out.printf("\nTOTAL TAXES: $ %.2f", total);

    }

}
