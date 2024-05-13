package Project.Controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Project.Model.*;

public class server {
    private static final int NUMBER_OF_AREAS = 5;
    private static final PriorityQueue emergencies = new PriorityQueue();
    private static final datastructure ds = new datastructure(); // Ensure it's correctly initialized
    private static final Graph graph = new Graph();
    private static final Object dbLock = new Object(); // Lock for synchronizing DB operations

    public static void main(String[] args) {
        final int PORT = 8080;
        ExecutorService executor = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                executor.execute(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream())) {

            ClientRequest request = (ClientRequest) ois.readObject();
            switch (request.getType()) {
                case "emergency":
                    handleEmergency(request.getData(), oos);
                    break;
                case "addVolunteer":
                    addVolunteer(request.getData(), oos);
                    break;
                case "updateVolunteer":
                    updateVolunteer(request.getData(), oos);
                    break;
                default:
                    oos.writeObject("Unknown request type");
                    break;
            }
            oos.flush();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleEmergency(Object data, ObjectOutputStream oos) throws IOException {
        emergency newEmergency = (emergency) data;
        synchronized (dbLock) {
            emergencies.enqueue(newEmergency);
            System.out.println("Emergency received and added to the queue");
            List<Volunteer> selectedVolunteers = processEmergencyAndGetVolunteers(newEmergency);
            oos.writeObject(selectedVolunteers);
            System.out.println("Selected volunteers sent back to client.");
        }
    }

    private static void addVolunteer(Object data, ObjectOutputStream oos) throws IOException {
        Volunteer newVolunteer = (Volunteer) data;
        String response;
        synchronized (dbLock) {
            try {
                DBfunctions.addVolunteer(newVolunteer);
                ds.addVolunteerToArea(newVolunteer);
                response = "Volunteer added successfully";
            } catch (Exception e) {
                response = "Failed to add volunteer: " + e.getMessage();
            }
        }
        oos.writeObject(response);
    }

    private static void updateVolunteer(Object data, ObjectOutputStream oos) throws IOException {
        Volunteer updatedVolunteer = (Volunteer) data;
        String response;
        synchronized (dbLock) {
            try {
                Volunteer vl = DBfunctions.getVolunteerFromDB(updatedVolunteer.getID());
                DBfunctions.ModifyVolunteer(updatedVolunteer, vl);
                if (ds.isVolunteerInStructure(vl)) {
                    ds.removefromstructure(vl);
                    if (updatedVolunteer.getAvailable() == 1) {
                        ds.addVolunteerToArea(updatedVolunteer);
                    }
                }
                response = "Volunteer updated successfully";
            } catch (Exception e) {
                response = "Failed to update volunteer: " + e.getMessage();
            }
        }
        oos.writeObject(response);
    }

    private static List<Volunteer> processEmergencyAndGetVolunteers(emergency currentEmergency) {
        synchronized (dbLock) {
            List<Volunteer> selectedVolunteers = GeneticAlgorithm.findVolunteer(ds.getdatastructure(), currentEmergency, graph);
            if (!selectedVolunteers.isEmpty()) {
                System.out.println("Selected volunteers for the emergency:");
                for (Volunteer volunteer : selectedVolunteers) {
                    System.out.println(volunteer.getName());
                    ds.removefromstructure(volunteer);

                }
                DBfunctions.insertVolunteerToEmergency(selectedVolunteers, currentEmergency);
            } else {
                System.out.println("No volunteers selected for the emergency.");
            }
            return selectedVolunteers;
        }
    }

 
}
