import java.util.ArrayList;

public class ShoppingCart
{
    private int customerID;
    private ArrayList<Product> products;

    public ShoppingCart(int customerID)
    {
        this.customerID = customerID;
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product)
    {
        this.products.add(product);
    }

    public void removeProduct(Product product)
    {
        products.remove(product);
    }

    public String getContents() {
        StringBuilder contents = new StringBuilder("Shopping Cart Contents:\n");
        for (Product product : products) {
            contents.append("- ").append(product.getBrand()).append(" ($").append(product.getPrice()).append(")\n");
        }
        return contents.toString();
//      Ex de saída:
//      Shopping Cart Contents:
//      - Apple ($0.99)
//      - Banana ($0.59)

    }

    public int getCustomerID()
    {
        return customerID;
    }

    public int getItemCount()
    {
        return products.size();
    }

    public double getTotalPrice()
    {
        double total = 0;
        for(Product product : products) //for product in products
        {
            total += product.getPrice();
        }
        return total;
    }
}


