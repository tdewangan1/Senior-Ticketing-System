package com.example.servingwebcontent.users;

import com.google.cloud.firestore.Firestore;

import java.util.Map;

public class Volunteer extends User{
    public Volunteer(String username, String password, Firestore db){
        super(db, username, password);
        AllUsers.usersList.add(this);
    }

    public Volunteer(Firestore db, Map<String, Object> userMap){
        super(db, userMap);
    }
}
