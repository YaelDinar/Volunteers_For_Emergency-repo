package Project.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import Project.Model.ClientRequest;
import Project.Model.Volunteer;
import Project.Model.emergency;

public class client {
    private emergency emergency; // Now an instance variable

    @FXML
    private Label responseLabel;

    // Call this method to initialize the data for the current controller
    public void initData(emergency emergency) {
        this.emergency = emergency;
        sendEmergencyToServer();
    }

    private void sendEmergencyToServer() {
        final String SERVER_ADDRESS = "localhost"; // Server address
        final int SERVER_PORT = 8080; // Server port
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
    
            // Create a ClientRequest for the emergency and send it
            ClientRequest request = new ClientRequest("emergency", this.emergency);
            oos.writeObject(request);
            oos.flush(); // Flush the output stream to ensure all data is sent
            System.out.println("Emergency sent to the server as a ClientRequest.");
    
            // Receive the list of volunteers
            List<Volunteer> volunteers = (List<Volunteer>) ois.readObject();
            if (volunteers != null && !volunteers.isEmpty()) {
                updateUIWithVolunteers(volunteers);
            } else {
                updateUI("No volunteers selected for the emergency.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            updateUI("Failed to communicate with the server.");
        }
    }
    
    // Helper method to update the UI
    private void updateUI(String message) {
        Platform.runLater(() -> responseLabel.setText(message));
    }

    // Method to update UI with a list of volunteers
    private void updateUIWithVolunteers(List<Volunteer> volunteers) {
        StringBuilder sb = new StringBuilder("המתנדבים שבדרך:\n");
        for (Volunteer volunteer : volunteers) {
            sb.append(volunteer.toString()).append("\n");
        }
        updateUI(sb.toString());
    }

    @FXML
    public void initialize() {
        // Initialize method if needed. Don't fetch Emergency here.
        // Wait for initData to be called with the correct Emergency object.
    }
}
