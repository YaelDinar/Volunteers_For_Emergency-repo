package Project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;


public class StartController {

    // This method is called when the "הודעה על אירוע חירום רפואי" button is pressed.
    @FXML
    private void NewEmergency(ActionEvent event) {
        // Add your action handling code here
        System.out.println("New Emergency button clicked");
        try {
            // Load the new FXML file for the emergency scene
            Parent emergencySceneRoot = FXMLLoader.load(getClass().getResource("/Project/NewEmegency.fxml"));
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
    
    // This method is called when the "שינוי מתנדב קיים" button is pressed.
    @FXML
    private void ChangeVolunteer(ActionEvent event) {
        // Add your action handling code here
        System.out.println("Change Volunteer button clicked");
        try {
            // Load the new FXML file for the new volunteer scene
            Parent volunteerSceneRoot = FXMLLoader.load(getClass().getResource("/Project/ChangeInfo.fxml")); // Ensure this path is correct
            Scene volunteerScene = new Scene(volunteerSceneRoot);

            // Get the current stage from the action event, assuming the event source is a Node
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            // Set the new scene on the current stage
            stage.setScene(volunteerScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Consider adding a user-friendly error message or dialog here
        }
    }

    // This method is called when the "הכנסת מתנדב" button is pressed.
    @FXML
    private void NewVolunteer(ActionEvent event) {
        System.out.println("New Volunteer button clicked");
        try {
            // Load the new FXML file for the new volunteer scene
            Parent volunteerSceneRoot = FXMLLoader.load(getClass().getResource("/Project/NewVolunteer.fxml")); // Ensure this path is correct
            Scene volunteerScene = new Scene(volunteerSceneRoot);

            // Get the current stage from the action event, assuming the event source is a Node
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            // Set the new scene on the current stage
            stage.setScene(volunteerScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Consider adding a user-friendly error message or dialog here
        }
    }

    // Optional: Add an initialize method if you need to perform any initialization.
    @FXML
    public void initialize() {
        // Initialization logic here
        System.out.println("OpenController initialized");
    }
}
