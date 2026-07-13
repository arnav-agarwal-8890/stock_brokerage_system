package service;

import model.Stock;
import model.User;
import java.util.HashMap;
import java.util.Map;

public class StockBrokerageService {
	private Map<String, Stock> stocks;
    private Map<String, User> users;

    public StockBrokerageService() {
        this.stocks = new HashMap<>();
        this.users = new HashMap<>();
        initializeStocks();
    }

    private void initializeStocks() {
        stocks.put("AAPL", new Stock("AAPL", "Apple Inc.", 150.0, 1000));
        stocks.put("GOOGL", new Stock("GOOGL", "Alphabet Inc.", 2800.0, 500));
        stocks.put("MSFT", new Stock("MSFT", "Microsoft Corporation", 300.0, 750));
    }

    public void registerUser(String username, double initialBalance) {
        users.put(username, new User(username, initialBalance));
    }

    public boolean buyStock(String username, String symbol, int quantity) {
        User user = users.get(username);
        Stock stock = stocks.get(symbol);
        
        if (user == null || stock == null || stock.getQuantity() < quantity) {
            return false;
        }

        double totalCost = stock.getPrice() * quantity;
        if (user.getBalance() < totalCost) {
            return false;
        }

        user.setBalance(user.getBalance() - totalCost);
        stock.setQuantity(stock.getQuantity() - quantity);
        user.getPortfolio().merge(symbol, quantity, Integer::sum);
        return true;
    }

    public boolean sellStock(String username, String symbol, int quantity) {
        User user = users.get(username);
        Stock stock = stocks.get(symbol);
        
        if (user == null || stock == null) {
            return false;
        }

        Integer ownedQuantity = user.getPortfolio().get(symbol);
        if (ownedQuantity == null || ownedQuantity < quantity) {
            return false;
        }

        double totalValue = stock.getPrice() * quantity;
        user.setBalance(user.getBalance() + totalValue);
        stock.setQuantity(stock.getQuantity() + quantity);
        
        int remainingQuantity = ownedQuantity - quantity;
        if (remainingQuantity == 0) {
            user.getPortfolio().remove(symbol);
        } else {
            user.getPortfolio().put(symbol, remainingQuantity);
        }
        return true;
    }

    public Map<String, Stock> getStocks() { return stocks; }
    public Map<String, User> getUsers() { return users; }
}
