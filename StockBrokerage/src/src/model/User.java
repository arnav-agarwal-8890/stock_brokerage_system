package src.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String username;
    private String password;
    private double balance;
    private List<Transaction> transactions;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 10000.0; // Default balance
        this.transactions = new ArrayList<>();
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public List<Transaction> getTransactions() { return transactions; }
}