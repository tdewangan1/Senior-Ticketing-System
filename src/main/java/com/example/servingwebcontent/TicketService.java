package com.example.servingwebcontent;

// Importing required classes from other packages.
import com.example.servingwebcontent.database.DatabaseOperations;
import com.example.servingwebcontent.tickets.Ticket;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

// Annotation to declare this class as a Spring Service, making it eligible for Spring-driven dependency injection.
@Service
public class TicketService {
    // A Firestore instance variable to handle database operations, injected by Spring's dependency injection mechanism.
    private final Firestore db;

    // Constructor that Spring uses to inject the Firestore database instance.
    @Autowired
    public TicketService(Firestore db) {
        this.db = db;
    }

    // Method to retrieve all tickets from the Firestore database. It returns a list of Ticket objects.
    public ArrayList<Ticket> getAllTickets() throws InterruptedException, ExecutionException {
        ArrayList<Ticket> tickets = new ArrayList<>();
        // Calls a static method from DatabaseOperations class to get all documents from the 'tickets' collection.
        ArrayList<Map<String, Object>> allTicketsMap =
                (ArrayList<Map<String, Object>>) DatabaseOperations.getAllDocumentsFromFirestore(db, "tickets");

        // Loop through each map in the list, create a Ticket object using the map, and add it to the list of tickets.
        for (Map<String, Object> ticketMap : allTicketsMap) {
            tickets.add(new Ticket(db, ticketMap));
        }

        return tickets; // Returns the list of Ticket objects.
    }

    // Method to delete a ticket from the Firestore database using the ticket's unique ID.
    public void deleteTicket(String ticketId) {
        db.collection("tickets").document(ticketId).delete(); // Deletes the document identified by ticketId from the 'tickets' collection.
    }
}
