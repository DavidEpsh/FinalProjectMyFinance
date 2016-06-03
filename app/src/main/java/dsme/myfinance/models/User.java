package dsme.myfinance.models;


import org.json.JSONObject;

public class User {
    String id;
    String displayName;
    String email;
    String phoneNumber;
    String sessionId;

    public User(String id, String displayName, String email, String phoneNumber, String sessionId){
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.sessionId = sessionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFirstName(){
        return displayName.split(" ")[0];
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
//    String provider;
//    String userName;
//    String phoneNumber;
//    String role;

    public class Adviser extends User{

        public Adviser(String id, String displayName, String email, String phoneNumber, String sessionId) {
            super(id, displayName, email, phoneNumber, sessionId);
        }
    }

    public class Customer extends User{

        public Customer(String id, String displayName, String email, String phoneNumber, String sessionId) {
            super(id, displayName, email, phoneNumber, sessionId);
        }
    }

}
