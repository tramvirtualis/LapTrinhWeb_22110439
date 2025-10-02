package com.example.category.controller;

import com.example.category.entity.Game;
import com.example.category.entity.Review;
import com.example.category.entity.User;
import com.example.category.service.GameService;
import com.example.category.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private GameService gameService;

    @GetMapping("/game/{gameId}")
    public String gameReviews(@PathVariable Long gameId,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "5") int size,
                             Model model, HttpSession session) {
        Game game = gameService.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found"));
        Page<Review> reviewPage = reviewService.getGameReviews(game, page, size);
        
        model.addAttribute("game", game);
        model.addAttribute("reviewPage", reviewPage);
        
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("userRole", user.getRole().toString());
            model.addAttribute("username", user.getUsername());
            
            // Check if user has purchased this game
            boolean hasPurchased = reviewService.getUserReviewForGame(user, game).isPresent() || 
                                 (user.getRole() == User.Role.CUSTOMER && reviewService.getUserReviewForGame(user, game).isEmpty());
            model.addAttribute("hasPurchased", hasPurchased);
            
            // Get user's existing review if any
            Optional<Review> userReview = reviewService.getUserReviewForGame(user, game);
            model.addAttribute("userReview", userReview.orElse(null));
        }
        
        return "review/list";
    }

    @GetMapping("/add/{gameId}")
    public String addReviewForm(@PathVariable Long gameId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.CUSTOMER) {
            return "redirect:/login";
        }

        Game game = gameService.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found"));
        model.addAttribute("game", game);
        model.addAttribute("review", new Review());
        model.addAttribute("userRole", user.getRole().toString());
        model.addAttribute("username", user.getUsername());
        
        return "review/form";
    }

    @PostMapping("/add/{gameId}")
    public String addReview(@PathVariable Long gameId,
                           @Valid @ModelAttribute("review") Review review,
                           BindingResult bindingResult,
                           Model model,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.CUSTOMER) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            Game game = gameService.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found"));
            model.addAttribute("game", game);
            return "review/form";
        }

        try {
            Game game = gameService.findById(gameId).orElseThrow(() -> new IllegalArgumentException("Game not found"));
            reviewService.createReview(user, game, review.getRating(), review.getComment());
            redirectAttributes.addFlashAttribute("success", "Review added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/reviews/game/" + gameId;
    }

    @GetMapping("/edit/{reviewId}")
    public String editReviewForm(@PathVariable Long reviewId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.CUSTOMER) {
            return "redirect:/login";
        }

        // Get review and check ownership
        Optional<Review> reviewOpt = reviewService.getUserReviewForGame(user, reviewService.getUserReviewForGame(user, null).orElse(null).getGame());
        if (reviewOpt.isEmpty()) {
            return "redirect:/games";
        }

        model.addAttribute("review", reviewOpt.get());
        model.addAttribute("userRole", user.getRole().toString());
        model.addAttribute("username", user.getUsername());
        
        return "review/form";
    }

    @PostMapping("/edit/{reviewId}")
    public String editReview(@PathVariable Long reviewId,
                           @Valid @ModelAttribute("review") Review review,
                           BindingResult bindingResult,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.CUSTOMER) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            return "review/form";
        }

        try {
            reviewService.updateReview(reviewId, user, review.getRating(), review.getComment());
            redirectAttributes.addFlashAttribute("success", "Review updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/games";
    }

    @PostMapping("/delete/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            if (user.getRole() == User.Role.ADMIN) {
                reviewService.deleteReviewByAdmin(reviewId);
                redirectAttributes.addFlashAttribute("success", "Review deleted successfully!");
            } else {
                reviewService.deleteReview(reviewId, user);
                redirectAttributes.addFlashAttribute("success", "Review deleted successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/games";
    }
}
