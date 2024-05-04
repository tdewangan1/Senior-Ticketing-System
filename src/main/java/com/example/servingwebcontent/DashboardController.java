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

@Controller // Marks this class as a Spring MVC controller.
@RequestMapping("/user") // All request mappings in this controller will be prefixed with "/user".
public class DashboardController {

    private final Firestore db; // Firestore database instance.
    private final TicketService ticketService; // Ticket service instance.

    @Autowired  // This annotation injects the Firestore instance into this controller.
    public DashboardController(Firestore db, TicketService ticketService) {
        this.db = db; // Initializes the Firestore database instance.
        this.ticketService = ticketService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        // Retrieve the username from the session.
        String username = (String) session.getAttribute("username");
        // Check if the user is logged in by checking if username exists in the session.
        if (username == null) {
            // If not logged in, redirect to the login page.
            return "redirect:/login";
        }

        // Add the username to the model to make it accessible in the view (template).
        model.addAttribute("username", username);

        try {
            ArrayList<Ticket> tickets = ticketService.getAllTickets();
            model.addAttribute("tickets", tickets);
        } catch (InterruptedException | ExecutionException e) {
            model.addAttribute("error", "Failed to load tickets.");
        }

        // Return the name of the Thymeleaf template to render the dashboard.
        return "dashboard";
    }

    @GetMapping("dashboard/add-ticket")
    public String showAddTicketForm(Model model) {
        // Log to the console whenever this method is accessed.
        System.out.println("SUBMIT TICKET PRESSED 1");
        // Return the dashboard view which includes the ticket submission form.
        return "dashboard";
    }

    @PostMapping("dashboard/add-ticket")
    public String addTicket(@RequestParam("residentName") String residentName,
                            @RequestParam("roomNumber") String roomNumber,
                            @RequestParam("description") String description,
                            @RequestParam("urgency") String urgency,
                            Model model) {
        // Log to the console when a ticket is submitted.
        System.out.println("SUBMIT TICKET PRESSED 2");

        // Create a new Ticket object with the provided form data.
        Ticket ticket = new Ticket(db, residentName, roomNumber, description, urgency);

        // Redirect to the dashboard page after the ticket is submitted.
        return "redirect:/user/dashboard";
    }
}
