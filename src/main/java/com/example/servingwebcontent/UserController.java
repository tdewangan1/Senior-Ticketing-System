package com.example.servingwebcontent;

import com.example.servingwebcontent.database.DatabaseOperations;
import com.example.servingwebcontent.login.AuthUtils;
import com.example.servingwebcontent.users.User;
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

// Controller responsible for handling user-related operations
@Controller
public class UserController {

    private final Firestore db;

    // Constructor to inject Firestore instance for database operations
    @Autowired
    public UserController(Firestore db) {
        this.db = db;
    }

    // Displays the sign-up form to the user
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", "testUser");
        return "signup";
    }

    // Displays the login form to the user
    @GetMapping("/login")
    public String loginForm(Model model) {
        return "login";
    }

    // Handles the user sign-up request and validates the input
    @PostMapping("/signup")
    public String handleSignup(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("confirm_password") String confirmedPassword,
            Model model) throws ExecutionException, InterruptedException {

        if (!password.equals(confirmedPassword)){
            model.addAttribute("signupError", "Please make sure your passwords match");
            return "signup";
        }

        ArrayList<String> userDocIDs = (ArrayList<String>) DatabaseOperations.getDocumentNamesFromFirestore(db, "users");
        for (String docID : userDocIDs){
            User user = new User(db, DatabaseOperations.getDocumentDataFromFirestore(db, "users", docID));
            if (user.getUsername().equals(username)) {
                model.addAttribute("signupError", "This username already exists.");
                return "signup";
            }
        }

        try {
            User user = new User(db, username, password);
            model.addAttribute("signupSuccess", "Registration successful. Please login.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("signupError", "Registration failed: " + e.getMessage());
            return "signup";
        }
    }

    // Handles the user login request, validates the credentials, and manages session on success
    @PostMapping("/login")
    public String handleLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session, // Session management after successful login
            Model model) throws ExecutionException, InterruptedException {

        boolean isAuthenticated = AuthUtils.authenticate(db, username, password);
        if (isAuthenticated) {
            session.setAttribute("username", username); // Store username in session
            return "redirect:/user/dashboard"; // Redirect to user dashboard
        } else {
            model.addAttribute("loginError", "Invalid username or password.");
            return "login";
        }
    }
}
