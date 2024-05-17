package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.database.DatabaseOperations;
import com.example.servingwebcontent.login.AuthUtils;
import com.example.servingwebcontent.users.Staff;
import com.example.servingwebcontent.users.User;
import com.example.servingwebcontent.users.Volunteer;
import com.google.cloud.firestore.Firestore;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

// Controller responsible for handling user-related operations (login, signup, etc.)
@Controller
public class UserController {

    private final Firestore db;

    // Constructor to inject Firestore instance for database operations
    @Autowired
    public UserController(Firestore db) {
        this.db = db;
    }

    // Displays the sign-up form to the user
    // Contributors: Shubham Kale
    @GetMapping("/signup")
    public String signupForm(Model model) {
        return "signup";
    }

    // Displays the login form to the user
    // Contributors: Shubham Kale
    @GetMapping("/login")
    public String loginForm(Model model) {
        return "login";
    }


    // Handles the user sign-up request and validates the input
    // Contributors: Shubham Kale and Tanmay Dewangan
    @PostMapping("/signup")
    public String handleSignup(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("confirm_password") String confirmedPassword,
            @RequestParam("user_type") String userType,
            @RequestParam("retirement_home") String retirementHome,
            Model model) throws ExecutionException, InterruptedException {

        // Checking if the password and confirmed password match
        // If not, return an error message to the user which will be displayed on the signup page
        if (!password.equals(confirmedPassword)){
            model.addAttribute("signupError", "Please make sure your passwords match");
            return "signup";
        }

        // Checking if the username already exists in the database.
        // If it exists, return an error message to the user which will be displayed on the signup page
        ArrayList<String> userDocIDs = (ArrayList<String>) DatabaseOperations.getDocumentNamesFromFirestore(db, "users");
        for (String docID : userDocIDs){
            User user = new User(db, DatabaseOperations.getDocumentDataFromFirestore(db, "users", docID));
            if (user.getUsername().equals(username)) {
                model.addAttribute("signupError", "This username already exists.");
                return "signup";
            }
        }

        // If the username is unique and the passwords match, create a new user object and store it in the database
        try {

            // Create a new user object based on the user type selected by the user
            if (userType.equals("Staff")) {
                User user = new Staff(db, username, password, retirementHome);
            }
            else if (userType.equals("Volunteer")) {
                User user = new Volunteer(db, username, password, 0);
            }

//            User user = new User(db, username, password);
            // Display a success message to the user and redirect them to the login page
            model.addAttribute("signupSuccess", "Registration successful. Please login.");
            return "login";

        // Catch exceptions and display an error message if there are any issues
        } catch (Exception e) {
            model.addAttribute("signupError", "Registration failed: " + e.getMessage());
            return "signup";
        }
    }

    // Handles the user login request, validates the credentials, and manages session on success
    // Contributors: Tanmay Dewangan
    @PostMapping("/login")
    public String handleLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session, // Session management after successful login
            Model model) throws ExecutionException, InterruptedException {

        // Authenticate the user using the provided username and password by calling the authenticate method from AuthUtils
        boolean isAuthenticated = AuthUtils.authenticate(db, username, password);

        // If the user is authenticated, store the username in the session and redirect to the user dashboard
        if (isAuthenticated) {
            session.setAttribute("username", username); // Store username in session
            return "redirect:/user/dashboard"; // Redirect to user dashboard

        // If the user is not authenticated, display an error message on the login page
        } else {
            model.addAttribute("loginError", "Invalid username or password.");
            return "login";
        }
    }
}
