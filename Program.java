package Project;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Program extends Application {
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Program.primaryStage = primaryStage; // Set the static primaryStage reference
        navigateTo("OpenScreen.fxml", "Start Screen");
    }

    // Utility method to navigate to different screens
    public static void navigateTo(String fxmlFile, String title) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(Program.class.getResource(fxmlFile));
            Parent root = loader.load();

            // Optionally, get the controller if you need to pass data to it
            // StartController controller = loader.getController();
            // controller.initData(data);

            // Set the scene
            Scene scene = new Scene(root);
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Consider showing an error message to the user
        }
    }

    // Method to get the primaryStage for scenarios where it might be needed outside this class
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
