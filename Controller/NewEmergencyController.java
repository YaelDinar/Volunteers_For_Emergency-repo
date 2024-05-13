package Project.Controller; // Note: Consider correcting the package name to Project.Controller

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NewEmergencyController {

    @FXML
    protected void SendNoInfo(ActionEvent event) {
        // Implement the logic that should be executed when the "אין זמן שליחת רחוב בלבד" button is clicked
        System.out.println("Sending street only...");
        try {
            // Load the new FXML file for the emergency scene
            Parent emergencySceneRoot = FXMLLoader.load(getClass().getResource("/Project/InputEmergencyNoInfo.fxml"));
            Scene emergencyScene = new Scene(emergencySceneRoot);
            
            // Get the current stage from the action event, assuming the event source is a Node
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            
            // Set the new scene on the current stage
            stage.setScene(emergencyScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception, perhaps show an error dialog
        }
    }

    @FXML
    protected void SendWithInfo(ActionEvent event) {
        // Implement the logic that should be executed when the "יש לי זמן למלא מספר פרטים" button is clicked
        System.out.println("Preparing to send detailed information...");
         try {
            // Load the new FXML file for the emergency scene
            Parent emergencySceneRoot = FXMLLoader.load(getClass().getResource("/Project/InputEmergency.fxml"));
            Scene emergencyScene = new Scene(emergencySceneRoot);
            
            // Get the current stage from the action event, assuming the event source is a Node
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            
            // Set the new scene on the current stage
            stage.setScene(emergencyScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception, perhaps show an error dialog
        }
    }

    // Optional: Include an initialization method if needed
    @FXML
    private void initialize() {
        // Initialization logic here (if needed)
        System.out.println("NewEmergencyController initialized");
    }
}
