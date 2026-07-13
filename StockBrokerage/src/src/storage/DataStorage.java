package src.storage;

import src.model.User;
import src.model.Stock;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    public static List<User> users = new ArrayList<>();
    public static List<Stock> stocks = new ArrayList<>();

    public static void loadData() {
        try (ObjectInputStream inUser = new ObjectInputStream(new FileInputStream("users.ser"));
             ObjectInputStream inStock = new ObjectInputStream(new FileInputStream("stocks.ser"))) {
            users = (List<User>) inUser.readObject();
            stocks = (List<Stock>) inStock.readObject();
        } catch (Exception e) {
            System.out.println("Data not found. Starting fresh.");
            users = new ArrayList<>();
            stocks = new ArrayList<>();
            stocks.add(new Stock("Google", 1500, 1500));
            stocks.add(new Stock("Apple", 120, 2200));
            stocks.add(new Stock("Amazon", 3000, 1800));
            stocks.add(new Stock("Ola", 100, 300));
            stocks.add(new Stock("Tata", 600, 800));
            stocks.add(new Stock("Bata", 200, 400));
            stocks.add(new Stock("TCS", 1000, 1500));
            stocks.add(new Stock("Mahindra", 1200, 1800));
            stocks.add(new Stock("Hindustan Unilever", 1300, 2000));
            stocks.add(new Stock("RIL", 1100, 3500));
            stocks.add(new Stock("MRF", 700, 1500));
            stocks.add(new Stock("Shell", 900, 300));
            stocks.add(new Stock("Zomato", 100, 50));
            stocks.add(new Stock("HDFC", 1100, 1500));
            stocks.add(new Stock("Bajaj", 325, 500));
            stocks.add(new Stock("Airtel", 250, 300));
            stocks.add(new Stock("Adani", 1325, 1000));
        }
    }

    public static void saveData() {
        try (ObjectOutputStream outUser = new ObjectOutputStream(new FileOutputStream("users.ser"));
             ObjectOutputStream outStock = new ObjectOutputStream(new FileOutputStream("stocks.ser"))) {
            outUser.writeObject(users);
            outStock.writeObject(stocks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}