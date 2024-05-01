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

@Controller
@RequestMapping("/user")
public class DashboardController {

    private final Firestore db;

    @Autowired  // This annotation is optional if you have a single constructor
    public DashboardController(Firestore db) {
        this.db = db;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        // Optional: Check if the user is logged in
        String username = (String) session.getAttribute("username");
        if (username == null) {
            // If not logged in, redirect to login page
            return "redirect:/login";
        }

        // Add more data to the model as needed
        model.addAttribute("username", username);

        return "dashboard"; // This should be the name of the Thymeleaf template for the dashboard
    }


    @GetMapping("/dashboard/add-ticket")
    public String showAddTicketForm(Model model) {
        System.out.println("SUBMIT TICKET PRESSED 1");
        return "dashboard"; // assuming the dashboard contains the overlay and the form
    }

    @PostMapping("/dashboard/add-ticket")
    public String addTicket(@RequestParam("residentName") String residentName,
                            @RequestParam("roomNumber") String roomNumber,
                            @RequestParam("description") String description,
                            @RequestParam("urgency") String urgency,
                            Model model) {
        System.out.println("Ticket submission received: " + residentName + ", " + roomNumber + ", " + description + ", " + urgency);

        Ticket ticket = new Ticket(db, residentName, roomNumber, description, urgency);
        // Make sure you save the ticket to the database here

        return "redirect:/dashboard"; // Redirect back to the dashboard after ticket submission
    }

}

