package com.example.category.controller;

import com.example.category.dto.LoginRequest;
import com.example.category.dto.RegisterRequest;
import com.example.category.entity.User;
import com.example.category.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginRequest") LoginRequest loginRequest,
                       BindingResult bindingResult,
                       HttpSession session,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }
        
        if (userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword())) {
            Optional<User> userOpt = userService.findByUsername(loginRequest.getUsername());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                session.setAttribute("user", user);
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole().toString());
                
                // Role-based redirect
                if (user.getRole() == User.Role.ADMIN) {
                    return "redirect:/admin";
                } else {
                    return "redirect:/user";
                }
            }
        }
        
        redirectAttributes.addFlashAttribute("error", "Invalid username or password");
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        
        if (!registerRequest.isPasswordMatching()) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Passwords do not match");
            return "auth/register";
        }
        
        // Check if username already exists
        if (userService.findByUsername(registerRequest.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "error.username", "Username already exists");
            return "auth/register";
        }
        
        // Create new customer user
        User newUser = new User(registerRequest.getUsername(), registerRequest.getPassword(), User.Role.CUSTOMER);
        userService.save(newUser);
        
        redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

