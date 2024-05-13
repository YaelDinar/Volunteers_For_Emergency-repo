package Project.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBfunctions {
     @SuppressWarnings("unused")
    public static int getAreaOfStreet(int street) {
        int area=0;
        try (Connection connection = getConnection()) {
            String sql = "SELECT AREAID FROM Streets WHERE Streetid = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, street);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        area = resultSet.getInt("AreaID");
                    } else {
                        System.out.println("Street not found: " + street);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return area;
    }
// Method to establish a connection to your database (replace with your own connection method)
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/Project";
        String username = "project";
        String password = "1970";
        return DriverManager.getConnection(url, username, password);
    }

    public static void insertVolunteerToEmergency(List<Volunteer> list, emergency emergency) {
        // It's more efficient to open the connection outside of the loop to avoid opening and closing it multiple times
        try (Connection connection = getConnection()) {
            // Prepared SQL statement should be corrected for syntax and structure
            String sql = "INSERT INTO volunteerinemergencies (EmergencyID, VolunteerID) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // Set the emergency ID once as it does not change per iteration
                statement.setInt(1, emergency.getID());
    
                // Loop through each volunteer in the list
                for (Volunteer volunteer : list) {
                    statement.setString(2, volunteer.getID());
                    statement.executeUpdate();  // Execute the update for each volunteer
                    setAvailable(volunteer,0);
                }
            }
           
        } catch (SQLException e) {
            e.printStackTrace();  // Properly handle exceptions by printing stack trace
        }
    }

    public static int getStreetID (String street){
        try (Connection connection = DBfunctions.getConnection()) {
            String sql = "SELECT StreetID FROM streets WHERE streetName = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, street);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("StreetID");
                    } else {
                        System.out.println("Street not found: " + street+ " enter again");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return -1;  
    }

    public static void addVolunteer(Volunteer volunteer){
        try (Connection connection = DBfunctions.getConnection()) {
            String sql = "SELECT 1 FROM Volunteers WHERE VolunteerID = ? ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, volunteer.getID());
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        //System.out.println("user already in database");
                        return;
                    } 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Connection connection = DBfunctions.getConnection()) {
            String sql = "INSERT INTO Volunteers (VolunteerID, VolunteerName, PhoneNumber, StreetID, Skill, Availability) VALUES (?, ?, ?, ?, ?, ?);";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, volunteer.getID());
                statement.setString(2, volunteer.getName());
                statement.setString(3, volunteer.getPhoneNumber());
                statement.setInt(4, volunteer.getStreet());
                statement.setInt(5, volunteer.getSkill());
                statement.setInt(6, volunteer.getAvailable());
                statement.executeUpdate();
                System.out.println(volunteer.getName()+" was added to db");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e);
            System.out.println("can't add "+volunteer.getName()+" to db");
    
        }}
        public static void setVolunteerName(String newName, Volunteer vl) {
            try (Connection connection = DBfunctions.getConnection()) {
                String sql = "UPDATE Volunteers SET VolunteerName = ? WHERE VolunteerID = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, newName);
                    statement.setString(2, vl.getID());
                    int rowsUpdated = statement.executeUpdate();
                    vl.setName(newName);
                    if (rowsUpdated < 0) {
                    
                        System.out.println("Failed to update Name for VolunteerID: " + vl.getID());
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        // Method to update StreetID for the Volunteer
        public static void updateVolunteerStreetID(String street, Volunteer vl) {
            try (Connection connection = DBfunctions.getConnection()) {
                String sql = "UPDATE Volunteers SET StreetID = ? WHERE VolunteerID = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, DBfunctions.getStreetID(street));
                    statement.setString(2, vl.getID());
                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        vl.setStreet(street);; // Update the local attribute if successful
                    } else {
                        System.out.println("Failed to update Street for VolunteerID: " + vl.getID());
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        // Setter method for PhoneNumber
        public static void setPhoneNumber(String newPhoneNumber ,Volunteer vl) {
            try (Connection connection = DBfunctions.getConnection()) {
                String sql = "UPDATE Volunteers SET PhoneNumber = ? WHERE VolunteerID = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, newPhoneNumber);
                    statement.setString(2, vl.getID());
                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        vl.setPhoneNumber(newPhoneNumber); // Update the local attribute if successful
                    } else {
                        System.out.println("Failed to update PhoneNumber for VolunteerID: " + vl.getID());
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        // Setter method for Available
        public static void setAvailable(Volunteer volunteer,int newAvailable) {
            try (Connection connection = DBfunctions.getConnection()) {
                String sql = "UPDATE Volunteers SET Availability = ? WHERE VolunteerID = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, newAvailable);
                    statement.setString(2, volunteer.getID());
                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        volunteer.setAvailability(newAvailable); // Update the local attribute if successful
                    } else {
                        System.out.println("Failed to update Availability for VolunteerID: " + volunteer.getID());
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        // Setter method for Skill
        public static void setSkill(int newSkill, Volunteer vl) {
            try (Connection connection = DBfunctions.getConnection()) {
                String sql = "UPDATE Volunteers SET Skill = ? WHERE VolunteerID = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, newSkill);
                    statement.setString(2, vl.getID());
                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        vl.setSkill(newSkill); // Update the local attribute if successful
                    } else {
                        System.out.println("Failed to update Skill for VolunteerID: " + vl.getID());
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Method to get the area from street name
    public static int getAreaFromStreet(String street) {
        int area=0;
        try (Connection connection = getConnection()) {
            String sql = "SELECT AREAID FROM Streets WHERE StreetName = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, street);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        area = resultSet.getInt("AreaID");
                    } else {
                        System.out.println("Street not found: " + street);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return area;
    }

    public static String getStreetName(int street){
        try (Connection connection = getConnection()) {
            String sql = "SELECT StreetName from Streets where StreetID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, street);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("StreetName");
                    } else {
                        System.out.println("Street Not Found");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Volunteer getVolunteerFromDB(String ID){
        String name, street, phone;
        int availability, skill;
        try (Connection connection = getConnection()) {
            String sql = "SELECT VolunteerName, PhoneNumber, StreetID, Skill, Availability FROM Volunteers WHERE VolunteerID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, ID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        name = resultSet.getString("VolunteerName");
                        phone = resultSet.getString("PhoneNumber");
                        int streetID = resultSet.getInt("StreetID");
                        street = DBfunctions.getStreetName(streetID);
                        // Handle the case where DBfunctions.getStreetName returns null
                        if (street == null) {
                            System.out.println("Street not found for ID: " + streetID);
                            return null;
                        }
                        skill = resultSet.getInt("Skill");
                        availability = resultSet.getInt("Availability");
                        return new Volunteer(ID, name, street, phone, skill, availability);
                    } else {
                        System.out.println("Volunteer Not Found");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    

    public static void removeVolunteerFromDB(String ID) {
        try (Connection connection = getConnection()) {
            String sql = "DELETE FROM Volunteers WHERE VolunteerID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, ID);
                // Use executeUpdate() for DELETE statements
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Volunteer with ID " + ID + " has been removed from the database.");
                } else {
                    System.out.println("No volunteer found with ID " + ID + ".");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void ModifyVolunteer(Volunteer updatedVolunteer, Volunteer vl) {
        if(!updatedVolunteer.getName().equals(vl.getName()))DBfunctions.setVolunteerName(updatedVolunteer.getName(), vl);
        if(!updatedVolunteer.getStreetName().equals(vl.getStreetName()))DBfunctions.updateVolunteerStreetID(updatedVolunteer.getStreetName(),vl);
        if(!updatedVolunteer.getPhoneNumber().equals(vl.getPhoneNumber()))DBfunctions.setPhoneNumber(updatedVolunteer.getPhoneNumber(),vl);
        if(updatedVolunteer.getSkill()!=(vl.getSkill()))DBfunctions.setSkill(updatedVolunteer.getSkill(),vl);
        if(updatedVolunteer.getAvailable()!=(vl.getAvailable()))DBfunctions.setAvailable(vl,updatedVolunteer.getAvailable());


        
    }

    public static String[] fetchStreetsFromDB() {
        ArrayList<String> streetList = new ArrayList<>();
        try (Connection con = DBfunctions.getConnection()){
            PreparedStatement pst = con.prepareStatement("SELECT StreetName FROM streets ORDER BY StreetName ASC");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                streetList.add(rs.getString("StreetName"));
            }

            // Locale-specific sorting (especially useful for languages like Hebrew)
        } catch (Exception e) {
            e.printStackTrace();
        }

        return streetList.toArray(new String[0]);
    }

    public static int insertEmergency(int street,int Priority,int number){
         // Insert the new emergency
         try (Connection connection = getConnection()) {
            String sql = "INSERT INTO emergencies (StreetID, Priority, housenumber) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, street);
                statement.setInt(2, Priority);
                statement.setInt(3, number);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("New emergency added to database");
    
                    // Retrieve the last inserted ID
                    sql = "SELECT MAX(ID) FROM emergencies";
                    try (PreparedStatement idStatement = connection.prepareStatement(sql)) {
                        try (ResultSet resultSet = idStatement.executeQuery()) {
                            if (resultSet.next()) {
                              return resultSet.getInt(1);
                            } else {
                                System.out.println("ID not found");
                            }
                        }
                    }
                } else {
                    System.out.println("Failed to add new emergency to database");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add new emergency to database");
        }
        return -1;
    }


    public static int insertEmergencyNoInfo(int street,int Priority){
        // Insert the new emergency
        try (Connection connection = getConnection()) {
           String sql = "INSERT INTO emergencies (StreetID, Priority) VALUES (?, ?)";
           try (PreparedStatement statement = connection.prepareStatement(sql)) {
               statement.setInt(1, street);
               statement.setInt(2, Priority);
               int rowsInserted = statement.executeUpdate();
               if (rowsInserted > 0) {
                   System.out.println("New emergency added to database");
   
                   // Retrieve the last inserted ID
                   sql = "SELECT MAX(ID) FROM emergencies";
                   try (PreparedStatement idStatement = connection.prepareStatement(sql)) {
                       try (ResultSet resultSet = idStatement.executeQuery()) {
                           if (resultSet.next()) {
                             return resultSet.getInt(1);
                           } else {
                               System.out.println("ID not found");
                           }
                       }
                   }
               } else {
                   System.out.println("Failed to add new emergency to database");
               }
           }
       } catch (SQLException e) {
           e.printStackTrace();
           System.out.println("Failed to add new emergency to database");
       }
       return -1;
   }

   public static boolean isVolunteerInDB(String ID){
    try (Connection connection = getConnection()) {
            String sql = "SELECT 1 from Volunteers where VolunteerID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, ID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
   }

   public static List<Volunteer> getAllAvailableVolunteers() {
    List<Volunteer> volunteers = new ArrayList<>();

    try (Connection connection = getConnection()) {
        String sql = "SELECT VolunteerID, VolunteerName, streets.streetname, PhoneNumber, Skill, Availability FROM Volunteers, Streets WHERE streets.streetid = volunteers.streetid AND volunteers.Availability = 1";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String ID = resultSet.getString("VolunteerID");
                    String name = resultSet.getString("VolunteerName");
                    String street = resultSet.getString("streets.streetname");
                    String phoneNumber = resultSet.getString("PhoneNumber");
                    int skill = resultSet.getInt("Skill");
                    int availability = resultSet.getInt("Availability");

                    Volunteer volunteer = new Volunteer(ID, name, street, phoneNumber, skill, availability);
                    volunteers.add(volunteer);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Convert the list to an array and return it
    return volunteers;
}    

    public static int fetchStreetCount()
     {
        try(Connection connection = getConnection()){
            String sql = "SELECT count(*) AS count FROM streets";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt("count") : 0;
    }}
        catch (SQLException e) {
            e.printStackTrace();
        }return -1;
    }


    public static Map<Integer, Integer> fetchAreaMap() {
        Map<Integer, Integer> areaMap = new HashMap<>();
        String sql = "SELECT streetid, areaid FROM streets";
        try(Connection connection = getConnection()){
            try (PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        areaMap.put(resultSet.getInt("streetid"), resultSet.getInt("areaid"));
                }
            }}
            
        catch (SQLException e) {
            e.printStackTrace();
        }
    
        return areaMap;
    }
}