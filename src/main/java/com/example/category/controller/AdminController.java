package com.example.category.controller;

import com.example.category.entity.User;
import com.example.category.service.GameService;
import com.example.category.service.OrderService;
import com.example.category.service.ReviewService;
import com.example.category.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private GameService gameService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public String adminDashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }
        
        // Add dashboard statistics
        model.addAttribute("totalGames", gameService.findAll("", 0, Integer.MAX_VALUE).getTotalElements());
        model.addAttribute("totalUsers", userService.findAll().size());
        model.addAttribute("username", user.getUsername());
        
        return "admin/dashboard";
    }
}
