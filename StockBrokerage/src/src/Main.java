package src;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import src.view.LoginView;
import src.storage.DataStorage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        DataStorage.loadData(); // Load users and stocks
        
        LoginView loginView = new LoginView(primaryStage);
        Scene scene = new Scene(loginView.getView(), 400, 300);
        primaryStage.setTitle("Stox Mate");
        primaryStage.setScene(scene);
        primaryStage.show();
        Image icon = new Image("stockk.png");
		primaryStage.getIcons().add(icon);
    }

    @Override
    public void stop() {
        DataStorage.saveData(); // Save users and stocks
    }

    public static void main(String[] args) {
        launch(args);
    }
}