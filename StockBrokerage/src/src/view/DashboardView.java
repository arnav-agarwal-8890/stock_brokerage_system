package src.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import src.controller.DashboardController;
import src.model.Stock;
import src.model.Transaction;
import src.model.User;
import src.storage.DataStorage;

import java.util.*;

public class DashboardView {
    private Stage primaryStage;
    private User user;
    private VBox layout;
    private Label balanceLabel;
    private TableView<Stock> stockTable;
    private TableView<String> transactionTable;
    private TableView<ProfitLossRow> profitLossTable;
    private DashboardController controller;

    public DashboardView(Stage stage, User user) {
        this.primaryStage = stage;
        this.user = user;
        this.controller = new DashboardController(this, user);

        layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        balanceLabel = new Label("Balance: $" + String.format("%.2f", user.getBalance()));

        // Stock Table
        stockTable = new TableView<>();
        stockTable.setPlaceholder(new Label("No stocks available."));
        stockTable.setItems(FXCollections.observableArrayList(DataStorage.stocks));
        stockTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Stock, String> nameCol = new TableColumn<>("Stock");
        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));

        TableColumn<Stock, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cell -> new SimpleStringProperty(String.format("$%.2f", cell.getValue().getPrice())));

        TableColumn<Stock, String> changeCol = new TableColumn<>("Change %");
        changeCol.setCellValueFactory(cell -> {
            double change = cell.getValue().getChangePercent();
            String arrow = change >= 0 ? "↑" : "↓";
            return new SimpleStringProperty(String.format("%s %.2f%%", arrow, Math.abs(change)));
        });

        // Color styling: Green ↑, Blue ↓
        changeCol.setCellFactory(column -> new TableCell<Stock, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle(item.startsWith("↑") ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
                }
            }
        });

        TableColumn<Stock, String> marketCapCol = new TableColumn<>("Market Cap");
        marketCapCol.setCellValueFactory(cell -> {
            double marketCap = cell.getValue().getPrice() * 1_000_000;
            return new SimpleStringProperty(String.format("$%.2fM", marketCap / 1_000_000));
        });

        stockTable.getColumns().addAll(nameCol, priceCol, changeCol, marketCapCol);

        // Buttons
        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER);
        Button buyButton = new Button("Buy");
        Button sellButton = new Button("Sell");
        actionBox.getChildren().addAll(buyButton, sellButton);

        buyButton.setOnAction(e -> controller.buyStock(stockTable.getSelectionModel().getSelectedItem()));
        sellButton.setOnAction(e -> controller.sellStock(stockTable.getSelectionModel().getSelectedItem()));

        // Transaction Table
        transactionTable = new TableView<>();
        transactionTable.setPrefWidth(500); // widened from 400 to 500
        transactionTable.setPlaceholder(new Label("No transactions yet."));
        TableColumn<String, String> transactionCol = new TableColumn<>("Transaction History");
        transactionCol.setPrefWidth(500); // widen the column itself
        transactionCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        transactionTable.getColumns().add(transactionCol);

        // Profit/Loss Table
        profitLossTable = new TableView<>();
        profitLossTable.setPrefWidth(400);
        TableColumn<ProfitLossRow, String> stockPLCol = new TableColumn<>("Stock");
        stockPLCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStockName()));

        TableColumn<ProfitLossRow, String> changePLCol = new TableColumn<>("Change");
        changePLCol.setCellValueFactory(cell -> {
            double change = cell.getValue().getChangeAmount();
            double percent = cell.getValue().getChangePercent();
            return new SimpleStringProperty(String.format("%+.2f (%.2f%%)", change, percent));
        });

        // Apply red/green styling
        changePLCol.setCellFactory(column -> new TableCell<ProfitLossRow, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    ProfitLossRow row = getTableView().getItems().get(getIndex());
                    setStyle("-fx-text-fill: " + (row.getChangeAmount() >= 0 ? "green;" : "red;"));
                }
            }
        });

        profitLossTable.getColumns().addAll(stockPLCol, changePLCol);

        // Lower Section Layout
        HBox bottomArea = new HBox(20);
        bottomArea.setAlignment(Pos.TOP_CENTER);
        bottomArea.getChildren().addAll(transactionTable, profitLossTable);

        layout.getChildren().addAll(balanceLabel, new Label("Available Stocks:"), stockTable, actionBox, bottomArea);

        refreshTransactions();
        refreshProfitLoss();
        startStockPriceUpdate();
    }

    public VBox getView() {
        return layout;
    }

    public void refreshBalance() {
        balanceLabel.setText("Balance: $" + String.format("%.2f", user.getBalance()));
    }

    public void refreshTransactions() {
        ObservableList<String> transactions = FXCollections.observableArrayList();
        user.getTransactions().forEach(t -> {
            String type = t.isBuy() ? "Bought" : "Sold";
            String line = type + " " + t.getQuantity() + " of " + t.getStockName() + " at $" + String.format("%.2f", t.getPrice());
            transactions.add(line);
        });
        transactionTable.setItems(transactions);
    }

    public void refreshProfitLoss() {
        Map<String, List<Transaction>> boughtMap = new HashMap<>();
        for (Transaction t : user.getTransactions()) {
            if (t.isBuy()) {
                boughtMap.computeIfAbsent(t.getStockName(), k -> new ArrayList<>()).add(t);
            }
        }

        ObservableList<ProfitLossRow> rows = FXCollections.observableArrayList();

        for (Map.Entry<String, List<Transaction>> entry : boughtMap.entrySet()) {
            String stockName = entry.getKey();
            List<Transaction> transactions = entry.getValue();

            double totalCost = 0;
            int totalQty = 0;

            for (Transaction t : transactions) {
                totalCost += t.getPrice() * t.getQuantity();
                totalQty += t.getQuantity();
            }

            if (totalQty == 0) continue;

            double avgPrice = totalCost / totalQty;
            Optional<Stock> stockOpt = DataStorage.stocks.stream()
                    .filter(s -> s.getName().equals(stockName))
                    .findFirst();

            if (!stockOpt.isPresent()) continue;

            double currentPrice = stockOpt.get().getPrice();
            double change = currentPrice - avgPrice;
            double percent = (change / avgPrice) * 100;

            rows.add(new ProfitLossRow(stockName, change, percent));
        }

        profitLossTable.setItems(rows);
    }

    public void refreshStockTable() {
        stockTable.refresh();
        refreshProfitLoss();
    }

    private void startStockPriceUpdate() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            DataStorage.stocks.forEach(Stock::fluctuatePrice);
            refreshStockTable();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // Inner class for Profit/Loss rows
    public static class ProfitLossRow {
        private final String stockName;
        private final double changeAmount;
        private final double changePercent;

        public ProfitLossRow(String stockName, double changeAmount, double changePercent) {
            this.stockName = stockName;
            this.changeAmount = changeAmount;
            this.changePercent = changePercent;
        }

        public String getStockName() {
            return stockName;
        }

        public double getChangeAmount() {
            return changeAmount;
        }

        public double getChangePercent() {
            return changePercent;
        }
    }
}