package com.example.servingwebcontent;

import com.example.servingwebcontent.tickets.Ticket;
import com.google.cloud.firestore.Firestore;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Controller for handling dashboard functionalities including viewing, adding, and deleting tickets.
 * Maps all requests with a "/user" prefix.
 */
@Controller
@RequestMapping("/user")
public class DashboardController {

    private final Firestore db; // Database service for Firestore operations.
    private final TicketService ticketService; // Service for managing ticket-related operations.


    // Constructor to inject Firestore and TicketService dependencies.
    @Autowired
    public DashboardController(Firestore db, TicketService ticketService) {
        this.db = db;
        this.ticketService = ticketService;
    }

    /**
     * Displays the dashboard page with tickets if the user is logged in.
     * Redirects to the login page if the user is not logged in.
     */
    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", username);

        try {
            ArrayList<Ticket> tickets = ticketService.getAllTickets();
            model.addAttribute("tickets", tickets);
        } catch (InterruptedException | ExecutionException e) {
            model.addAttribute("error", "Failed to load tickets.");
        }

        return "dashboard";
    }


    // Provides a form to submit a new ticket from the dashboard page.
    @GetMapping("/dashboard/add-ticket")
    public String showAddTicketForm(Model model) {
        return "dashboard"; // Just returns the dashboard page where the form is included.
    }


    // Handles the submission of a new ticket form and redirects to the dashboard.
    @PostMapping("/dashboard/add-ticket")
    public String addTicket(@RequestParam("residentName") String residentName,
                            @RequestParam("roomNumber") String roomNumber,
                            @RequestParam("description") String description,
                            @RequestParam("urgency") String urgency,
                            Model model) {
        // Make sure the urgency is capitalized
        String capitalizedUrgency = urgency.substring(0, 1).toUpperCase() + urgency.substring(1).toLowerCase();
        Ticket ticket = new Ticket(db, residentName, roomNumber, description, capitalizedUrgency);

        return "redirect:/user/dashboard";
    }


    // Deletes a ticket given its ID by calling the deleteTicket method and redirects to the dashboard.
    @PostMapping("/dashboard/delete-ticket")
    public String deleteTicket(@RequestParam("ticketId") String ticketId) {
        ticketService.deleteTicket(ticketId);
        return "redirect:/user/dashboard";
    }
}
