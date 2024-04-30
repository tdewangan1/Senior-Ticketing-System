package com.example.servingwebcontent.tickets;

import com.example.servingwebcontent.database.DatabaseOperations;
import com.google.cloud.firestore.Firestore;

import java.util.ArrayList;
import java.util.Map;

public class TicketManager {
    public static ArrayList<Ticket> ticketsList = new ArrayList<Ticket>();
    public TicketManager(Firestore db){
        ArrayList<Map<String, Object>> allUsersMap = (ArrayList<Map<String, Object>>) DatabaseOperations.getAllDocumentsFromFirestore(db, "tickets");
        for (Map<String, Object> userMap : allUsersMap){
//            ticketsList.add(new Ticket(db, userMap));
        }
    }
}
