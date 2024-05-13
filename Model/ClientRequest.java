package Project.Model;

import java.io.Serializable;

public class ClientRequest implements Serializable {
    private static final long serialVersionUID = 1L;                  

    private String type; // "emergency", "addVolunteer", "updateVolunteer"
    private Object data; 
    // Constructor to initialize request type and data
    public ClientRequest(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    // Getter for the type of the request
    public String getType() {
        return type;
    }

    // Setter for the type of the request
    public void setType(String type) {
        this.type = type;
    }

    // Getter for the data of the request
    public Object getData() {
        return data;
    }

    // Setter for the data of the request
    public void setData(Object data) {
        this.data = data;
    }
}
