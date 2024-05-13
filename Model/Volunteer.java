package Project.Model;
import java.io.Serializable;

public class Volunteer implements Serializable {
    private static final long serialVersionUID = 1L;
    private String ID;
    private String Name;
    private String PhoneNumber;
    private int street;
    private String streetName;
    private int skill;
    private int available;

   public Volunteer(String ID, String Name, String street, String phoneNumber, int skill, int available) {
    this.ID = ID;
    this.Name = Name;
    this.PhoneNumber = phoneNumber;
    this.skill = skill;
    this.available = available;
    this.streetName=street;
    this.street = DBfunctions.getStreetID(street);
    }


    public String getID() {
        return this.ID;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPhoneNumber() {
        return this.PhoneNumber;
    }

    public void setPhoneNumber(String pn) {
        this.PhoneNumber = pn;
    }

    public int getStreet() {
        return this.street;
    }
    public String getStreetName(){
        return this.streetName;
    }

    public void setStreet(String street) {
        this.streetName = street;
        this.street = DBfunctions.getStreetID(street);
    }

    public int getSkill() {
        return this.skill;
    }

    public void setSkill( int skill) {
        this.skill = skill;
    }

    public int getAvailable() {
        return this.available;
    }


    // Setter methods (if needed)
    public void setID(String ID) {
        this.ID = ID;
    }

    public void setAvailability(int i) {
        this.available = i;
    }

    
    public String toString(){
        String s;
        s=this.Name + " יגיע/תגיעה מרחוב: " + this.streetName;
        return s;
    }
    
}

 

