package Project.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import Project.Model.ClientRequest;
import Project.Model.DBfunctions;
import Project.Model.Functions;
import Project.Model.Volunteer;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChangeInfoClient {
    @FXML private TextField ID;
    @FXML private TextField Name;
    @FXML private ComboBox<String> streetChoice;
    @FXML private TextField PhoneNumber;
    @FXML private ComboBox<Integer> Skill; // Corrected to ComboBox
    @FXML private ComboBox<Integer> Availability; // Corrected to ComboBox

    private final String SERVER_ADDRESS = "localhost";
    private final int SERVER_PORT = 8080;

    @FXML
    public void initialize() {
        try {
            String[] streetNames = DBfunctions.fetchStreetsFromDB();
            ObservableList<String> streets = FXCollections.observableArrayList(streetNames);
            streetChoice.setItems(streets);
            streetChoice.setVisibleRowCount(5);

            ObservableList<Integer> skillLevels = FXCollections.observableArrayList(1, 2, 3);
            Skill.setItems(skillLevels);

            ObservableList<Integer> availabilityOptions = FXCollections.observableArrayList(0, 1); // 0 for Unavailable, 1 for Available
            Availability.setItems(availabilityOptions);
        } catch (Exception e) {
            showAlert("Error", "Failed to fetch data.");
            e.printStackTrace();
        }
    }

    @FXML
    private void ChangeInfo(ActionEvent event) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            Volunteer volunteer = createVolunteerFromFields();
            if (volunteer == null) return; // Check for null to avoid sending invalid data

            ClientRequest request = new ClientRequest("updateVolunteer", volunteer);
            oos.writeObject(request);
            oos.flush();

            showAlert("Success", "Volunteer information updated successfully.");
        } catch (Exception e) {
            showAlert("Error", "Failed to update volunteer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Volunteer createVolunteerFromFields() {
        Volunteer vl = DBfunctions.getVolunteerFromDB(ID.getText());
        if (vl == null) {
            showAlert("Problem", "Volunteer not found.");
            return null;
        }

        if (!Name.getText().equals("enter name:")) vl.setName(Name.getText());
        if (streetChoice.getValue() != null) vl.setStreet(streetChoice.getValue());
        if (!PhoneNumber.getText().equals("enter number")&& Functions.checkPhone(PhoneNumber.getText())) vl.setPhoneNumber(PhoneNumber.getText());
        if (Skill.getValue() != null) vl.setSkill(Skill.getValue());
        if (Availability.getValue() != null) vl.setAvailability(Availability.getValue());

        return vl;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
