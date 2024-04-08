package pharmacity;

import java.util.Date;

public class Product {
    private String ID;
    private String Name;
    private Date Expiry;
    private int QuantityEntered;
    private int AmountSold;

    public Product() {
    }

    public Product(String ID, String name, Date expiry, int quantityEntered, int amountSold) {
        this.ID = ID;
        Name = name;
        Expiry = expiry;
        QuantityEntered = quantityEntered;
        AmountSold = amountSold;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Date getExpiry() {
        return Expiry;
    }

    public void setExpiry(Date expiry) {
        Expiry = expiry;
    }

    public int getQuantityEntered() {
        return QuantityEntered;
    }

    public void setQuantityEntered(int quantityEntered) {
        QuantityEntered = quantityEntered;
    }

    public int getAmountSold() {
        return AmountSold;
    }

    public void setAmountSold(int amountSold) {
        AmountSold = amountSold;
    }
}
