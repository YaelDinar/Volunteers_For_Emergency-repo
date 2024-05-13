package Project.Model;

import java.io.Serializable;
import java.sql.SQLException;

public class emergency implements Serializable{
    private int ID;
    private int Priority;
    private int street;
    private int number;

    public emergency(int priority, String street, int number) throws SQLException {
        this.Priority = priority;
        this.number = number;
        this.street = DBfunctions.getStreetID(street);
        int i=DBfunctions.insertEmergency(this.street, priority, number);
        if(i!=-1){
            this.ID=i;
            return;
        }
        System.out.println("problem with emergency");
    }
    
    public emergency(String street) throws SQLException {
        this.Priority = 3;
    
        
        this.street = DBfunctions.getStreetID(street);
        int i=DBfunctions.insertEmergencyNoInfo(this.street, this.Priority);
        if(i!=-1){
            this.ID=i;
            return;
        }
        System.out.println("problem with emergency");
    }
            
    // Getter method for ID
    public int getID() {
        return this.ID;
    }

    // Getter method for Priority
    public int getPriority() {
        return this.Priority;
    }
    // Getter method for street
    public int getStreet() {
        return this.street;
    }

    // Getter method for number
    private int getNumber() {
        return this.number;
    }

}

