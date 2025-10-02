package com.example.category.config;

import com.example.category.entity.User;
import com.example.category.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create or update default admin user
        createOrUpdateUser("admin", "admin123", User.Role.ADMIN);
        
        // Create or update default customer user
        createOrUpdateUser("customer", "customer123", User.Role.CUSTOMER);
    }
    
    private void createOrUpdateUser(String username, String plainPassword, User.Role role) {
        Optional<User> existingUser = userService.findByUsername(username);
        
        if (existingUser.isEmpty()) {
            // Create new user
            User newUser = new User(username, plainPassword, role);
            userService.save(newUser);
            System.out.println("Created default " + role.toString().toLowerCase() + " user: " + username + "/" + plainPassword);
        } else {
            // Check if existing user has properly encoded password
            User user = existingUser.get();
            String storedPassword = user.getPassword();
            
            // If password doesn't start with $2a, $2b, or $2y, it's likely not BCrypt encoded
            if (!storedPassword.startsWith("$2a") && !storedPassword.startsWith("$2b") && !storedPassword.startsWith("$2y")) {
                System.out.println("Found user with unencoded password, re-encoding: " + username);
                user.setPassword(plainPassword); // Set plain password
                userService.save(user); // UserService will encode it
                System.out.println("Re-encoded password for user: " + username);
            } else {
                System.out.println("User " + username + " already exists with properly encoded password");
            }
        }
    }
}


