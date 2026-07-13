package src.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import src.model.User;
import src.storage.DataStorage;
import src.view.DashboardView;
import src.view.LoginView;

public class LoginController {
    private LoginView view;

    public LoginController(LoginView view) {
        this.view = view;
    }

    public void login() {
        String username = view.getUsername();
        String password = view.getPassword();

        for (User user : DataStorage.users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                view.setMessage("Login successful!");
                openDashboard(user);
                return;
            }
        }
        view.setMessage("Invalid credentials.");
    }

    public void register() {
        String username = view.getUsername();
        String password = view.getPassword();

        for (User user : DataStorage.users) {
            if (user.getUsername().equals(username)) {
                view.setMessage("Username already exists!");
                return;
            }
        }

        User newUser = new User(username, password);
        DataStorage.users.add(newUser);
        view.setMessage("Registration successful!");
    }

    private void openDashboard(User user) {
        DashboardView dashboardView = new DashboardView(view.getPrimaryStage(), user);
        Scene scene = new Scene(dashboardView.getView(), 600, 400);
        view.getPrimaryStage().setTitle("Welcome, " + user.getUsername());
        view.getPrimaryStage().setScene(scene);
    }
}