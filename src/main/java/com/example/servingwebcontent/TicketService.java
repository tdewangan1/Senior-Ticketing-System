package com.example.servingwebcontent;

import com.example.servingwebcontent.database.DatabaseOperations;
import com.example.servingwebcontent.tickets.Ticket;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class TicketService {

    private final Firestore db;

    @Autowired
    public TicketService(Firestore db) {
        this.db = db;
    }

    public ArrayList<Ticket> getAllTickets() throws InterruptedException, ExecutionException {
        ArrayList<Ticket> tickets = new ArrayList<>();
        ArrayList<Map<String, Object>> allTicketsMap = (ArrayList<Map<String, Object>>) DatabaseOperations.getAllDocumentsFromFirestore(db, "tickets");

        for (Map<String, Object> ticketMap : allTicketsMap) {
            tickets.add(new Ticket(db, ticketMap));
        }

        return tickets;
    }
}