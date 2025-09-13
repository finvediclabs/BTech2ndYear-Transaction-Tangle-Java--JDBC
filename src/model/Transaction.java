package model;

public class Transaction {
    private int id;
    private int fromAccountId;
    private int toAccountId;
    private double amount;
    private String timestamp;

    public Transaction(int id, int fromAccountId, int toAccountId, double amount, String timestamp) {
        this.id = id;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public int getFromAccountId() { return fromAccountId; }
    public int getToAccountId() { return toAccountId; }
    public double getAmount() { return amount; }
    public String getTimestamp() { return timestamp; }
}
