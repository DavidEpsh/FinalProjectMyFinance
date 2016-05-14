package dsme.myfinance.models;


import org.json.JSONObject;

public class User {
    String id;
    String displayName;

    public User(String id, String displayName){
        this.id = id;
        this.displayName = displayName;
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

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
//    String provider;
//    String userName;
//    String phoneNumber;
//    String role;

    public class Adviser extends User{

        public Adviser(String id, String displayName) {
            super(id, displayName);
        }
    }

    public class Customer extends User{


        public Customer(String id, String displayName) {
            super(id, displayName);
        }
    }

}
