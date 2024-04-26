package com.example.servingwebcontent.users;

import com.google.cloud.firestore.Firestore;

import java.util.Map;

public class Staff extends User{
    public Staff(String username, String password, Firestore db){
        super(db, username, password);
        AllUsers.usersList.add(this);
    }

    public Staff(Firestore db, Map<String, Object> userMap){
        super(db, userMap);
    }
}
