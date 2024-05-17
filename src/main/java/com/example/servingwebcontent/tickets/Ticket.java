package com.example.servingwebcontent.tickets;

// Importing necessary classes from other packages.
import com.example.servingwebcontent.database.DatabaseOperations;
import com.google.cloud.firestore.Firestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Definition of the Ticket class which models the ticket object within the system.
public class Ticket {
    // Declaration of private instance variables to encapsulate ticket information.
    private String residentName;
    private String roomNumber;
    private String urgency;
    private String description;
    private String ticketID; // Unique identifier for each ticket.
    private Firestore db; // Firestore database instance for DB operations.
    public static int ticketCount; // Static variable to keep track of tickets.


    // Constructor for creating a new ticket with provided details and saving to Firestore.
    // Contributors: Tanmay Dewangan
    public Ticket(Firestore db, String residentName,  String roomNumber, String description, String urgency){
        this.db = db;
        this.roomNumber = roomNumber;
        this.residentName = residentName;
        this.description = description;
        this.urgency = urgency;

        // Using a HashMap to hold ticket data for database operations.
        Map<String, Object> ticketData = new HashMap<>();
        ticketData.put("residentName", residentName);
        ticketData.put("roomNumber", roomNumber);
        ticketData.put("description", description);
        ticketData.put("urgency", urgency);

        // Generating a unique ID for each ticket using UUID.
        ticketID = UUID.randomUUID().toString();
        ticketData.put("ticketID", ticketID);

        // Writing the ticket data to the Firestore database.
        DatabaseOperations.writeDataToFirestore(db, "tickets", ticketID, ticketData);
    }

    /*
     * Alternate constructor used for creating ticket objects from Firestore data.
     * This does NOT write the ticket to the database.
     * Used when retrieving tickets from the database and saving it in an ArrayList of Ticket objects in the TicketService class.
     * Contributors: Shubham Kale
     */
    public Ticket(Firestore db, Map<String, Object> ticketMap) {
        this.db=db;
        this.residentName = (String) ticketMap.get("residentName");
        this.roomNumber = (String) ticketMap.get("roomNumber");
        this.urgency = (String) ticketMap.get("urgency");
        this.description = (String) ticketMap.get("description");
        this.ticketID = (String) ticketMap.get("ticketID");
    }

    // Getter methods for retrieving private data fields.
    public String getResidentName() {
        return residentName;
    }

    public String getRoomNumber(){
        return roomNumber;
    }

    public String getUrgency(){
        return urgency;
    }

    public String getDescription(){
        return description;
    }
    public String getTicketID(){
        return ticketID;
    }
}
