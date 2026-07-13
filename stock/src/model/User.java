package model;

import java.util.HashMap;
import java.util.Map;

public class User {
	private String username;
    private double balance;
    private Map<String, Integer> portfolio;

    public User(String username, double initialBalance) {
        this.username = username;
        this.balance = initialBalance;
        this.portfolio = new HashMap<>();
    }

    public String getUsername() { return username; }
    public double getBalance() { return balance; }
    public Map<String, Integer> getPortfolio() { return portfolio; }
    public void setBalance(double balance) { this.balance = balance; }
}
