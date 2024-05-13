package Project.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.sql.SQLException;

import Project.Model.DBfunctions;
import Project.Model.Functions;
import Project.Model.emergency;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

public class EmergencyInfoController {

    @FXML private Label open;
    @FXML private TextField HouseNumber;
    @FXML private Button button;
    @FXML private ComboBox<Integer> HurtPeople;
    @FXML private ComboBox<String> streetChoice; // This matches your FXML declaration
    @FXML private ComboBox<Integer> HowBad; // Changed to ComboBox for consistency

    private String[] streetNames; // Initialized in initialize()

    @FXML
    private void NewCall(ActionEvent event) {
        try {
            if (streetChoice.getValue() == null || HouseNumber.getText().isEmpty() || HurtPeople.getValue() == null || HowBad.getValue() == null) {
                System.out.println("Please fill all fields correctly!");
                return; // Exit the method if any field is not properly filled
            }
            
            // Create an emergency object with validated input
            emergency emergencyInstance = new emergency(
                Functions.CaclulatePriority(HurtPeople.getValue(), HowBad.getValue()),
                streetChoice.getValue(),
                Integer.parseInt(HouseNumber.getText())
            );

            // Load new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Project/clientframe.fxml")); // Ensure path is correct
            Parent root = loader.load();

            client newSceneController = loader.getController();
            newSceneController.initData(emergencyInstance);

            Scene scene = new Scene(root);
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (NumberFormatException e) {
            System.out.println("Please ensure all number fields are filled correctly.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred while loading the scene.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("A database error occurred.");
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() throws SQLException {
        streetNames = DBfunctions.fetchStreetsFromDB();
        ObservableList<String> streets = FXCollections.observableArrayList(streetNames);
        streetChoice.setItems(streets);
        streetChoice.setVisibleRowCount(5);

        ObservableList<Integer> hurtNumbers = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        HurtPeople.setItems(hurtNumbers);
        HurtPeople.setVisibleRowCount(5);

        ObservableList<Integer> severityLevels = FXCollections.observableArrayList(1, 2, 3);
        HowBad.setItems(severityLevels);
        HowBad.setVisibleRowCount(5);
    }
}
