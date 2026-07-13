package src.model;

import java.io.Serializable;

public class Stock implements Serializable {
    private String name;
    private double price;
    private double previousPrice;
    private double marketCap;

    public Stock(String name, double price, double marketCap) {
        this.name = name;
        this.price = price;
        this.previousPrice = price;
        this.marketCap = marketCap;
    }

    public void fluctuatePrice() {
        this.previousPrice = this.price;
        double change = (Math.random() - 0.5) * 10; // +/- 5
        this.price += change;
        if (this.price < 1) this.price = 1;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getPreviousPrice() { return previousPrice; }
    public double getMarketCap() { return marketCap; }
    public void setMarketCap(double marketCap) { this.marketCap = marketCap; }

    public double getChangePercent() {
        if (previousPrice == 0) return 0;
        return ((price - previousPrice) / previousPrice) * 100;
    }
}