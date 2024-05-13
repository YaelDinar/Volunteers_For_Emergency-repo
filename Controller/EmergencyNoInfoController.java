package Project.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;

import Project.Model.DBfunctions;
import Project.Model.emergency;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;


public class EmergencyNoInfoController {

    @FXML
    private ComboBox<String> streetChoice; // This matches your FXML declaration

    // Array to hold street names
    private String[] streetNames = DBfunctions.fetchStreetsFromDB(); // Add your street names here

    // Method to initialize the ChoiceBox with street names
    @FXML
    public void initialize() {
        // Populate the ChoiceBox with street names
        ObservableList<String> streets = FXCollections.observableArrayList(streetNames);
        streetChoice.setItems(streets);
        streetChoice.setVisibleRowCount(5);
    }

    // Method to handle the button click event
    @FXML
    private void NewCallWithOnlyStreet(ActionEvent event) throws SQLException {
        try {
            // Here, you get the selected street from the ChoiceBox
            String streetName = streetChoice.getValue();
            System.out.println("Emergency reported at street: " + streetName);
            
            // Assuming emergency constructor and datastructure.getAreaFromStreet are properly defined
            emergency emergencyInstance = new emergency(streetName); // Adjust based on your actual constructor

            // Load the new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Project/clientframe.fxml")); // Adjust the path to your FXML
            Parent root = loader.load();

            // Assuming 'client' is the controller class for the new scene. 
            // It should have an 'initData' method expecting an Emergency instance
            client newSceneController = loader.getController(); // Ensure this matches the controller class for your client.fxml
            newSceneController.initData(emergencyInstance); // Pass the emergency object to the new scene

            // Switch to the new scene
            Scene scene = new Scene(root);
            Stage stage = (Stage) streetChoice.getScene().getWindow(); // Corrected variable name to match your ChoiceBox
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
