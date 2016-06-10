package dsme.myfinance.models;


import org.json.JSONObject;

public class User {
    String id;
    String displayName;
    String email;
    String phoneNumber;
    String sessionId;
    String lastName;
    String firstName;
    String userName;
    String password;

    public User(String id, String displayName,String userName, String firstName, String lastName, String email, String phoneNumber, String sessionId, String password){
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.sessionId = sessionId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getSessionIdTrimmed() {
        return sessionId.split("=")[0].split(";")[0];
    }


    public static class Adviser extends User{

        String description;

        public Adviser(String id, String displayName,String userName, String firstName, String lastName, String email, String phoneNumber, String sessionId, String password, String description){
            super(id, displayName, userName, firstName, lastName, email, phoneNumber, sessionId, password);

            this.description = description;
        }
    }
}
