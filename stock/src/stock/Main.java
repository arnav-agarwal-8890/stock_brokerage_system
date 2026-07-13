package stock;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import service.StockBrokerageService;

public class Main extends Application {
    private StockBrokerageService brokerageService;

    @Override
    public void start(Stage primaryStage) {
        brokerageService = new StockBrokerageService();
        
        // Create the login form
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label usernameLabel = new Label("Username:");
        grid.add(usernameLabel, 0, 0);

        TextField usernameField = new TextField();
        grid.add(usernameField, 1, 0);

        Label balanceLabel = new Label("Initial Balance:");
        grid.add(balanceLabel, 0, 1);

        TextField balanceField = new TextField();
        grid.add(balanceField, 1, 1);

        Button loginButton = new Button("Login/Register");
        grid.add(loginButton, 1, 2);

        // Handle login action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            try {
                double balance = Double.parseDouble(balanceField.getText());
                brokerageService.registerUser(username, balance);
                showTradingView(username);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Balance");
                alert.setContentText("Please enter a valid number for initial balance.");
                alert.showAndWait();
            }
        });

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setTitle("Stock Brokerage Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showTradingView(String username) {
        Stage tradingStage = new Stage();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label welcomeLabel = new Label("Welcome, " + username + "!");
        grid.add(welcomeLabel, 0, 0, 2, 1);

        // Display available stocks
        TextArea stocksArea = new TextArea();
        stocksArea.setEditable(false);
        stocksArea.setPrefRowCount(5);
        
        StringBuilder stocksText = new StringBuilder("Available Stocks:\n");
        brokerageService.getStocks().values().forEach(stock -> 
            stocksText.append(String.format("%s (%s): $%.2f - Quantity: %d\n", 
                stock.getName(), stock.getSymbol(), stock.getPrice(), stock.getQuantity())));
        stocksArea.setText(stocksText.toString());
        
        grid.add(stocksArea, 0, 1, 2, 1);

        Scene scene = new Scene(grid, 400, 300);
        tradingStage.setTitle("Trading View");
        tradingStage.setScene(scene);
        tradingStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}