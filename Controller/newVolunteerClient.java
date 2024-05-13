package Project.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import Project.Model.Volunteer;
import Project.Model.ClientRequest;
import Project.Model.DBfunctions;
import javafx.scene.control.ComboBox;

public class newVolunteerClient {
    @FXML private TextField ID;
    @FXML private TextField Name;
    @FXML private TextField PhoneNumber;
    @FXML private ComboBox<Integer> Skill;
    @FXML private ComboBox<Integer> Availability;
    @FXML private ComboBox<String> streetChoice; // This matches your FXML declaration

    private final String SERVER_ADDRESS = "localhost";
    private final int SERVER_PORT = 8080;

    // Method to initialize the ComboBoxes with street names, skills, and availability statuses
    @FXML
    public void initialize() {
        try {
            String[] streetNames = DBfunctions.fetchStreetsFromDB();
            ObservableList<String> streets = FXCollections.observableArrayList(streetNames);
            streetChoice.setItems(streets);
            streetChoice.setVisibleRowCount(5);

            ObservableList<Integer> skillLevels = FXCollections.observableArrayList(1, 2, 3);
            Skill.setItems(skillLevels);

            ObservableList<Integer> availabilityStatuses = FXCollections.observableArrayList(0, 1);
            Availability.setItems(availabilityStatuses);
        } catch (Exception e) {
            showAlert("Error", "An error occurred while fetching data.");
            e.printStackTrace();
        }
    }

    // Handler for the Send button
    @FXML
    void sendVolunteerData(ActionEvent event) {
        if (!validateInputs()) {
            showAlert("Input Error", "Please fill all fields correctly.");
            return;
        }

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            Volunteer volunteer = new Volunteer(
                ID.getText(),
                Name.getText(),
                streetChoice.getValue(),
                PhoneNumber.getText(),
                Skill.getValue(),
                Availability.getValue()
            );

            ClientRequest request = new ClientRequest("addVolunteer", volunteer);
            oos.writeObject(request);
            oos.flush();

            // Optionally read response from the server
            showAlert("Success", "The volunteer has been successfully added.");

        } catch (IOException e) {
            showAlert("Connection Error", "Failed to connect to the server.");
            e.printStackTrace();
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred.");
            e.printStackTrace();
        }
    }

    // Utility method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Validate input fields before sending data
    private boolean validateInputs() {
        return !ID.getText().isEmpty() &&
               !Name.getText().isEmpty() &&
               !PhoneNumber.getText().isEmpty() &&
               Skill.getValue() != null &&
               Availability.getValue() != null &&
               streetChoice.getValue() != null;
    }
}
