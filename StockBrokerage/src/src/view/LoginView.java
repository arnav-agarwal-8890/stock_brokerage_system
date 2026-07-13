package src.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.controller.LoginController;

public class LoginView {
    private VBox layout;
    private Stage primaryStage;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;

    public LoginView(Stage stage) {
        this.primaryStage = stage;
        layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Stock Brokerage Login");

        usernameField = new TextField();
        usernameField.setPromptText("Username");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        messageLabel = new Label();

        layout.getChildren().addAll(titleLabel, usernameField, passwordField, loginButton, registerButton, messageLabel);

        LoginController controller = new LoginController(this);

        loginButton.setOnAction(e -> controller.login());
        registerButton.setOnAction(e -> controller.register());
    }

    public VBox getView() {
        return layout;
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}