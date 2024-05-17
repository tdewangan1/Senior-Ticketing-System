package com.example.servingwebcontent.controllers;

// Imports
import com.example.servingwebcontent.database.DatabaseOperations;
import com.example.servingwebcontent.service.TicketService;
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
import com.example.servingwebcontent.users.Volunteer;
import com.example.servingwebcontent.users.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Controller for handling dashboard functionalities including viewing, adding, and deleting tickets.
 * Maps all requests with a "/user" prefix.
 * Contributors: Shubham Kale and Tanmay Dewangan
 */
@Controller
@RequestMapping("/user")
public class DashboardController {

    private final Firestore db; // Database service instance variable for Firestore operations.
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
     * Contributors: Tanmay Dewangan
     */
    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        // Check if the user is logged in
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        // Add the username to the model
        model.addAttribute("username", username);


        try {
            // Get all tickets from the Firestore database
            ArrayList<Ticket> tickets = ticketService.getAllTickets();
            // Add the tickets to the model which will be displayed on the dashboard
            model.addAttribute("tickets", tickets);

            // Get the user type and additional information based on the user type
            Map<String, Object> userMap = DatabaseOperations.getDocumentByUsernameFromFirestore(db, "users", username);
            String userType = (String) userMap.get("userType");

            if (userType.equals("volunteer")) {
                // Get the number of volunteer hours that the user currently has
                Long volunteerHours =  (long) userMap.get("volunteerHours");
                // Convert it to a string so that it can be displayed on the dashboard
                String volunteerHoursStr = String.valueOf(userMap.get("volunteerHours"));

                // Add a boolean variable to the model to check if the user is a volunteer
                model.addAttribute("isVolunteer", true);

                // Add the volunteer hours to the model if the user is a volunteer
                model.addAttribute("volunteerHours", volunteerHoursStr);

            } else {
                // Add a boolean variable to the model to check if the user is a volunteer
                model.addAttribute("isVolunteer", false);

                // Add the retirement home name to the model if the user is a retirement home staff
                model.addAttribute("retirementHome", userMap.get("retirementHome"));

            }

        // Catch exceptions and display an error message if there are any issues
        } catch (InterruptedException | ExecutionException e) {
            model.addAttribute("error", "Failed to load tickets.");
        }

        // Return the dashboard template html page
        return "dashboard";
    }


    // Provides a form to submit a new ticket from the dashboard page.
    // Contributors: Tanmay Dewangan
    @GetMapping("/dashboard/add-ticket")
    public String showAddTicketForm(Model model) {
        return "dashboard"; // Just returns the dashboard page where the form is included.
    }


    // Handles the submission of a new ticket form and redirects to the dashboard.
    // Contributors: Tanmay Dewangan
    @PostMapping("/dashboard/add-ticket")
    public String addTicket(@RequestParam("residentName") String residentName,
                            @RequestParam("roomNumber") String roomNumber,
                            @RequestParam("description") String description,
                            @RequestParam("urgency") String urgency,
                            Model model) {

        // Make sure the urgency is capitalized
        String capitalizedUrgency = urgency.substring(0, 1).toUpperCase() + urgency.substring(1).toLowerCase();

        // Create a new ticket object with the form data
        Ticket ticket = new Ticket(db, residentName, roomNumber, description, capitalizedUrgency);

        // Return the dashboard page after adding the ticket
        return "redirect:/user/dashboard";
    }

    /**
     * Deletes a ticket given its ID by calling the deleteTicket method and redirects to the dashboard.
     * Only called if the user is a volunteer, not a staff
     * Contributors: Shubham Kale
     */
    @PostMapping("/dashboard/delete-ticket-volunteer")
    public String deleteTicket(@RequestParam("ticketId") String ticketId, @RequestParam("currentHours") String currentHours, @RequestParam("usernameT") String username) {
        // Delete the ticket by calling the method from the ticketService class
        ticketService.deleteTicket(ticketId);

        // Update volunteer hours by incrementing by 1
        int newHours = Integer.parseInt(currentHours) + 1;
        DatabaseOperations.writeDataToFirestoreByUsername(db, "users", username, "volunteerHours", newHours);

        // Redirect to the dashboard which will display the updated volunteer hours and the updated ticket list
        return "redirect:/user/dashboard";
    }

    /**
     * Deletes a ticket given its ID by calling the deleteTicket method and redirects to the dashboard.
     * Contributors: Shubham Kale
     */
    @PostMapping("/dashboard/delete-ticket")
    public String deleteTicket(@RequestParam("ticketId") String ticketId) {
        // Delete the ticket by calling the method from the ticketService class
        ticketService.deleteTicket(ticketId);

        // Redirect to the dashboard which will display the updated ticket list
        return "redirect:/user/dashboard";
    }
}