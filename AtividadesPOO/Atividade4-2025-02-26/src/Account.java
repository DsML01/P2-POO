public class Account {
    private int number;
    private String holder;
    private double balance;
    private double withdrawLimit;

    public Account(int number, String holder, double balance, double withdrawLimit)
    {
        this.number = number;
        this.holder = holder;
        this.balance = balance;
        this.withdrawLimit = withdrawLimit;
    }

    public void deposit(Double amount)
    {
        balance += amount;
    }

    public void withdraw(Double amount) throws Exception
    {
        if(amount > withdrawLimit){
            throw new Exception("Withdraw amount exceeds the limit");
        }
        if(amount > balance){
            throw new Exception("Insufficient balance");
        }
        balance -= amount;
    }

    public double getBalance(){
        return balance;
    }
}
