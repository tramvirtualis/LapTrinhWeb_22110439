package com.example.category.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.category.entity.Game;
import com.example.category.entity.User;
import com.example.category.service.GameService;
import com.example.category.service.OrderService;
import com.example.category.service.ReviewService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/games")
public class GameController {
    private final GameService gameService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ReviewService reviewService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String keyword,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "6") int size,
                       Model model, HttpSession session) {
        Page<Game> gamePage = gameService.findAll(keyword, page, size);
        model.addAttribute("gamePage", gamePage);
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        
        // Add user role to model for template rendering
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("userRole", user.getRole().toString());
            model.addAttribute("username", user.getUsername());
            
            // Check if user has purchased each game (for customers)
            if (user.getRole() == User.Role.CUSTOMER) {
                Map<Long, Boolean> purchaseStatus = new HashMap<>();
                for (Game game : gamePage.getContent()) {
                    boolean hasPurchased = orderService.hasUserPurchasedGame(user, game);
                    purchaseStatus.put(game.getId(), hasPurchased);
                }
                model.addAttribute("purchaseStatus", purchaseStatus);
            }
        }
        
        return "game/list";
    }

    @GetMapping("/new")
    public String createForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/games";
        }
        model.addAttribute("game", new Game());
        return "game/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("game") Game game, BindingResult bindingResult, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/games";
        }
        if (bindingResult.hasErrors()) {
            return "game/form";
        }
        gameService.save(game);
        return "redirect:/games";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/games";
        }
        Game game = gameService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        model.addAttribute("game", game);
        return "game/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("game") Game game,
                         BindingResult bindingResult, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/games";
        }
        if (bindingResult.hasErrors()) {
            return "game/form";
        }
        game.setId(id);
        gameService.save(game);
        return "redirect:/games";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        Game game = gameService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
        model.addAttribute("game", game);
        
        // Add user role to model for template rendering
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("userRole", user.getRole().toString());
            model.addAttribute("username", user.getUsername());
            
            // Check if user has purchased this game (for customers)
            if (user.getRole() == User.Role.CUSTOMER) {
                boolean hasPurchased = orderService.hasUserPurchasedGame(user, game);
                model.addAttribute("hasPurchased", hasPurchased);
                
                // Get user's review if exists
                model.addAttribute("userReview", reviewService.getUserReviewForGame(user, game).orElse(null));
            }
        }
        
        return "game/detail";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/games";
        }
        gameService.deleteById(id);
        return "redirect:/games";
    }
}


