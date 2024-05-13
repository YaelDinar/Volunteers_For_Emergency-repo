package Project.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class datastructure {
    private Map<String, List<Volunteer>> areaVolunteers;

    public datastructure() {
        areaVolunteers = new HashMap<>();
        // Initialize hash maps for each area
        areaVolunteers.put("1", new ArrayList<>());
        areaVolunteers.put("2", new ArrayList<>());
        areaVolunteers.put("3", new ArrayList<>());
        areaVolunteers.put("4", new ArrayList<>());
        areaVolunteers.put("5", new ArrayList<>());
        this.populateFromDatabase();
    }

    // Method to add a volunteer to the appropriate area hash map
    public void addVolunteerToArea(Volunteer volunteer) {
        int area = DBfunctions.getAreaFromStreet(volunteer.getStreetName());
        // Ensure the provided area exists in the map
        if (areaVolunteers.containsKey(String.valueOf(area))) {
            // Get the list of volunteers for the specified area
            List<Volunteer> volunteers = areaVolunteers.get(String.valueOf(area));

            // Add the volunteer to the list
            volunteers.add(volunteer);
            System.out.println("Volunteer added: " + volunteer.getName() + " in area " + DBfunctions.getAreaFromStreet(volunteer.getStreetName()));

        } else {
            System.out.println("Area '" + area + "' does not exist.");
        }
    }

    // Method to retrieve volunteers from a specific area
    public List<Volunteer> getVolunteersInArea(String area) {
        // Ensure the provided area exists in the map
        if (areaVolunteers.containsKey(area)) {
            // Return the list of volunteers for the specified area
            return areaVolunteers.get(area);
        } else {
            System.out.println("Area '" + area + "' does not exist.");
            return null;
        }
    }

    // Method to display all volunteers in each area
    public void displayAllVolunteers() {
        for (Map.Entry<String, List<Volunteer>> entry : areaVolunteers.entrySet()) {
            String area = entry.getKey();
            List<Volunteer> volunteers = entry.getValue();
            System.out.println("Area: " + area);
            System.out.println("Volunteers:");
            for (Volunteer volunteer : volunteers) {
                System.out.println("- " + volunteer.getName());
            }
            System.out.println();
        }
    }

    // Method to retrieve all volunteers from the database and populate the VolunteerManager
    public void populateFromDatabase() {
        // Fetch the list of all available volunteers from the database
        List<Volunteer> list = DBfunctions.getAllAvailableVolunteers();
        
        // Iterate through the list and add each volunteer to the correct area
        for (Volunteer vl : list) {
            addVolunteerToArea(vl);
        }
    }
    
    public Map<String, List<Volunteer>> getdatastructure() {
        return this.areaVolunteers;
    }

// Method to remove a volunteer from the structure by their specific volunteer object
public void removefromstructure(Volunteer vl) {
    // Determine the area of the volunteer using their street name
    int AreaOfVolunteer = DBfunctions.getAreaFromStreet(vl.getStreetName());

    // Convert the area to a string key, as used in the map
    String areaKey = String.valueOf(AreaOfVolunteer);

    // Retrieve the list of volunteers for this area
    List<Volunteer> volunteersInArea = areaVolunteers.get(areaKey);

    // Check if the list exists and contains the volunteer
    if (volunteersInArea != null && volunteersInArea.contains(vl)) {
        // Remove the volunteer from the list
        volunteersInArea.remove(vl);
        System.out.println("Volunteer removed: " + vl.getName() + " from area " + AreaOfVolunteer);
    } else {
        // Log if the volunteer was not found in the expected area
        System.out.println("Volunteer not found in area " + AreaOfVolunteer + " or area list does not exist.");
    }
}
public boolean isVolunteerInStructure(Volunteer volunteer) {
    // Determine the area of the volunteer using their street name
    int areaOfVolunteer = DBfunctions.getAreaFromStreet(volunteer.getStreetName());

    // Convert the area to a string key, as used in the map
    String areaKey = String.valueOf(areaOfVolunteer);

    // Retrieve the list of volunteers for this area
    List<Volunteer> volunteersInArea = areaVolunteers.get(areaKey);

    // Check if the list exists and contains the volunteer
    if (volunteersInArea != null && volunteersInArea.contains(volunteer)) {
        return true; // Volunteer is in the structure
    } else {
        return false; // Volunteer is not in the structure or the area list does not exist
    }
}


}


