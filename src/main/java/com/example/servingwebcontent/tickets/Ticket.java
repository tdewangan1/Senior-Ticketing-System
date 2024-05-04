package com.example.servingwebcontent.tickets;

import com.example.servingwebcontent.database.DatabaseOperations;
import com.google.cloud.firestore.Firestore;

import java.util.HashMap;
import java.util.Map;

public class Ticket {
    private String residentName;
    private String roomNumber;
    private String urgency;
    private String description;
    private Firestore db;
    public static int ticketCount;
    public Ticket(Firestore db, String residentName,  String roomNumber, String description, String urgency){
        this.db = db;
        this.roomNumber = roomNumber;
        this.residentName = residentName;
        this.description = description;
        this.urgency = urgency;

        Map<String, Object> ticketData = new HashMap<>();
        ticketData.put("residentName", residentName);
        ticketData.put("roomNumber", roomNumber);
        ticketData.put("description", description);
        ticketData.put("urgency", urgency);

        ticketCount = DatabaseOperations.getDocumentNamesFromFirestore(db, "tickets").size() + 1;

        int numZeros = 5 - String.valueOf(ticketCount).length();
        String docID = "ticket" + "0".repeat(numZeros) + ticketCount;
        DatabaseOperations.writeDataToFirestore(db, "tickets", docID, ticketData);
    }

    public Ticket(Firestore db, Map<String, Object> ticketMap) {
        this.db=db;

        // Assigning instance variables from the userMap, ensuring to cast the objects to their appropriate types.
        this.residentName = (String) ticketMap.get("residentName");
        this.roomNumber = (String) ticketMap.get("roomNumber");
        this.urgency = (String) ticketMap.get("urgency");
        this.description = (String) ticketMap.get("description");


    }

    public String getResidentName() {
        return residentName;
    }

    public String getRoomNumber(){
        return roomNumber;
    }

    public String geturgency(){
        return urgency;
    }

    public String getDescription(){
        return description;
    }

    public String toString(){
        String output = "";
        output += "Description: " + this.description + "\n";
        output += "Resident Name: " + this.residentName + "\n";
        output += "Room Number: " + this.roomNumber + "\n";
        output += "Urgency: " + this.urgency;
        return output;
    }
}
