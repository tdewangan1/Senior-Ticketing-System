package com.example.servingwebcontent.users;

import com.google.cloud.firestore.Firestore;

import java.util.HashMap;
import java.util.Map;

// Volunteer class that extends the User class
// Contributors: Tanmay Dewangan
public class Volunteer extends User{
    // Instance variable to store the volunteer hours of the volunteer
    private int volunteerHours = 0;

    // Constructor for creating a new volunteer with provided details and saving to Firestore.
    public Volunteer(Firestore db, String username, String password, int volunteerHours){
        // Calling the super constructor to initialize the user object
        super(db, username, password, new HashMap<String, Object>() {{
            put("volunteerHours", volunteerHours);
        }}, "volunteer");

        // Setting the volunteer hours of the volunteer
        this.volunteerHours = volunteerHours;
    }

    // Constructor for creating a volunteer from Firestore data
    public Volunteer(Firestore db, Map<String, Object> userMap){
        super(db, userMap);
    }
}
