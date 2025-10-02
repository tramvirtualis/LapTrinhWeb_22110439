package com.example.category.service;

import com.example.category.entity.User;
import com.example.category.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        String originalPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(originalPassword);
        user.setPassword(encodedPassword);
        
        // Debug logging
        System.out.println("Saving user: " + user.getUsername());
        System.out.println("Original password length: " + originalPassword.length());
        System.out.println("Encoded password starts with: " + encodedPassword.substring(0, Math.min(10, encodedPassword.length())));
        System.out.println("User role: " + user.getRole());
        
        User savedUser = userRepository.save(user);
        System.out.println("User saved with ID: " + savedUser.getId());
        return savedUser;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public boolean authenticate(String username, String password) {
        try {
            Optional<User> userOpt = findByUsername(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String storedPassword = user.getPassword();
                boolean matches = passwordEncoder.matches(password, storedPassword);
                
                // Debug logging
                System.out.println("=== Authentication Debug ===");
                System.out.println("Username: " + username);
                System.out.println("User found: " + user.getUsername());
                System.out.println("User role: " + user.getRole());
                System.out.println("Input password length: " + password.length());
                System.out.println("Stored password length: " + storedPassword.length());
                System.out.println("Stored password starts with: " + storedPassword.substring(0, Math.min(10, storedPassword.length())));
                System.out.println("Is BCrypt format: " + (storedPassword.startsWith("$2a") || storedPassword.startsWith("$2b") || storedPassword.startsWith("$2y")));
                System.out.println("Password matches: " + matches);
                System.out.println("=== End Debug ===");
                
                return matches;
            } else {
                System.out.println("User not found: " + username);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Authentication error for user " + username + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isPasswordEncoded(String password) {
        return password != null && (password.startsWith("$2a") || password.startsWith("$2b") || password.startsWith("$2y"));
    }
}


