package com.example.servingwebcontent;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class DashboardController {

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
}

