package com.example.servingwebcontent.users;

import com.google.cloud.firestore.Firestore;
import com.example.servingwebcontent.database.DatabaseOperations;

import java.util.ArrayList;
import java.util.Map;
public class AllUsers {
    public static ArrayList<User> usersList = new ArrayList<User>();
    public AllUsers(Firestore db){
        ArrayList<Map<String, Object>> allUsersMap = (ArrayList<Map<String, Object>>) DatabaseOperations.getAllDocumentsFromFirestore(db, "users");
        for (Map<String, Object> userMap : allUsersMap){
            if (((String) userMap.get("username")).equals("staff"))
                usersList.add(new Staff(db, userMap));
            if (((String) userMap.get("username")).equals("volunteer"))
                usersList.add(new Volunteer(db, userMap));
        }
    }
}
