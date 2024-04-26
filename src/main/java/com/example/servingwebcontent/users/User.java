package com.example.servingwebcontent.users;

import com.example.servingwebcontent.database.DatabaseOperations;
import com.example.servingwebcontent.login.AuthUtils;
import com.google.cloud.firestore.Firestore;


import java.util.HashMap;
import java.util.Map;

public class User {
    private String username;
    private String hashedPassword;
    private String userType;
    Firestore db;
    public static int userCount;

    public User(Firestore db, String username, String password) {
        this.username = username;
        this.hashedPassword = AuthUtils.hashPassword(password);
//        this.userType = userType;
        this.db=db;

        Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);
        userData.put("hashedPassword", hashedPassword);
        userData.put("userType", userType);


        userCount = DatabaseOperations.getDocumentNamesFromFirestore(db, "users").size() + 1;
        int numZeros = 3 - String.valueOf(userCount).length();
        String docID = "user" + "0".repeat(numZeros) + userCount;
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

    public void changeUsername(Firestore db, String newUserName){
        DatabaseOperations.writeDataToFirestore(db, "users", username, "username", newUserName);
        this.username = newUserName;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getUserType() {return userType;}


}
