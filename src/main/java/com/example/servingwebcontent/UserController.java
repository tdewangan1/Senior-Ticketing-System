package com.example.servingwebcontent;

import com.example.servingwebcontent.database.DatabaseOperations;
import com.example.servingwebcontent.login.AuthUtils;
import com.example.servingwebcontent.users.User;
import com.google.cloud.firestore.Firestore;

import jakarta.servlet.http.HttpSession;
//import login.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


@Controller
public class UserController {

    private final Firestore db;

    @Autowired  // This annotation is optional if you have a single constructor
    public UserController(Firestore db) {
        this.db = db;
    }

//    @GetMapping("/")
//    public String index() {
//        return "index"; // Render the landing page with signup and login options
//    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
//        model.addAttribute("user", new User());
        model.addAttribute("user", "testUser");
        return "signup"; // Render the signup form
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        return "login"; // Render the login form
    }


//    @PostMapping("/signup")
//    public String handleSignup(@RequestParam String username, @RequestParam String password) {
//        // You might need to adjust the User class or create a new method for signup.
//        new User(db, username, password);
//        return "redirect:/login"; // Redirect to login page after successful signup
//    }

    @PostMapping("/signup")
    public String handleSignup(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("confirm_password") String confirmedPassword,
            Model model) throws ExecutionException, InterruptedException {
        // Here you can invoke your service method to register the new user

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
            // Handle the case where user registration fails (e.g., username already exists)
            model.addAttribute("signupError", "Registration failed: " + e.getMessage());
            return "signup";
        }
    }



//    @PostMapping("/login")
//    public String handleLogin(@RequestParam String username, @RequestParam String password, Model model) {
//        // Authentication logic here...
//        boolean isAuthenticated = AuthUtils.authenticate(username, password);
//        if (isAuthenticated) {
//            // Add success message and other necessary data to the model
//            model.addAttribute("username", username);
//            return "userProfile"; // Redirect to user profile or dashboard
//        } else {
//            // Add error message to the model
//            model.addAttribute("loginError", "Invalid username or password.");
//            return "login"; // Reload the login page with an error message
//        }
//    }

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session, // To manage the session after successful login
            Model model) throws ExecutionException, InterruptedException {
        // Here you can invoke your service method to authenticate the user
        boolean isAuthenticated = AuthUtils.authenticate(db, username, password);
//        boolean isAuthenticated = false;
        if (isAuthenticated) {
            // If authentication is successful, manage user session and proceed
            session.setAttribute("username", username); // Store username in the session
            return "redirect:/user/dashboard"; // Redirect to a secure user dashboard page
        } else {
            // Authentication failed, show login page with error message
            model.addAttribute("loginError", "Invalid username or password.");
            return "login";
        }
    }

}
