package com.example.servingwebcontent.users;

import com.google.cloud.firestore.Firestore;

import java.util.HashMap;
import java.util.Map;

// Staff class that extends the User class
// Contributors: Shubham Kale
public class Staff extends User{
    // Instance variable to store the retirement home of the staff member
    private String retirementHome;

    // Constructor for creating a new staff member with provided details and saving to Firestore.
    public Staff(Firestore db, String username, String password, String retirementHome){
        // Calling the super constructor to initialize the user object
        super(db, username, password, new HashMap<String, Object>() {{
            put("retirementHome", retirementHome);
        }}, "staff");

        // Setting the retirement home of the staff member
        this.retirementHome = retirementHome;
    }

    // Constructor for creating a staff member from Firestore data
    public Staff(Firestore db, Map<String, Object> userMap){
        super(db, userMap);
    }
}
