package com.example.category.controller;

import com.example.category.entity.Game;
import com.example.category.entity.Order;
import com.example.category.entity.User;
import com.example.category.service.GameService;
import com.example.category.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GameService gameService;

    @GetMapping
    public String myOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = "10") int size,
                          Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Page<Order> orderPage = orderService.getUserOrders(user, page, size);
        model.addAttribute("orderPage", orderPage);
        model.addAttribute("userRole", user.getRole().toString());
        model.addAttribute("username", user.getUsername());
        
        return "order/list";
    }

    @PostMapping("/purchase/{gameId}")
    public String purchaseGame(@PathVariable Long gameId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        if (user.getRole() != User.Role.CUSTOMER) {
            redirectAttributes.addFlashAttribute("error", "Only customers can purchase games");
            return "redirect:/games";
        }

        try {
            Game game = gameService.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found"));
            orderService.createOrder(user, game);
            redirectAttributes.addFlashAttribute("success", "Game purchased successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/games";
    }
}

