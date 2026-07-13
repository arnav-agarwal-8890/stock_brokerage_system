package src.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    private String stockName;
    private int quantity;
    private double price;
    private LocalDateTime dateTime;
    private boolean isBuy; // true = buy, false = sell

    public Transaction(String stockName, int quantity, double price, boolean isBuy) {
        this.stockName = stockName;
        this.quantity = quantity;
        this.price = price;
        this.dateTime = LocalDateTime.now();
        this.isBuy = isBuy;
    }

    // Getters
    public String getStockName() { return stockName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public LocalDateTime getDateTime() { return dateTime; }
    public boolean isBuy() { return isBuy; }
}