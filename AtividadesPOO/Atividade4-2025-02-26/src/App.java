import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try{
            System.out.println("Enter account data:");

            System.out.print("Number: ");
            int number = sc.nextInt();
            sc.nextLine();

            System.out.print("Holder:");
            String holder = sc.nextLine();

            System.out.print("Initial balance: ");
            double initialBalance = sc.nextDouble();

            System.out.print("Withdraw limit: ");
            double withdrawLimit = sc.nextDouble();

            Account account = new Account(number, holder, initialBalance, withdrawLimit);

            System.out.println("\nEnter amount to withdraw:");
            double amount = sc.nextDouble();

            account.withdraw(amount);
            System.out.printf("New balance: $ %.2f\n", account.getBalance());
        } catch(Exception e){
            System.out.println("Error: "+ e.getMessage());
        } finally{
            sc.close();
        }

    }
}
