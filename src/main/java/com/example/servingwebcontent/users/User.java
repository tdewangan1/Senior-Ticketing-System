package com.example.servingwebcontent.users;

import com.example.servingwebcontent.database.DatabaseOperations;
import com.example.servingwebcontent.login.AuthUtils;
import com.google.cloud.firestore.Firestore;


import java.util.HashMap;
import java.util.Map;

// User parent class that models the user object within the system.
// Contributors: Shubham Kale and Tanmay Dewangan
public class User {
    // Declaration of private instance variables to encapsulate user information.
    private String username;
    private String hashedPassword;
    private String userType;
    Firestore db;
    public static int userCount;

    /*
     * Constructor for creating a new user with provided details and saving to Firestore.
     * @param db: Firestore database instance for DB operations.
     * @param username: Username of the user.
     * @param password: Password of the user.
     * @param userData: Map containing additional user data such as volunteer hours or retirement home.
     * @param userType: Type of user (staff or volunteer).
     * Contributors: Shubham Kale and Tanmay Dewangan
     */
    public User(Firestore db, String username, String password, Map<String, Object> userData, String userType){
        // Assigning instance variables from the parameters.
        this.username = username;
        this.hashedPassword = AuthUtils.hashPassword(password);
        this.userType = userType;
        this.db=db;

        // Adding the username, hashed password, and user type to the userData map.
        userData.put("username", username);
        userData.put("hashedPassword", hashedPassword);
        userData.put("userType", userType);

        // Generating a unique ID for each user using the userCount static variable.
        userCount = DatabaseOperations.getDocumentNamesFromFirestore(db, "users").size() + 1;
        int numZeros = 3 - String.valueOf(userCount).length();
        String docID = "user" + "0".repeat(numZeros) + userCount;

        // Writing the user data to the Firestore database.
        DatabaseOperations.writeDataToFirestore(db, "users", docID, userData);
    }

    // This constructor used to create User objects that are already in the database
    // (so no need to add them to database again)
    public User(Firestore db, Map<String, Object> userMap) {
        this.db=db;

        // Assigning instance variables from the userMap, ensuring to cast the objects to their appropriate types.
        this.username = (String) userMap.get("username");
        this.hashedPassword = (String) userMap.get("hashedPassword");
//        this.userType = (String) userMap.get("userType");
    }

    // Getter methods to access the private instance variables.
    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getUserType() {return userType;}


}
