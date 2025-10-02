package com.example.category.controller;

import com.example.category.entity.User;
import com.example.category.service.GameService;
import com.example.category.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private GameService gameService;
    
    @Autowired
    private OrderService orderService;

    @GetMapping
    public String userDashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.CUSTOMER) {
            return "redirect:/login";
        }
        
        // Add user dashboard data
        model.addAttribute("username", user.getUsername());
        model.addAttribute("totalGames", gameService.findAll("", 0, Integer.MAX_VALUE).getTotalElements());
        
        return "user/dashboard";
    }
}
