package src.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import src.model.Stock;
import src.model.Transaction;
import src.model.User;
import src.storage.DataStorage;
import src.view.DashboardView;

import java.util.Optional;

public class DashboardController {
    private DashboardView view;
    private User user;

    public DashboardController(DashboardView view, User user) {
        this.view = view;
        this.user = user;
    }

    public void buyStock(Stock stock) {
        if (stock == null) {
            showAlert("Select a stock to buy.");
            return;
        }
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buy Stock");
        dialog.setHeaderText("Enter quantity to buy:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int quantity = Integer.parseInt(result.get());
                double totalPrice = quantity * stock.getPrice();

                if (user.getBalance() >= totalPrice) {
                    user.setBalance(user.getBalance() - totalPrice);
                    user.getTransactions().add(new Transaction(stock.getName(), quantity, stock.getPrice(), true));
                    view.refreshBalance();
                    view.refreshTransactions();
                    showAlert("Purchase successful!");
                } else {
                    showAlert("Insufficient balance!");
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid quantity entered.");
            }
        }
    }

    public void sellStock(Stock stock) {
        if (stock == null) {
            showAlert("Select a stock to sell.");
            return;
        }
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Sell Stock");
        dialog.setHeaderText("Enter quantity to sell:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int quantity = Integer.parseInt(result.get());

                if (quantity <= 0) {
                    showAlert("Quantity must be greater than zero.");
                    return;
                }

                // Assume selling without checking ownership for simplicity
                double totalSale = quantity * stock.getPrice();
                user.setBalance(user.getBalance() + totalSale);
                user.getTransactions().add(new Transaction(stock.getName(), quantity, stock.getPrice(), false));
                view.refreshBalance();
                view.refreshTransactions();
                showAlert("Sale successful!");
            } catch (NumberFormatException e) {
                showAlert("Invalid quantity entered.");
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}