package my.models;

public class Donation
{
    public String id;
    public int amount;
    public String paymenttype;
    public int upvotes;
    public Donation (String id, int amount, String method, int upvotes)
    {
        this.id = id;
        this.amount = amount;
        this.paymenttype = method;
        this.upvotes = upvotes;
    }
    public Donation ()
    {
        this.amount = 0;
        this.paymenttype = "";
        this.upvotes = 0;
    }
    public String toString()
    {
        return id + ", " + amount + ", " + paymenttype + ", " + upvotes;
    }
}

