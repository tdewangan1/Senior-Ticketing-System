package com.example.servingwebcontent.login;

import com.example.servingwebcontent.database.DatabaseOperations;
import com.example.servingwebcontent.users.User;
import com.google.cloud.firestore.Firestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

// Utility class providing authentication-related methods such as password hashing and user verification.
public class AuthUtils {

    // Hashes a password using SHA-256 and returns the hexadecimal string.
    // Contributors: Tanmay Dewangan
    public static String hashPassword(String password) {
        try {
            // Create a MessageDigest instance for SHA-256.
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Perform the hash computation and get the resulting byte array.
            byte[] encodedhash = digest.digest(password.getBytes());

            // Convert the byte array into a hex string.
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            // Return the hex string representation of the hashed password.
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle potential exception by throwing a runtime exception.
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    // Authenticates a user by username and password. Returns true if the user exists and the password matches.
    // Contributors: Tanmay Dewangan
    public static boolean authenticate(Firestore db, String username, String password) throws ExecutionException, InterruptedException {
        // Retrieve all document IDs in the 'users' collection.
        ArrayList<String> userDocIDs = (ArrayList<String>) DatabaseOperations.getDocumentNamesFromFirestore(db, "users");
        for (String docID : userDocIDs){
            // Retrieve user data by document ID and create a User object.
            User user = new User(db, DatabaseOperations.getDocumentDataFromFirestore(db, "users", docID));
            if (user.getUsername().equals(username)) {
                // Hash the input password to compare with the stored hash.
                String hashedInputPassword = hashPassword(password);
                // Check if the hashed input password matches the stored hash.
                return user.getHashedPassword().equals(hashedInputPassword);
            }
        }
        return false; // Return false if no matching user is found or if the password does not match.
    }
}
