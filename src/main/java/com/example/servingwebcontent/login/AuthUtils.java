package com.example.servingwebcontent.login;



import com.example.servingwebcontent.database.DatabaseOperations;
import com.example.servingwebcontent.users.User;
import com.google.cloud.firestore.Firestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AuthUtils {
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    public static boolean authenticate(Firestore db, String username, String password) throws ExecutionException, InterruptedException {
        ArrayList<String> userDocIDs = (ArrayList<String>) DatabaseOperations.getDocumentNamesFromFirestore(db, "users");
        for (String docID : userDocIDs){
            User user = new User(db, DatabaseOperations.getDocumentDataFromFirestore(db, "users", docID));
            if (user.getUsername().equals(username)) {
                // Compare the hashed password
                String hashedInputPassword = hashPassword(password);
                return user.getHashedPassword().equals(hashedInputPassword);
            }
        }
        return false;
    }
}